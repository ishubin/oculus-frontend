package net.mindengine.oculus.frontend.web.controllers.customization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class DeleteParameterController extends SecureSimpleViewController {
	private CustomizationDAO customizationDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.parseLong(request.getParameter("id"));
		String projectPath = request.getParameter("projectPath");
		String unit = request.getParameter("unit");

		customizationDAO.deleteCustomization(id);

		return new ModelAndView("redirect:../customization/project-" + projectPath + "?unit=" + unit);
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}
}
