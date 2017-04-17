/*
 * Decompiled with CFR 0_121.
 */
package smartmail.messaging.controller.module;

import javax.servlet.Servlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import smartmail.messaging.controller.module.EmailSenderReceiver;

public class RunServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8988);
        ServletContextHandler context = new ServletContextHandler(1);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new EmailSenderReceiver()), "/*");
        server.start();
        server.join();
    }
}

