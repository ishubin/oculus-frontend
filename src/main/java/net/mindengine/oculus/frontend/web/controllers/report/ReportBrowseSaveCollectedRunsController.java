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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.report.SavedRun;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ReportBrowseSaveCollectedRunsController extends SecureSimpleViewController {
	private TestRunDAO testRunDAO;
	private Config config;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		String name = request.getParameter("saveName");
		String redirect = request.getParameter("redirect");
		if (redirect == null) {
			redirect = "";
		}
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		List<TestRunSearchData> collectedTestRuns = session.getCollectedTestRuns();

		SavedRun savedRun = new SavedRun();
		savedRun.setDate(new Date());
		savedRun.setName(name);
		savedRun.setUserId(user.getId());

		Long id = testRunDAO.saveRun(savedRun);

		savedRun.setId(id);
		FileUtils.mkdirs(config.getDataFolder() + File.separator + savedRun.generateDirUrl());

		File file = new File(config.getDataFolder() + File.separator + savedRun.generateFileUrl());

		file.createNewFile();

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(collectedTestRuns);
		oos.flush();
		oos.close();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(savedRun.getDate());

		String convertedName = savedRun.getName().replaceAll("[^a-zA-Z0-9]", "_");

		String url = "../report/saved-" + user.getLogin() + "-" + strDate + "-" + convertedName + "-" + id;
		session.setTemporaryMessage("Your collected test runs were successfully saved." + " You can use them with the following url:<br/>" + "HTML: <a href=\"" + url + ".html\">Html version</a>" + "<br/>" + "Excel: <a href=\"" + url + ".xls\">Excel version</>");
		return new ModelAndView(new RedirectView(redirect));
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
}
