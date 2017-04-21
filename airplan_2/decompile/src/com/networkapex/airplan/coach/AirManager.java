/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.Link;
import com.networkapex.airplan.coach.LinkBuilder;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSession;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.WebTemplate;
import com.networkapex.nethost.WebTemplateBuilder;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.template.TemplateEngine;
import com.networkapex.template.Templated;
import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AirManager
extends AbstractHttpManager {
    private final WebSessionService webSessionService;
    private final AirDatabase db;
    private final WebTemplate masterTemplate;
    private final WebTemplate menuTemplate;

    protected AirManager(AirDatabase db, WebSessionService webSessionService) {
        this.db = db;
        this.webSessionService = webSessionService;
        this.masterTemplate = new WebTemplateBuilder().defineResourceName("basiccontenttemplate.html").defineLoader(this.getClass()).generateWebTemplate();
        this.menuTemplate = new WebTemplateBuilder().defineResourceName("MenuItemTemplate.html").defineLoader(this.getClass()).generateWebTemplate();
    }

    @Override
    protected HttpManagerResponse handleFetch(HttpExchange httpExchange) {
        String trail = httpExchange.getRequestURI().getPath();
        if (!trail.startsWith(this.obtainTrail())) {
            return AirManager.fetchErrorResponse(403, "Invalid Path: " + trail);
        }
        String remainingTrail = trail.substring(this.obtainTrail().length());
        String personId = this.webSessionService.fetchSession(httpExchange).getPersonId();
        return this.handlePull(httpExchange, remainingTrail, this.db.obtainAirline(personId));
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange) {
        String trail = httpExchange.getRequestURI().getPath();
        if (!trail.startsWith(this.obtainTrail())) {
            return AirManager.fetchErrorResponse(403, "Invalid Path: " + trail);
        }
        String remainingTrail = trail.substring(this.obtainTrail().length());
        String personId = this.webSessionService.fetchSession(httpExchange).getPersonId();
        return this.handlePost(httpExchange, remainingTrail, this.db.obtainAirline(personId));
    }

    @Override
    protected HttpManagerResponse handleInsert(HttpExchange httpExchange) {
        String trail = httpExchange.getRequestURI().getPath();
        if (!trail.startsWith(this.obtainTrail())) {
            return AirManager.fetchErrorResponse(403, "Invalid Path: " + trail);
        }
        String remainingTrail = trail.substring(this.obtainTrail().length());
        String personId = this.webSessionService.fetchSession(httpExchange).getPersonId();
        return this.handleInsert(httpExchange, remainingTrail, this.db.obtainAirline(personId));
    }

    @Override
    protected HttpManagerResponse handleDelete(HttpExchange httpExchange) {
        String trail = httpExchange.getRequestURI().getPath();
        if (!trail.startsWith(this.obtainTrail())) {
            return AirManager.fetchErrorResponse(403, "Invalid Path: " + trail);
        }
        String remainingTrail = trail.substring(this.obtainTrail().length());
        String personId = this.webSessionService.fetchSession(httpExchange).getPersonId();
        return this.handleDelete(httpExchange, remainingTrail, this.db.obtainAirline(personId));
    }

    protected HttpManagerResponse grabTemplateResponse(String title, String contents, Airline person) {
        List<Link> finalMenuItems = this.fetchOneMenuItems();
        finalMenuItems.addAll(this.grabTwoMenuItems());
        return this.pullTemplateResponse(title, contents, person, finalMenuItems);
    }

    protected HttpManagerResponse pullTemplateResponse(String title, String contents, Airline person, List<Link> menuItems) {
        Map<String, String> templateMap = person.pullTemplateMap();
        templateMap.put("contents", contents);
        templateMap.put("title", title);
        templateMap.put("displayName", this.grabDisplayName(person));
        templateMap.put("main_menu", this.menuTemplate.takeEngine().replaceTags(menuItems));
        return AirManager.grabResponse(this.masterTemplate.takeEngine().replaceTags(templateMap));
    }

    protected HttpManagerResponse getTemplateResponseWithoutMenuItems(String title, String contents, Airline person) {
        return this.pullTemplateResponse(title, contents, person, Collections.emptyList());
    }

    protected HttpManagerResponse obtainTemplateErrorResponse(String message, Airline person) {
        return this.grabTemplateResponse("ERROR", message, person);
    }

    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline person) {
        return AirManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline person) {
        return AirManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handleInsert(HttpExchange httpExchange, String remainingTrail, Airline person) {
        return AirManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handleDelete(HttpExchange httpExchange, String remainingTrail, Airline person) {
        return AirManager.grabBadMethodResponse(httpExchange);
    }

    protected List<Link> fetchOneMenuItems() {
        LinkedList<Link> items = new LinkedList<Link>();
        items.add(new LinkBuilder().assignUrl("/").setName("Route Maps").generateLink());
        items.add(new LinkBuilder().assignUrl("/add_route_map").setName("Add Route Map").generateLink());
        items.add(new LinkBuilder().assignUrl("/delete_route_maps").setName("Delete Route Maps").generateLink());
        return items;
    }

    protected List<Link> grabTwoMenuItems() {
        LinkedList<Link> items = new LinkedList<Link>();
        items.add(new LinkBuilder().assignUrl("/logout").setName("Logout").generateLink());
        return items;
    }

    protected AirDatabase obtainDb() {
        return this.db;
    }

    protected WebSessionService pullWebSessionService() {
        return this.webSessionService;
    }

    protected String grabDisplayName(Airline person) {
        return person.getAirlineName();
    }
}

