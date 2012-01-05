package net.mindengine.oculus.frontend;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;




public class OculusFrontend {

    public static void main(String[] args) throws Exception {
        
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();
        context.setContextPath("/oculus");
        context.setWar("src/main/webapp");
        context.setExtraClasspath("src/main/webapp");
        context.setParentLoaderPriority(true);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context});
        server.setHandler(context);
        server.start();

    }
}
