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
