/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.experior.suite.Suite;
import net.mindengine.oculus.experior.suite.XmlSuiteParser;
import net.mindengine.oculus.experior.test.descriptors.TestDefinition;
import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.trm.AgentTagRulesContainer;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.utils.FileUtils;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.frontend.web.controllers.display.FileDisplayController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;
import net.mindengine.oculus.grid.domain.task.DefaultTask;
import net.mindengine.oculus.grid.domain.task.SuiteTask;
import net.mindengine.oculus.grid.domain.task.TaskUser;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Runs the task or saves it in TRMServer scheduler
 * 
 * @author Ivan Shubin
 * 
 */
public class RunTaskController extends SecureSimpleViewController {
	private TrmDAO trmDAO;
	private TestDAO testDAO;
	private Config config;
	private ProjectDAO projectDAO;
	private AgentTagRulesContainer agentTagRulesContainer;
	private Log logger = LogFactory.getLog(getClass());

	
	@Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
         * Fetching the task
         */
        Map<String, Object> map = new HashMap<String, Object>();
        Long taskId = Long.parseLong(request.getParameter("taskId"));

        TrmTask task = trmDAO.getTask(taskId);

        if (task == null)
            throw new UnexistentResource("The task doesn't exist");

        Collection<TrmTask> taskDependencies = trmDAO.getDependentTasks(taskId);
        String submit = request.getParameter("Submit");
        task.setParameters(trmDAO.getTaskProperties(task.getProjectId(), taskId, TrmDAO.PROPERTY_TYPE_SUITE_PARAMETER));
        
        if (submit == null) {
            map.put("task", task);
            map.put("tasks", taskDependencies);
            
            /*
             * Checking the connection with the TRMServer.
             */
            ClientServerRemoteInterface server = null;
            try {
                server = config.lookupGridServer();
            }
            catch (Exception e) {
                logger.error("Can't connect to G", e);
                return new ModelAndView("trm-server-not-found");
            }
            
            /*
             * Fetching all available agents
             */
            AgentStatus[] agents = server.getAgents();
            agentTagRulesContainer.wrapAgentTags(agents);
            map.put("agents", agents);
            map.put("agentsCount", agents.length);
            map.put("agentTags", agentTagRulesContainer.fetchAllAgentWrappedTags(agents));
            
            ModelAndView mav = new ModelAndView("trm-run-task", map);
            return mav;
        }
        else if (submit.equals("Run Task")) {
            /*
             * Sending all tasks to TRMServer
             */
            for(TrmTask trmTask : taskDependencies){
                trmTask.setParameters(task.getParameters());
                runTask(trmTask, request);
            }
            runTask(task, request);
            
            return new ModelAndView(new RedirectView("../grid/my-active-tasks"));
        }
        else if (submit.equals("Export Task")) {
            taskDependencies.add(task);
            return exportTasks(taskDependencies, request, response);
        }
        else
            throw new InvalidRequest();
    }
	
	/**
	 * Sends the task to {@link TRMServer} and updates the user tasks in
	 * {@link Session}
	 * 
	 * @param trmTask
	 * @param request
	 * @throws Exception
	 */
	public void runTask(TrmTask trmTask, HttpServletRequest request) throws Exception {
	    User user = getAuthorizedUser(request);
	    DefaultTask task = createGridTask(trmTask, trmDAO, testDAO, projectDAO, new TaskUser(user.getId(), user.getName()));
	    
	    String build = request.getParameter("build");
        if (build.equals("Current Version")) {
            build = "current";
        }
        
        String[] selectedAgents = fetchSelectedAgents(request.getParameter("selectedAgents"));
        
        Map<String, String> suiteParameters = gatherSuiteParametersFromRequest(trmTask, request);
        for ( SuiteTask suiteTask : task.getSuiteTasks()) {
	        suiteTask.getSuite().setParameters(suiteParameters);
	        suiteTask.setProjectVersion(build);
	        suiteTask.setAgentNames(selectedAgents);
	    }
        
        ClientServerRemoteInterface server = config.lookupGridServer();
        server.runTask(task);
	}

    private Map<String, String> gatherSuiteParametersFromRequest(TrmTask trmTask, HttpServletRequest request) {
        Map<String, String> suiteParameters = new HashMap<String, String>();
        for (TrmProperty property : trmTask.getParameters()) {
            String value = request.getParameter("sp_" + property.getId());
            if (value == null)
                value = "";
            suiteParameters.put(property.getName(), value);
        }
        return suiteParameters;
    }
	
	
	private String[] fetchSelectedAgents(String agentsString) {
	    if(agentsString!=null && !agentsString.trim().isEmpty()) {
	        String[] agents = agentsString.split(",");
	        return agents;
	    }
        return null;
    }

	public static DefaultTask createGridTask(TrmTask trmTask, TrmDAO trmDAO, TestDAO testDAO, ProjectDAO projectDAO, TaskUser taskUser) throws Exception {
        /*
         * Fetching suites from specified task
         */
        List<TrmSuite> rootSuites = trmDAO.getTaskEnabledSuites(trmTask.getId());
        
        List<TrmSuite> suites = new LinkedList<TrmSuite>();

        suites.addAll(rootSuites);

        if(suites.size()>0){
            trmTask.setSuites(suites);
            
            DefaultTask task = new DefaultTask();
            task.setTaskUser(taskUser);
            task.setName(trmTask.getName());
            List<SuiteTask> tasks = new LinkedList<SuiteTask>();
            
            
            //Used for fetching the mapping, name, parent project path
            Map<Long, Test> cashedTests = new HashMap<Long, Test>();
            //Used for fetching parent project path for tests
            Map<Long, String> cashedProjectPath = new HashMap<Long, String>();
            
            for (TrmSuite trmSuite : trmTask.getSuites()) {
                if(trmSuite.getSuiteData()!=null && !trmSuite.getSuiteData().isEmpty()) {
                    Project project = projectDAO.getProject(trmTask.getProjectId());
                    SuiteTask suiteTask = new SuiteTask();
    
                    // Setting the name of the projects folder and its jar prefix which
                    // is the same as project path in DB
                    suiteTask.setProjectName(project.getPath());
                    suiteTask.setName(trmSuite.getName());
                    
                    
                    Suite suite = TrmSuite.convertSuiteFromJSON(StringEscapeUtils.unescapeJavaScript(trmSuite.getSuiteData()), testDAO);
                    //Filling test definition with all missing data
                    for(TestDefinition td : suite.getTests()){
                        fillTestDefinition(testDAO, projectDAO, td, cashedTests, cashedProjectPath);
                    }
                    
                    suite.setName(trmSuite.getName());
    
                    suite.setRunnerId(taskUser.getId());
                    suiteTask.setSuite(suite);
    
                    
                    tasks.add(suiteTask);
                } 
            }

            task.setSuiteTasks(tasks);
            return task;
        }
        else throw new IllegalArgumentException("There are no suites for this task");
    }

    private static Test fetchTest(TestDAO testDAO, ProjectDAO projectDAO, Long testId, Map<Long, Test> cashedTests, Map<Long, String> cashedProjectPath) throws Exception{
	    Test test = cashedTests.get(testId);
	    if(test==null){
	        test = testDAO.getTest(testId);
	        
	        String projectPath = cashedProjectPath.get(test.getProjectId());
	        if(projectPath == null){
	            Long rootId = projectDAO.getProjectRootId(test.getProjectId(), 5);
	            projectPath = projectDAO.getProject(rootId).getPath();
	            cashedProjectPath.put(test.getProjectId(), projectPath);
	        }
	        
	        test.setParentProjectPath(projectPath);
	        cashedTests.put(testId, test);
	    }
	    return test;
	}
	
	
	/**
	 * Used in order to fetch all important test data and put it to test definition which will later be sent to the grid
	 * @param td
	 * @param cashedTests Stores all tests which are used in suites. Used in order to decrease the amount of db queries
	 * @param cashedProjectPath Stores root project paths used in test
	 * @throws Exception 
	 */
	private static void fillTestDefinition(TestDAO testDAO, ProjectDAO projectDAO, TestDefinition td, Map<Long, Test> cashedTests, Map<Long, String> cashedProjectPath) throws Exception{
	    if(td.getTestId() != null) {
	        Test test = fetchTest(testDAO, projectDAO, td.getTestId(), cashedTests, cashedProjectPath);
	        td.setMapping(test.getMapping());
	        td.setProject(test.getParentProjectPath());
	        td.setName(test.getName());
	    }
	    if ( td.getInjectedTests() != null && td.getInjectedTests().size() > 0 ) {
	        for ( TestDefinition injectedTestDiDefinition : td.getInjectedTests() ) {
	            fillTestDefinition(testDAO, projectDAO, injectedTestDiDefinition, cashedTests, cashedProjectPath);
	        }
	    }
	}

	

	
	public ModelAndView exportTasks(Collection<TrmTask> tasks, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	    String folderName = "" + new Date().getTime() + new Random().nextInt(1000);
        File rootFolder = new File(config.getDataFolder() + File.separator + folderName);
        logger.info("Creating temp directory " + rootFolder.getPath());
        
        rootFolder.mkdirs();
        
        for(TrmTask task : tasks){
            exportTask(task, rootFolder, request);
        }

		// Compressing the root folder to zip
		String zipPath = rootFolder.getPath() + ".zip";

		FileUtils.zipDirectory(zipPath, rootFolder.getPath());

		FileDisplayController.showFile(response, zipPath, FileUtils.convertName("task_export") + ".zip", "application/zip");

		FileUtils.removeDirectory(rootFolder);
		FileUtils.removeFile(zipPath);
		return null;
	}
	
	public void exportTask(TrmTask task, File rootFolder, HttpServletRequest request) throws Exception{
	    File taskFolder = new File(rootFolder.getAbsolutePath()+File.separator+FileUtils.convertName(task.getName()));
	    taskFolder.mkdir();
        
        // Exporting root suites of the task
        exportSuites(task, taskFolder, trmDAO.getTaskEnabledSuites(task.getId()), request);
	}

	public void exportSuites(TrmTask trmTask, File rootFolder, List<TrmSuite> suites, HttpServletRequest request) throws Exception {
		User user = getUser(request);
		List<TrmProperty> suiteProperties = trmDAO.getProperties(trmTask.getProjectId(), TrmDAO.PROPERTY_TYPE_SUITE_PARAMETER);
		
		/*
         * Gathering all suite parameters sent from client
         */
        Map<String, String> suiteParameters = new HashMap<String, String>();
        for (TrmProperty property : suiteProperties) {
            String value = request.getParameter("sp_" + property.getId());
            if (value == null)
                value = "";
            suiteParameters.put(property.getName(), value);
        }
		
		for (TrmSuite trmSuite : suites) {
		    if(trmSuite.getSuiteData()!=null && !trmSuite.getSuiteData().isEmpty()){
    			Suite suite = TrmSuite.convertSuiteFromJSON(StringEscapeUtils.unescapeJavaScript(trmSuite.getSuiteData()), testDAO);
    			suite.setAgentName("");
    			
    			suite.setParameters(suiteParameters);
    			suite.setName(trmSuite.getName());
    
    			suite.setRunnerId(user.getId());
    			File suiteFile = new File(rootFolder.getPath() + File.separator + FileUtils.convertName(suite.getName()) + ".xml");
    			XmlSuiteParser.saveSuite(suite, suiteFile);
		    }
		}
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }


    public AgentTagRulesContainer getAgentTagRulesContainer() {
        return agentTagRulesContainer;
    }


    public void setAgentTagRulesContainer(AgentTagRulesContainer agentTagRulesContainer) {
        this.agentTagRulesContainer = agentTagRulesContainer;
    }
}
