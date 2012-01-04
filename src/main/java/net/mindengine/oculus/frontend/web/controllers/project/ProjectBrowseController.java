package net.mindengine.oculus.frontend.web.controllers.project;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseResult;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Displays only the root projects.
 * 
 * @author Ivan Shubin
 * 
 */
public class ProjectBrowseController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private DocumentDAO documentDAO;

	private ProjectBrowseFilter filter;

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();

		filter.setOnlyRoot(true);
		ProjectBrowseResult projectBrowseResult = projectDAO.browseProjects(filter);
		map.put("projectBrowseResult", projectBrowseResult);
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object object = super.formBackingObject(request);
		setFilter((ProjectBrowseFilter) object);
		return object;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		setFilter((ProjectBrowseFilter) command);
		mav.addObject("projectBrowseFilter", command);
		mav.addAllObjects(referenceData(request));
		return mav;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public void setFilter(ProjectBrowseFilter filter) {
		this.filter = filter;
	}

	public ProjectBrowseFilter getFilter() {
		return filter;
	}
}
