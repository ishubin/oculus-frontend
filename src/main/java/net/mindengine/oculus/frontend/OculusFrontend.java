package net.mindengine.oculus.frontend;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.webapp.WebAppContext;

public class OculusFrontend {

    public static void main(String[] args) throws Exception {
        
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();
        context.setContextPath("/oculus");
        context.setResourceBase(OculusFrontend.class.getResource("/webapp").getFile());
        context.setParentLoaderPriority(true);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(context);
        server.start();

    }
}
