package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.io.File;
import java.util.ArrayList;
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
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
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
	private Log logger = LogFactory.getLog(getClass());

	/**
	 * Sends the task to {@link TRMServer} and updates the user tasks in
	 * {@link Session}
	 * 
	 * @param trmTask
	 * @param request
	 * @throws Exception
	 */
	public void runTask(TrmTask trmTask, HttpServletRequest request) throws Exception {
	    /*
         * Fetching suite groups and suites from specified task
         */
        List<TrmSuiteGroup> groups = trmDAO.getTaskEnabledSuiteGroups(trmTask.getId());
        List<TrmSuite> rootSuites = trmDAO.getTaskEnabledSuites(trmTask.getId(), 0L);
        
        List<TrmSuite> suites = new LinkedList<TrmSuite>();

        for (TrmSuiteGroup group : groups) {
            suites.addAll(trmDAO.getTaskEnabledSuites(trmTask.getId(), group.getId()));
        }

        suites.addAll(rootSuites);

        if(suites.size()>0){
            trmTask.setSuites(suites);
            
            User user = Session.create(request).getAuthorizedUser();
            TaskUser taskUser = new TaskUser();
            taskUser.setId(user.getId());
            taskUser.setName(user.getName());

            ClientServerRemoteInterface server = config.getGridServer();
            DefaultTask task = new DefaultTask();
            task.setTaskUser(taskUser);
            task.setName(trmTask.getName());
            List<SuiteTask> tasks = new LinkedList<SuiteTask>();
            
            /*
             * Fetching the agents list
             */
            String[] agentNames = null;
            if ("on".equals(request.getParameter("task_" + trmTask.getId() + "_choose_agents"))) {
                Integer agentsCount = Integer.parseInt(request.getParameter("agentsCount"));
                List<String> agentList = new LinkedList<String>();
                for (int i = 0; i < agentsCount; i++) {
                    if ("on".equals(request.getParameter("task_" + trmTask.getId() + "_agent_chk_" + i))) {
                        agentList.add(request.getParameter("task_" + trmTask.getId() + "_agent_name_" + i));
                    }
                }
                if (agentList.size() > 0) {
                    agentNames = new String[agentList.size()];
                    int i = 0;
                    for (String agentName : agentList) {
                        agentNames[i] = agentName;
                        i++;
                    }   
                }
            }

            /*
             * Gathering all task parameters sent from client
             */
            Map<String, String> suiteParameters = new HashMap<String, String>();
            for (TrmProperty property : trmTask.getParameters()) {
                String value = request.getParameter("task_" + trmTask.getId() + "_parameter_" + property.getId());
                if (value == null)
                    value = "";
                suiteParameters.put(property.getName(), value);
            }


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
                    
                    
                    String build = request.getParameter("task_" + trmTask.getId() + "_build");
                    if (!build.equals("Current Version")) {
                        suiteTask.setProjectVersion(build);
                    }
                    else suiteTask.setProjectVersion("current");
    
                    Suite suite = TrmSuite.convertSuiteFromJSON(StringEscapeUtils.unescapeJavaScript(trmSuite.getSuiteData()));
                    //Filling test definition with all missing data
                    for(TestDefinition td : suite.getTests()){
                        fillTestDefinition(td, cashedTests, cashedProjectPath);
                    }
                    
                    suite.setParameters(suiteParameters);
                    suite.setName(trmSuite.getName());
    
                    suite.setRunnerId(user.getId());
                    suiteTask.setSuite(suite);
    
                    suiteTask.setAgentNames(agentNames);
                    tasks.add(suiteTask);
                } 
            }

            task.setSuiteTasks(tasks);

            /*
             * Running task on grid server
             */
            server.runTask(task);
        }
	}
	
	
	public Test fetchTest(Long testId, Map<Long, Test> cashedTests, Map<Long, String> cashedProjectPath) throws Exception{
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
	public void fillTestDefinition(TestDefinition td, Map<Long, Test> cashedTests, Map<Long, String> cashedProjectPath) throws Exception{
	    if(td.getTestId() != null) {
	        Test test = fetchTest(td.getTestId(), cashedTests, cashedProjectPath);
	        td.setMapping(test.getMapping());
	        td.setProject(test.getParentProjectPath());
	        td.setName(test.getName());
	    }
	}

	/**
	 * Used for cashing trm properties for common projects
	 * 
	 * @param projectId
	 * @param cashedProjectProperties
	 * @return
	 * @throws Exception
	 */
	public Collection<TrmProperty> fetchTrmProperty(Long projectId, Map<Long, Collection<TrmProperty>> cashedProjectProperties) throws Exception {
		if (cashedProjectProperties.containsKey(projectId)) {
			return cashedProjectProperties.get(projectId);
		}
		else {
			Collection<TrmProperty> properties = trmDAO.getProperties(projectId, TrmDAO.PROPERTY_TYPE_SUITE_PARAMETER);
			cashedProjectProperties.put(projectId, properties);
			return properties;
		}
	}	

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

		
		//TODO Fetch all tasks from dependencies and order them by dependency
		//TODO For each task fetch related parameters
		Collection<TrmTask> tasks = new LinkedList<TrmTask>();
		tasks.add(task);
		tasks.addAll(trmDAO.getDependentTasks(taskId));
		
		Map<Long, Collection<TrmProperty>> cashedProjectProperties = new HashMap<Long, Collection<TrmProperty>>();
		
		for (TrmTask trmTask : tasks){
		    trmTask.setParameters(fetchTrmProperty(trmTask.getProjectId(), cashedProjectProperties));
		}
		
		String submit = request.getParameter("Submit");

		if (submit == null) {
			map.put("task", task);
			map.put("tasks", tasks);
			
			String[] dayHours = new String[24];
			for (int i = 0; i < 24; i++) {
				dayHours[i] = "" + i;
			}
			List<String> weekDays = new ArrayList<String>();
			weekDays.add("Sunday");
			weekDays.add("Monday");
			weekDays.add("Tuesday");
			weekDays.add("Wednesday");
			weekDays.add("Thursday");
			weekDays.add("Friday");
			weekDays.add("Saturday");
			map.put("weekDays", weekDays);
			map.put("dayHours", dayHours);
			
			/*
	         * Checking the connection with the TRMServer.
	         */
	        ClientServerRemoteInterface server = null;
	        try {
	            server = config.getGridServer();
	        }
	        catch (Exception e) {
	            logger.error("Can't connect to TRMServer", e);
	            return new ModelAndView("trm-server-not-found");
	        }
	        
			/*
			 * Fetching all available agents
			 */
			AgentStatus[] agents = server.getAgents();
			map.put("agents", agents);
			map.put("agentsCount", agents.length);
			
			ModelAndView mav = new ModelAndView("trm-run-task", map);
			return mav;
		}
		else if (submit.equals("Run Task")) {
		    /*
		     * Sending all tasks to TRMServer
		     */
		    for(TrmTask trmTask : tasks){
		        runTask(trmTask, request);
		    }
			
			if ("on".equals(request.getParameter("useScheduler"))) {
                return new ModelAndView(new RedirectView("../test-run-manager/scheduler"));
            }
            else {
                return new ModelAndView(new RedirectView("../test-run-manager/my-active-tasks"));
            }
		}
		else if (submit.equals("Export Task")) {
			return exportTasks(tasks, request, response);
		}
		else
			throw new InvalidRequest();
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
        // Creating the enabled suite groups folders
        List<TrmSuiteGroup> groups = trmDAO.getTaskEnabledSuiteGroups(task.getId());

        for (TrmSuiteGroup group : groups) {
            File groupFolder = new File(taskFolder.getPath() + File.separator + FileUtils.convertName(group.getName()));
            groupFolder.mkdir();

            exportSuites(task, groupFolder, trmDAO.getTaskEnabledSuites(task.getId(), group.getId()), request);
        }

        // Exporting root suites of the task
        exportSuites(task, taskFolder, trmDAO.getTaskEnabledSuites(task.getId(), 0L), request);
	}

	public void exportSuites(TrmTask trmTask, File rootFolder, List<TrmSuite> suites, HttpServletRequest request) throws Exception {
		User user = getUser(request);
		List<TrmProperty> suiteProperties = trmDAO.getProperties(trmTask.getProjectId(), TrmDAO.PROPERTY_TYPE_SUITE_PARAMETER);
		
		/*
         * Gathering all suite parameters sent from client
         */
        Map<String, String> suiteParameters = new HashMap<String, String>();
        for (TrmProperty property : suiteProperties) {
            String value = request.getParameter("task_" + trmTask.getId() + "_parameter_" + property.getId());
            if (value == null)
                value = "";
            suiteParameters.put(property.getName(), value);
        }
		
		for (TrmSuite trmSuite : suites) {
		    if(trmSuite.getSuiteData()!=null && !trmSuite.getSuiteData().isEmpty()){
    			Suite suite = TrmSuite.convertSuiteFromJSON(StringEscapeUtils.unescapeJavaScript(trmSuite.getSuiteData()));
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
}
