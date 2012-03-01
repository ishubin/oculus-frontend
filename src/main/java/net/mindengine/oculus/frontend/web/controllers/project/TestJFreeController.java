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
package net.mindengine.oculus.frontend.web.controllers.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class TestJFreeController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("A", new Integer(75));
		pieDataset.setValue("B", new Integer(10));
		pieDataset.setValue("C", new Integer(10));
		pieDataset.setValue("D", new Integer(5));

		JFreeChart chart = ChartFactory.createPieChart("CSC408 Mark Distribution", // Title
				pieDataset, // Dataset
				true, // Show legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);

		response.setCharacterEncoding("image/jpeg");
		response.setStatus(200);
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500, 400);
		return null;
	}

}
