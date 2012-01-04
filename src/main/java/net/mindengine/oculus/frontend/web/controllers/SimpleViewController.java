package net.mindengine.oculus.frontend.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SimpleViewController implements Controller {
	private String view;

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = handleController(request);
		ModelAndView mav = new ModelAndView(view);
		if (model != null)
			mav.addAllObjects(model);
		return mav;
	}

}
