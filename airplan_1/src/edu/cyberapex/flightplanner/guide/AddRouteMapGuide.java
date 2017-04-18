/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.PartFileLoader;
import edu.cyberapex.flightplanner.TextFileLoader;
import edu.cyberapex.flightplanner.XmlFileLoader;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddRouteMapGuide
extends AirGuide {
    protected static final String PATH = "/add_route_map";
    protected static final String TITLE = "Add Route Map";
    private static final String FILE_FIELD_NAME = "file";
    private static final String ROUTE_MAP_FIELD_NAME = "route_map_name";
    private static final String CONTENT = "<form action=\"/add_route_map\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"file\"> Route Map: </label>    <input type=\"file\" id=\"file\" name=\"file\" autofocus required accept=\"*/*\"/>    <br/>    <label for=\"map\"> Name your route map: </label>    <input type=\"text\" id=\"map\" name=\"route_map_name\" placeholder=\"Route Map\" /> <br/>    <input type=\"submit\" value=\"Submit Route Map\" name=\"submit\" id=\"submit\" /></form>";
    private static final Set<String> ALL_FIELDS = new HashSet<String>();
    private static Path tempDirectory;
    private final PartFileLoader partFileLoader = new PartFileLoader();
    private final TextFileLoader textFileLoader = new TextFileLoader();
    private final XmlFileLoader xmlFileLoader = new XmlFileLoader();

    public AddRouteMapGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String getPath() {
        return "/add_route_map";
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline member) {
        if (!remainingPath.equals("") && !remainingPath.equals("/")) {
            return AddRouteMapGuide.getErrorResponse(404, "Page not found.");
        }
        return this.getTemplateResponse("Add Route Map", "<form action=\"/add_route_map\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"file\"> Route Map: </label>    <input type=\"file\" id=\"file\" name=\"file\" autofocus required accept=\"*/*\"/>    <br/>    <label for=\"map\"> Name your route map: </label>    <input type=\"text\" id=\"map\" name=\"route_map_name\" placeholder=\"Route Map\" /> <br/>    <input type=\"submit\" value=\"Submit Route Map\" name=\"submit\" id=\"submit\" /></form>", member);
    }

    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline)
    {
        if ((!remainingPath.equals("")) && (!remainingPath.equals("/"))) {
            return getErrorResponse(404, "Page not found.");
        }
        Path path;
        try
        {
            path = obtainTempDirectory();
        }
        catch (IOException e)
        {

            return getErrorResponse(400, e.getMessage());
        }
        String fileName = airline.obtainID() + new Date().getTime();

        Map<String, String> fieldNameInputs = MultipartHelper.grabMultipartFile(httpExchange, ALL_FIELDS, "file", path, fileName);

        String mime = (String)fieldNameInputs.get("MIME");
        String filePath = path.resolve(fileName).toString();
        String name = (String)fieldNameInputs.get("route_map_name");
        if ((mime == null) || (name == null)) {
            return getErrorResponse(400, "Invalid input");
        }
        if (name.trim().isEmpty()) {
            return fetchTemplateErrorResponse("The route map must have a name.", airline);
        }
        if (airline.takeRouteMap(name) != null) {
            return fetchTemplateErrorResponse("A route map with this name already exists.", airline);
        }
        AirDatabase database = obtainDb();
        RouteMap routeMap;
        try
        {
            switch (mime)
            {
                case "application/xml":
                case "text/xml":
                    routeMap = this.xmlFileLoader.loadRouteMap(filePath, database);
                    break;
                case "text/plain":
                    routeMap = this.textFileLoader.loadRouteMap(filePath, database);
                    break;
                case "application/json":
                case "application/octet-stream":
                    routeMap = this.partFileLoader.loadRouteMap(filePath, database);
                    break;
                default:
                    return getErrorResponse(415, "This file format is not supported.");
            }
        }
        catch (IOException e)
        {
            return getErrorResponse(400, e.getMessage());
        }
        catch (AirFailure e)
        {
            HttpGuideResponse localHttpGuideResponse1;
            return getErrorResponse(400, e.getMessage());
        }
        finally
        {
            new File(filePath).delete();
        }
        routeMap.fixName(name);
        airline.addRouteMap(routeMap);
        database.commit();

        return takeRedirectResponse(GuideUtils.generateMapPropertiesURL(routeMap));
    }

    private static Path obtainTempDirectory() throws IOException {
        if (tempDirectory == null) {
            tempDirectory = Files.createTempDirectory(null, new FileAttribute[0]);
        }
        return tempDirectory;
    }

    static {
        ALL_FIELDS.add("file");
        ALL_FIELDS.add("route_map_name");
        tempDirectory = null;
    }
}

