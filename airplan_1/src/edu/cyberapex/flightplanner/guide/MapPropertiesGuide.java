/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.math.NumberUtils
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.ChartAgent;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.FlightWeightType;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.framework.RouteMapDensity;
import edu.cyberapex.flightplanner.framework.RouteMapSize;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.CellFormatter;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.guide.ParameterOverseer;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;

public class MapPropertiesGuide
extends AirGuide {
    protected static final String PATH = "/map_properties";
    private static final String TITLE = "Map Properties";
    private static final String WEIGHT_TYPE_FIELD = "weight-type";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngineBuilder().defineText("<h2>The following pages allow you to verify the correctness of the uploaded map.</h2><h2>{{routeMapName}}</h2><p>Select a weight type to be used in the properties calculation.</p><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\"><label for=\"weight-type\"> Select a Weight Type: </label><select name=\"weight-type\">{{weightTypeChoices}}</select> <br /><input type=\"submit\" value=\"Get the List of Properties\" name=\"submit\" id=\"submit\" /></form>").generateTemplateEngine();
    private static final TemplateEngine RESULTS_ENGINE = new TemplateEngineBuilder().defineText("<h4>This page allows you to verify the correctness of the uploaded map. You may delete this route map if it is not as intended.</h4><a href=\"{{deleteMapURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Delete the Map\" name=\"submit\"></a><a href=\"{{graphMatrix}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Next\" name=\"submit\"></a><h2>{{routeMapName}}'s Properties</h2><p>These properties are related to the \"{{lowerCaseWeightLabel}}\" weight type. </p><ul>   {{properties}}</ul>").generateTemplateEngine();
    private static final TemplateEngine PROPERTY_ENGINE = new TemplateEngineBuilder().defineText("<li>{{propertyLabel}}: {{propertyValue}} </li>").generateTemplateEngine();
    private CellFormatter formatter = new CellFormatter(10);
    private ParameterOverseer paramGuide = new ParameterOverseer();
    private int valueLength = 19;

    @Override
    public String getPath() {
        return "/map_properties";
    }

    public MapPropertiesGuide(AirDatabase database, WebSessionService webSessionService) {
        super(database, webSessionService);
        this.paramGuide.set("shou1d_adjust", false);
        this.paramGuide.set("adjustment_factor", 2);
    }

    private RouteMap takeRouteMapFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        String idStr;
        String[] pathParts = remainingPath.split("/");
        if (pathParts.length == 2 && NumberUtils.isNumber((String)(idStr = pathParts[1]))) {
            return airline.getRouteMap(Integer.parseInt(idStr));
        }
        return null;
    }

    private String getContentsForFetch(RouteMap routeMap) {
        HashMap<String, String> choicesDict = new HashMap<String, String>();
        choicesDict.put("routeMapName", routeMap.takeName());
        choicesDict.put("weightTypeChoices", GuideUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(choicesDict);
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            RouteMap routeMap = this.takeRouteMapFromPath(remainingPath, airline);
            if (routeMap == null) {
                return MapPropertiesGuide.getErrorResponse(400, "This route map does not exist.");
            }
            return this.pullTemplateResponseWithoutMenuItems("Map Properties", this.getContentsForFetch(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    private String fetchContentsForPost(RouteMap routeMap, FlightWeightType weightType) throws AirFailure {
        this.valueLength = 19;
        StringBuilder properties = new StringBuilder();
        ChartAgent chartAgent = new ChartAgent(routeMap, weightType);
        String connectedString = chartAgent.getConnected();
        properties.append(this.generateHTMLForProperty("Connected", this.format(connectedString)));
        String bipartiteString = chartAgent.takeBipartite();
        properties.append(this.generateHTMLForProperty("Bipartite", this.format(bipartiteString)));
        RouteMapSize size = chartAgent.describeSize();
        properties.append(this.generateHTMLForProperty("Size", this.format(size.fetchDescription())));
        RouteMapDensity density = chartAgent.describeDensity();
        properties.append(this.generateHTMLForProperty("Density", this.format(density.pullDescription())));
        String twoConnected = chartAgent.kConnected(2);
        properties.append(this.generateHTMLForProperty("Is 2-connected", this.format(twoConnected)));
        String threeConnected = chartAgent.kConnected(3);
        properties.append(this.generateHTMLForProperty("Is 3-connected", this.format(threeConnected)));
        String fourConnected = chartAgent.kConnected(4);
        properties.append(this.generateHTMLForProperty("Is 4-connected", this.format(fourConnected)));
        String fiveConnected = chartAgent.kConnected(5);
        properties.append(this.generateHTMLForProperty("Is 5-connected", this.format(fiveConnected)));
        String regular = chartAgent.fetchRegular();
        properties.append(this.generateHTMLForProperty("Regular", this.format(regular)));
        String eulerian = chartAgent.takeEulerian();
        properties.append(this.generateHTMLForProperty("Eulerian", this.format(eulerian)));
        HashMap<String, String> results = new HashMap<String, String>();
        String formattedRouteName = this.formatter.format(routeMap.takeName(), 10, CellFormatter.Justification.CENTER, false);
        results.put("routeMapName", formattedRouteName);
        String formattedWeightLabel = this.formatter.format(weightType.takeDescription().toLowerCase(), 20, CellFormatter.Justification.CENTER, false);
        results.put("lowerCaseWeightLabel", formattedWeightLabel);
        results.put("properties", properties.toString());
        results.put("graphMatrix", GuideUtils.generateRouteMapMatrixURL(routeMap));
        results.put("deleteMapURL", GuideUtils.generateDeleteMapURL());
        return RESULTS_ENGINE.replaceTags(results);
    }

    private String format(String value) {
        String formatted = value;
        formatted = value.length() < this.valueLength ? this.formatter.format(value, this.valueLength, CellFormatter.Justification.CENTER, (Boolean)this.paramGuide.pull("should_adjust")) : this.formatter.format(value, this.valueLength *= ((Integer)this.paramGuide.pull("adjustment_factor")).intValue(), CellFormatter.Justification.CENTER, (Boolean)this.paramGuide.pull("should_adjust"));
        return formatted;
    }

    private String generateHTMLForProperty(String propertyLabel, String propertyValue) {
        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.put("propertyLabel", propertyLabel);
        propertiesMap.put("propertyValue", propertyValue);
        return PROPERTY_ENGINE.replaceTags(propertiesMap);
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        RouteMap routeMap = this.takeRouteMapFromPath(remainingPath, airline);
        if (routeMap == null) {
            return MapPropertiesGuide.getErrorResponse(400, "This route map does not exist.");
        }
        String data = MultipartHelper.grabMultipartFieldContent(httpExchange, "weight-type");
        if (data == null || data.isEmpty()) {
            return MapPropertiesGuide.getErrorResponse(400, "A weight type was not selected.");
        }
        FlightWeightType weightType = FlightWeightType.fromString(data);
        if (weightType == null) {
            return MapPropertiesGuide.getErrorResponse(400, "The weight type selected is not a known weight type.");
        }
        try {
            HttpGuideResponse response = this.pullTemplateResponseWithoutMenuItems("Map Properties", this.fetchContentsForPost(routeMap, weightType), airline);
            return response;
        }
        catch (AirFailure e) {
            return MapPropertiesGuide.getErrorResponse(400, "Unable to create a list of properties for this route map.");
        }
    }

    @Override
    protected String takeDisplayName(Airline member) {
        return this.format(member.getAirlineName());
    }
}

