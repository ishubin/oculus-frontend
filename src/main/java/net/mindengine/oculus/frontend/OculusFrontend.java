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
package net.mindengine.oculus.frontend;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.grid.storage.DefaultGridStorage;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;

public class OculusFrontend {

    private net.mindengine.oculus.grid.server.Server gridServer;
    private Config config;
    public OculusFrontend(Config config) {
        this.config = config;
    }

    public void startGrid() {
        gridServer = new net.mindengine.oculus.grid.server.Server();
        
        DefaultGridStorage storage = new DefaultGridStorage();
        storage.setStoragePath(config.getGridServerStorage());
        gridServer.setStorage(storage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gridServer.startServer(config.getGridServerPort(), config.getGridServerName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
    }
    
    public void start() throws Exception {
        if (config.getGridEmbedded() ) {
            startGrid();
        }
        
        //Starting frontend web-server
        Server server = new Server(config.getOculusServerPort());
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
         OculusFrontend frontend = new OculusFrontend(Config.getInstance());
         frontend.start();
    }
}
