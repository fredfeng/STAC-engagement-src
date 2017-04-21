/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.GraphTranslator;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.CellFormatter;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.coach.ParameterManager;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.FlightWeightType;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.prototype.RouteMapDensity;
import com.networkapex.airplan.prototype.RouteMapSize;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;

public class MapPropertiesManager
extends AirManager {
    protected static final String TRAIL = "/map_properties";
    private static final String TITLE = "Map Properties";
    private static final String WEIGHT_TYPE_FIELD = "weight-type";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngine("<h2>The following pages allow you to verify the correctness of the uploaded map.</h2><h2>{{routeMapName}}</h2><p>Select a weight type to be used in the properties calculation.</p><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\"><label for=\"weight-type\"> Select a Weight Type: </label><select name=\"weight-type\">{{weightTypeChoices}}</select> <br /><input type=\"submit\" value=\"Get the List of Properties\" name=\"submit\" id=\"submit\" /></form>");
    private static final TemplateEngine RESULTS_ENGINE = new TemplateEngine("<h4>This page allows you to verify the correctness of the uploaded map. You may delete this route map if it is not as intended.</h4><a href=\"{{deleteMapURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Delete the Map\" name=\"submit\"></a><a href=\"{{graphMatrix}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Next\" name=\"submit\"></a><h2>{{routeMapName}}'s Properties</h2><p>These properties are related to the \"{{lowerCaseWeightLabel}}\" weight type. </p><ul>   {{properties}}</ul>");
    private static final TemplateEngine PROPERTY_ENGINE = new TemplateEngine("<li>{{propertyLabel}}: {{propertyValue}} </li>");
    private CellFormatter formatter = new CellFormatter(10);
    private ParameterManager paramManager = new ParameterManager();
    private int valueLength = 19;

    @Override
    public String obtainTrail() {
        return "/map_properties";
    }

    public MapPropertiesManager(AirDatabase database, WebSessionService webSessionService) {
        super(database, webSessionService);
        this.paramManager.set("shou1d_adjust", false);
        this.paramManager.set("adjustment_factor", 2);
    }

    private RouteMap grabRouteMapFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        String idStr;
        String[] trailParts = remainingTrail.split("/");
        if (trailParts.length == 2 && NumberUtils.isNumber(idStr = trailParts[1])) {
            return airline.getRouteMap(Integer.parseInt(idStr));
        }
        return null;
    }

    private String pullContentsForPull(RouteMap routeMap) {
        HashMap<String, String> choicesDict = new HashMap<String, String>();
        choicesDict.put("routeMapName", routeMap.takeName());
        choicesDict.put("weightTypeChoices", ManagerUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(choicesDict);
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            RouteMap routeMap = this.grabRouteMapFromTrail(remainingTrail, airline);
            if (routeMap == null) {
                return MapPropertiesManager.fetchErrorResponse(400, "This route map does not exist.");
            }
            return this.getTemplateResponseWithoutMenuItems("Map Properties", this.pullContentsForPull(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    private String grabContentsForPost(RouteMap routeMap, FlightWeightType weightType) throws AirRaiser {
        this.valueLength = 19;
        StringBuilder properties = new StringBuilder();
        GraphTranslator graphTranslator = new GraphTranslator(routeMap, weightType);
        String connectedString = graphTranslator.fetchConnected();
        properties.append(this.generateHTMLForProperty("Connected", this.format(connectedString)));
        String bipartiteString = graphTranslator.takeBipartite();
        properties.append(this.generateHTMLForProperty("Bipartite", this.format(bipartiteString)));
        RouteMapSize size = graphTranslator.describeSize();
        properties.append(this.generateHTMLForProperty("Size", this.format(size.obtainDescription())));
        RouteMapDensity density = graphTranslator.describeDensity();
        properties.append(this.generateHTMLForProperty("Density", this.format(density.obtainDescription())));
        String twoConnected = graphTranslator.kConnected(2);
        properties.append(this.generateHTMLForProperty("Is 2-connected", this.format(twoConnected)));
        String threeConnected = graphTranslator.kConnected(3);
        properties.append(this.generateHTMLForProperty("Is 3-connected", this.format(threeConnected)));
        String fourConnected = graphTranslator.kConnected(4);
        properties.append(this.generateHTMLForProperty("Is 4-connected", this.format(fourConnected)));
        String fiveConnected = graphTranslator.kConnected(5);
        properties.append(this.generateHTMLForProperty("Is 5-connected", this.format(fiveConnected)));
        String regular = graphTranslator.obtainRegular();
        properties.append(this.generateHTMLForProperty("Regular", this.format(regular)));
        String eulerian = graphTranslator.grabEulerian();
        properties.append(this.generateHTMLForProperty("Eulerian", this.format(eulerian)));
        HashMap<String, String> results = new HashMap<String, String>();
        String formattedRouteName = this.formatter.format(routeMap.takeName(), 10, CellFormatter.Justification.CENTER, false);
        results.put("routeMapName", formattedRouteName);
        String formattedWeightLabel = this.formatter.format(weightType.getDescription().toLowerCase(), 20, CellFormatter.Justification.CENTER, false);
        results.put("lowerCaseWeightLabel", formattedWeightLabel);
        results.put("properties", properties.toString());
        results.put("graphMatrix", ManagerUtils.generateRouteMapMatrixURL(routeMap));
        results.put("deleteMapURL", ManagerUtils.generateDeleteMapURL());
        return RESULTS_ENGINE.replaceTags(results);
    }

    private String format(String value) {
        String formatted = value;
        formatted = value.length() < this.valueLength ? this.formatter.format(value, this.valueLength, CellFormatter.Justification.CENTER, (Boolean)this.paramManager.take("should_adjust")) : this.formatter.format(value, this.valueLength *= ((Integer)this.paramManager.take("adjustment_factor")).intValue(), CellFormatter.Justification.CENTER, (Boolean)this.paramManager.take("should_adjust"));
        return formatted;
    }

    private String generateHTMLForProperty(String propertyLabel, String propertyValue) {
        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.put("propertyLabel", propertyLabel);
        propertiesMap.put("propertyValue", propertyValue);
        return PROPERTY_ENGINE.replaceTags(propertiesMap);
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        RouteMap routeMap = this.grabRouteMapFromTrail(remainingTrail, airline);
        if (routeMap == null) {
            return MapPropertiesManager.fetchErrorResponse(400, "This route map does not exist.");
        }
        String data = MultipartHelper.grabMultipartFieldContent(httpExchange, "weight-type");
        if (data == null || data.isEmpty()) {
            return MapPropertiesManager.fetchErrorResponse(400, "A weight type was not selected.");
        }
        FlightWeightType weightType = FlightWeightType.fromString(data);
        if (weightType == null) {
            return MapPropertiesManager.fetchErrorResponse(400, "The weight type selected is not a known weight type.");
        }
        try {
            HttpManagerResponse response = this.getTemplateResponseWithoutMenuItems("Map Properties", this.grabContentsForPost(routeMap, weightType), airline);
            return response;
        }
        catch (AirRaiser e) {
            return MapPropertiesManager.fetchErrorResponse(400, "Unable to create a list of properties for this route map.");
        }
    }

    @Override
    protected String grabDisplayName(Airline person) {
        return this.format(person.getAirlineName());
    }
}

