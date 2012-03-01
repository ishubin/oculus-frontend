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
package net.mindengine.oculus.frontend.web.controllers.trm.customize;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.domain.trm.TrmPropertyStub;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class AjaxFetchSuiteParametersController extends SimpleAjaxController {
	private TrmDAO trmDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		AjaxModel model = new AjaxModel();
		Long projectId = Long.parseLong(request.getParameter("projectId"));

		List<TrmProperty> list = trmDAO.getProperties(projectId, TrmProperty._TYPE_SUITE_PARAMETER);
		List<TrmPropertyStub> properties = new LinkedList<TrmPropertyStub>();
		for (TrmProperty property : list) {
			properties.add(new TrmPropertyStub(property));
		}

		model.setObject(properties);
		model.setResult("fetched");
		return model;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
