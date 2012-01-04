package net.mindengine.oculus.frontend.web.controllers.display;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class HomeController extends SecureSimpleViewController {
	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", getTitle());
		return map;
	}
}
