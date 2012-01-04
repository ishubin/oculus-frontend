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
