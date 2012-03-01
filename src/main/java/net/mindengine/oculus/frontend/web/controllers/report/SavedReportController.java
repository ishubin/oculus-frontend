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
package net.mindengine.oculus.frontend.web.controllers.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.report.SavedRun;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.report.ExcelReport;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class SavedReportController extends SecureSimpleViewController {
	private TestRunDAO testRunDAO;
	private Config config;
	private String type;

	/**
	 * This method is overridden as it checks the type of current document
	 * either it is 'html' or 'xls' In case if the 'html' document was requested
	 * the controller will work as usual controller In case if the 'xls'
	 * document was requested the controller will print the xls file to response
	 * instead of returning ModelAndView
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (type.equals("html")) {
			return super.handleRequest(request, response);
		}
		else {
			// The requested document is a file

			List<TestRunSearchData> testRuns;
			SavedRun savedRun = getSavedRun(request);
			testRuns = getSavedTestRuns(savedRun);

			OutputStream outputStream = response.getOutputStream();
			response.setContentType("application/ms-excel");
			ExcelReport xlsReport = new ExcelReport();
			xlsReport.setConfig(config);
			xlsReport.setOutputStream(outputStream);
			xlsReport.writeDocument(savedRun, testRuns);
			outputStream.flush();
			outputStream.close();
			return null;
		}
	}

	private SavedRun getSavedRun(HttpServletRequest request) throws Exception {
		String shortUrl = request.getPathInfo().substring(7).replace("." + type, "");
		String arr[] = shortUrl.split("-");
		if (arr == null || arr.length != 6)
			throw new Exception("Url is incorrect");
		Long id = Long.parseLong(arr[5]);
		SavedRun savedRun = testRunDAO.getSavedRunById(id);
		if (savedRun == null)
			throw new UnexistentResource("There is no saved run in DB with id '" + id + "'");
		return savedRun;
	}

	@SuppressWarnings("unchecked")
	private List<TestRunSearchData> getSavedTestRuns(SavedRun savedRun) throws Exception {
		List<TestRunSearchData> testRuns;
		File file = new File(config.getDataFolder() + File.separator + savedRun.generateFileUrl());
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		testRuns = (List<TestRunSearchData>) ois.readObject();
		return testRuns;
	}

	/**
	 * This method will be called only in case if the document was requested as
	 * 'html'
	 */
	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		List<TestRunSearchData> testRuns;
		SavedRun savedRun = getSavedRun(request);
		testRuns = getSavedTestRuns(savedRun);
		model.put("savedRun", savedRun);
		model.put("savedTestRuns", testRuns);
		model.put("title", getTitle());
		model.put("config", config);
		return model;
	}

	public TestRunDAO getTestRunDAO() {
		return testRunDAO;
	}

	public void setTestRunDAO(TestRunDAO testRunDAO) {
		this.testRunDAO = testRunDAO;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
