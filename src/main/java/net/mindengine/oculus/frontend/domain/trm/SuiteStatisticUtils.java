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
package net.mindengine.oculus.frontend.domain.trm;

import net.mindengine.oculus.grid.domain.task.SuiteStatistic;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.domain.task.TaskStatus;

public class SuiteStatisticUtils {
    
    public static SuiteStatistic collectStatisticFromTasks(TaskInformation[] tasks) {
        SuiteStatistic statistic = new SuiteStatistic();
        statistic.setFailed(0);
        statistic.setFinished(0);
        statistic.setPassed(0);
        statistic.setPostponed(0);
        statistic.setTotal(0);
        statistic.setWarning(0);
        
        for ( TaskInformation task : tasks ) {
            if ( task.getTaskStatus().getStatus() == TaskStatus.COMPLETED && task.getTaskStatus().getSuiteInformation() != null ) {
                SuiteStatistic ss = task.getTaskStatus().getSuiteInformation().calculateStatistics();
                
                statistic.setFailed(statistic.getFailed() + ss.getFailed());
                statistic.setFinished(statistic.getFinished() + ss.getFinished());
                statistic.setPassed(statistic.getPassed() + ss.getPassed());
                statistic.setPostponed(statistic.getPostponed() + ss.getPostponed());
                statistic.setTotal(statistic.getTotal() + ss.getTotal());
                statistic.setWarning(statistic.getWarning() + ss.getWarning());
            }
        }
        
        return statistic;
    }

    public static String getPrettyStatistics(SuiteStatistic statistics) {
        StringBuffer text = new StringBuffer();
        text.append("<br/> ");
        text.append("<span style='color:red;' title='Failed'>");
        text.append(statistics.getFailed());
        text.append("</span> / ");
        text.append("<span style='color:#EBB521;' title='Warn'>");
        text.append(statistics.getWarning());
        text.append("</span> / ");
        text.append("<span style='color:green;' title='Passed'>");
        text.append(statistics.getPassed());
        text.append("</span>");
        
        text.append(" from <span style='font-weight:bold;' title='Total'> (");
        text.append(statistics.getTotal());
        text.append(") </span>");
        return text.toString();
    }

}
