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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.chart;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.service.chart.ChartDAO;
import net.mindengine.oculus.experior.chart.Chart;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class CreateChartController extends SecureSimpleViewController {

	private ChartDAO chartDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Chart chart = new Chart();
		chart.setTitle(request.getParameter("title"));
		chart.setType(request.getParameter("type"));
		chart.setXAxisName(request.getParameter("xAxisName"));
		chart.setXAxisType(request.getParameter("xAxisType"));
		chart.setYAxisName(request.getParameter("yAxisName"));
		chart.setYAxisType(request.getParameter("yAxisType"));
		chart.setData(request.getParameter("data"));

		long chartId = chartDAO.createChart(chart);

		response.setContentType("html/text");
		PrintWriter pw = response.getWriter();
		pw.write("" + chartId);
		pw.close();
		return null;
	}

	public void setChartDAO(ChartDAO chartDAO) {
		this.chartDAO = chartDAO;
	}

	public ChartDAO getChartDAO() {
		return chartDAO;
	}

}
