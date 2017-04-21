/*
 * Decompiled with CFR 0_121.
 */
package infotrader.userinteraction;

import infotrader.userinteraction.DocumentParser;
import java.io.PrintStream;
import javax.servlet.Servlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SitemapServlet {
    public static void mainx(String[] args) throws Exception {
        Server server = new Server(8988);
        ServletContextHandler context = new ServletContextHandler(1);
        context.setContextPath("/");
        server.setHandler(context);
        System.out.println("Initializing server, please wait...");
        System.out.println("Indexing all files, could take a minute.");
        System.out.println("Initializing server, please wait...");
        context.addServlet(new ServletHolder(new DocumentParser()), "/*");
        System.out.println("Server coming up.");
        server.start();
        server.join();
    }
}

