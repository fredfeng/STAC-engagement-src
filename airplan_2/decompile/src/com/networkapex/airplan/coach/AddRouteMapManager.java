/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.ParserFileLoader;
import com.networkapex.airplan.TextFileLoader;
import com.networkapex.airplan.XmlFileLoader;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddRouteMapManager
extends AirManager {
    protected static final String TRAIL = "/add_route_map";
    protected static final String TITLE = "Add Route Map";
    private static final String FILE_FIELD_NAME = "file";
    private static final String ROUTE_MAP_FIELD_NAME = "route_map_name";
    private static final String CONTENT = "<form action=\"/add_route_map\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"file\"> Route Map: </label>    <input type=\"file\" id=\"file\" name=\"file\" autofocus required accept=\"*/*\"/>    <br/>    <label for=\"map\"> Name your route map: </label>    <input type=\"text\" id=\"map\" name=\"route_map_name\" placeholder=\"Route Map\" /> <br/>    <input type=\"submit\" value=\"Submit Route Map\" name=\"submit\" id=\"submit\" /></form>";
    private static final Set<String> ALL_FIELDS = new HashSet<String>();
    private static Path tempDirectory;
    private final ParserFileLoader parserFileLoader = new ParserFileLoader();
    private final TextFileLoader textFileLoader = new TextFileLoader();
    private final XmlFileLoader xmlFileLoader = new XmlFileLoader();

    public AddRouteMapManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/add_route_map";
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline person) {
        if (!remainingTrail.equals("") && !remainingTrail.equals("/")) {
            return AddRouteMapManager.fetchErrorResponse(404, "Page not found.");
        }
        return this.grabTemplateResponse("Add Route Map", "<form action=\"/add_route_map\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"file\"> Route Map: </label>    <input type=\"file\" id=\"file\" name=\"file\" autofocus required accept=\"*/*\"/>    <br/>    <label for=\"map\"> Name your route map: </label>    <input type=\"text\" id=\"map\" name=\"route_map_name\" placeholder=\"Route Map\" /> <br/>    <input type=\"submit\" value=\"Submit Route Map\" name=\"submit\" id=\"submit\" /></form>", person);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline)
    {
        if ((!remainingTrail.equals("")) && (!remainingTrail.equals("/"))) {
            return fetchErrorResponse(404, "Page not found.");
        }
        Path trail;
        try
        {
            trail = obtainTempDirectory();
        }
        catch (IOException e)
        {
            return fetchErrorResponse(400, e.getMessage());
        }

        String fileName = airline.pullID() + new Date().getTime();

        Map<String, String> fieldNameInputs = MultipartHelper.takeMultipartFile(httpExchange, ALL_FIELDS, "file", trail, fileName);

        String mime = (String)fieldNameInputs.get("MIME");
        String fileTrail = trail.resolve(fileName).toString();
        String name = (String)fieldNameInputs.get("route_map_name");
        if ((mime == null) || (name == null)) {
            return fetchErrorResponse(400, "Invalid input");
        }
        if (name.trim().isEmpty()) {
            return obtainTemplateErrorResponse("The route map must have a name.", airline);
        }
        if (airline.grabRouteMap(name) != null) {
            return obtainTemplateErrorResponse("A route map with this name already exists.", airline);
        }
        AirDatabase database = obtainDb();
        RouteMap routeMap;
        try
        {
            switch (mime)
            {
                case "application/xml":
                case "text/xml":
                    routeMap = this.xmlFileLoader.loadRouteMap(fileTrail, database);
                    break;
                case "text/plain":
                    routeMap = this.textFileLoader.loadRouteMap(fileTrail, database);
                    break;
                case "application/json":
                case "application/octet-stream":
                    routeMap = this.parserFileLoader.loadRouteMap(fileTrail, database);
                    break;
                default:
                    return fetchErrorResponse(415, "This file format is not supported.");
            }
        }
        catch (IOException e)
        {
            return fetchErrorResponse(400, e.getMessage());
        }
        catch (AirRaiser e)
        {
            HttpManagerResponse localHttpManagerResponse1;
            return fetchErrorResponse(400, e.getMessage());
        }
        finally
        {
            new File(fileTrail).delete();
        }
        routeMap.defineName(name);
        airline.addRouteMap(routeMap);
        database.commit();
        System.out.println("Routemap name = " + name + ", ID = " + routeMap.grabId());

        return obtainRedirectResponse(ManagerUtils.generateMapPropertiesURL(routeMap));
    }

    private static Path obtainTempDirectory() throws IOException {
        if (tempDirectory == null) {
            AddRouteMapManager.grabTempDirectoryFunction();
        }
        return tempDirectory;
    }

    private static void grabTempDirectoryFunction() throws IOException {
        tempDirectory = Files.createTempDirectory(null, new FileAttribute[0]);
    }

    static {
        ALL_FIELDS.add("file");
        ALL_FIELDS.add("route_map_name");
        tempDirectory = null;
    }
}

