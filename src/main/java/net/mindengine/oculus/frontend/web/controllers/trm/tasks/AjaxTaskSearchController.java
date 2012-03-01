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
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.experior.utils.XmlUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class AjaxTaskSearchController extends SecureSimpleViewController {

    private TrmDAO trmDAO;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        
        OutputStream os = response.getOutputStream();
        response.setContentType("text/xml");

        String rId = request.getParameter("id");

        OutputStreamWriter w = new OutputStreamWriter(os);

        

        w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        w.write("<tree id=\"" + rId + "\" >");

        if(rId.equals("0")){
            w.write("<item text=\"My Tasks\" " + "id=\"mytasks\" " + "im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" child=\"1\" " + " nocheckbox=\"1\" >");
            w.write("</item>");
            w.write("<item text=\"Shared Tasks\" " + "id=\"shared\" " + "im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" child=\"1\" " + " nocheckbox=\"1\" >");
            w.write("</item>");
        }
        else {
            User user = getUser(request);
            
            Collection<TrmTask> tasks = null;
            Long projectId = null;
            String strProject = request.getParameter("projectId");
            if(strProject!=null) {
                projectId = Long.parseLong(strProject);
            }
            
            if (rId.equals("mytasks")) {
                tasks = trmDAO.getUserTasks(user.getId(), projectId);
                
            }
            else if (rId.equals("shared")){
                Collection<User> users = trmDAO.getUsersWithSharedTasks(user.getId(), projectId);
                for(User u : users){
                    w.write("<item text=\"" + XmlUtils.escapeXml(u.getName()) + "\" " + "id=\"u" + u.getId() + "\" " + "im0=\"workflow-icon-user.png\" im1=\"workflow-icon-user.png\" im2=\"workflow-icon-user.png\" child=\"1\" " + " nocheckbox=\"1\" >");
                    w.write("</item>");
                }
            }
            else if (rId.startsWith("u")) {
                Long userId = Long.parseLong(rId.substring(1));
                tasks = trmDAO.getUserSharedTasks(userId, projectId);
            }
            
            if(tasks!=null){
                for (TrmTask task : tasks) {
                    w.write("<item text=\"" + XmlUtils.escapeXml(task.getName()) + "\" " + "id=\"t" + task.getId() + "\" " + "im0=\"iconTask.png\" im1=\"iconTask.png\" im2=\"iconTask.png\" child=\"0\" " + " >");
                    w.write("</item>");
                }
            }
                    
            

        }
        
        w.write("</tree>");
        w.flush();
        os.flush();
        os.close();
        return null;
    }
    
    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }

    public TrmDAO getTrmDAO() {
        return trmDAO;
    }
}
