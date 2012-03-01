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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend;

import net.mindengine.oculus.grid.storage.DefaultGridStorage;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;

public class OculusFrontend {

    private net.mindengine.oculus.grid.server.Server gridServer;
    
    public OculusFrontend() {
        
    }
    
    public void startGrid() {
        gridServer = new net.mindengine.oculus.grid.server.Server();
        
        DefaultGridStorage storage = new DefaultGridStorage();
        storage.setStoragePath("../data/storage-server");
        gridServer.setStorage(storage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gridServer.startServer(8081, "grid");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
    }
    
    public void start() throws Exception {
        startGrid();
        
        //Starting frontend web-server
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWar("webapp");
        context.setExtraClasspath("webapp");
        context.setParentLoaderPriority(true);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context});
        server.setHandler(context);
        server.start();
        
    }
    
    public static void main(String[] args) throws Exception {
         OculusFrontend frontend = new OculusFrontend();
         frontend.start();
    }
}
