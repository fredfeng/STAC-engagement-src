/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.guide.Link;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSession;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.Templated;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AirGuide
extends AbstractHttpGuide {
    private final WebSessionService webSessionService;
    private final AirDatabase db;
    private final WebTemplate masterTemplate;
    private final WebTemplate menuTemplate;

    protected AirGuide(AirDatabase db, WebSessionService webSessionService) {
        this.db = db;
        this.webSessionService = webSessionService;
        this.masterTemplate = new WebTemplate("basiccontenttemplate.html", this.getClass());
        this.menuTemplate = new WebTemplate("MenuItemTemplate.html", this.getClass());
    }

    @Override
    protected HttpGuideResponse handleGrab(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith(this.getPath())) {
            return AirGuide.getErrorResponse(403, "Invalid Path: " + path);
        }
        String remainingPath = path.substring(this.getPath().length());
        String memberId = this.webSessionService.fetchSession(httpExchange).grabMemberId();
        return this.handlePull(httpExchange, remainingPath, this.db.obtainAirline(memberId));
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith(this.getPath())) {
            return AirGuide.getErrorResponse(403, "Invalid Path: " + path);
        }
        String remainingPath = path.substring(this.getPath().length());
        String memberId = this.webSessionService.fetchSession(httpExchange).grabMemberId();
        return this.handlePost(httpExchange, remainingPath, this.db.obtainAirline(memberId));
    }

    @Override
    protected HttpGuideResponse handleInsert(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith(this.getPath())) {
            return AirGuide.getErrorResponse(403, "Invalid Path: " + path);
        }
        String remainingPath = path.substring(this.getPath().length());
        String memberId = this.webSessionService.fetchSession(httpExchange).grabMemberId();
        return this.handleInsert(httpExchange, remainingPath, this.db.obtainAirline(memberId));
    }

    @Override
    protected HttpGuideResponse handleDelete(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith(this.getPath())) {
            return AirGuide.getErrorResponse(403, "Invalid Path: " + path);
        }
        String remainingPath = path.substring(this.getPath().length());
        String memberId = this.webSessionService.fetchSession(httpExchange).grabMemberId();
        return this.handleDelete(httpExchange, remainingPath, this.db.obtainAirline(memberId));
    }

    protected HttpGuideResponse getTemplateResponse(String title, String contents, Airline member) {
        List<Link> finalMenuItems = this.getFirstMenuItems();
        finalMenuItems.addAll(this.obtainTwoMenuItems());
        return this.grabTemplateResponse(title, contents, member, finalMenuItems);
    }

    protected HttpGuideResponse grabTemplateResponse(String title, String contents, Airline member, List<Link> menuItems) {
        Map<String, String> templateMap = member.pullTemplateMap();
        templateMap.put("contents", contents);
        templateMap.put("title", title);
        templateMap.put("displayName", this.takeDisplayName(member));
        templateMap.put("main_menu", this.menuTemplate.getEngine().replaceTags(menuItems));
        return AirGuide.takeResponse(this.masterTemplate.getEngine().replaceTags(templateMap));
    }

    protected HttpGuideResponse pullTemplateResponseWithoutMenuItems(String title, String contents, Airline member) {
        return this.grabTemplateResponse(title, contents, member, Collections.emptyList());
    }

    protected HttpGuideResponse fetchTemplateErrorResponse(String message, Airline member) {
        return this.getTemplateResponse("ERROR", message, member);
    }

    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline member) {
        return AirGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline member) {
        return AirGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handleInsert(HttpExchange httpExchange, String remainingPath, Airline member) {
        return AirGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handleDelete(HttpExchange httpExchange, String remainingPath, Airline member) {
        return AirGuide.grabBadMethodResponse(httpExchange);
    }

    protected List<Link> getFirstMenuItems() {
        LinkedList<Link> items = new LinkedList<Link>();
        items.add(new Link("/", "Route Maps"));
        items.add(new Link("/add_route_map", "Add Route Map"));
        items.add(new Link("/delete_route_maps", "Delete Route Maps"));
        return items;
    }

    protected List<Link> obtainTwoMenuItems() {
        LinkedList<Link> items = new LinkedList<Link>();
        items.add(new Link("/logout", "Logout"));
        return items;
    }

    protected AirDatabase obtainDb() {
        return this.db;
    }

    protected WebSessionService fetchWebSessionService() {
        return this.webSessionService;
    }

    protected String takeDisplayName(Airline member) {
        return member.getAirlineName();
    }
}

