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
package net.mindengine.oculus.frontend.web.controllers.dashboard;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.web.Session;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ChartDashboardController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer width;
		Integer height;

		if (request.getParameter("width") != null) {
			width = Integer.parseInt(request.getParameter("width"));
		}
		else
			width = 500;

		if (request.getParameter("height") != null) {
			height = Integer.parseInt(request.getParameter("height"));
		}
		else
			height = 400;

		String chartId = request.getPathInfo().substring(7);
		Session session = Session.create(request);
		JFreeChart chart = session.getChart(chartId);

		OutputStream os = response.getOutputStream();
		ChartUtilities.writeChartAsPNG(os, chart, width, height);
		os.flush();
		os.close();
		return null;
	}

}
