package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class ScreenshotController extends SecureSimpleViewController {
	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", request.getParameter("type"));
		map.put("object", request.getParameter("object"));
		map.put("item", request.getParameter("item"));
		return map;
	}

}
