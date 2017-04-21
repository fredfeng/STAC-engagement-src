/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlRouteMapManager
extends DefaultHandler {
    private RouteMap routeMap;
    private AirDatabase database;

    public XmlRouteMapManager(AirDatabase database) {
        this.database = database;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch (localName) {
            case "route": {
                if (this.routeMap != null) {
                    this.startElementHome();
                }
                this.routeMap = new RouteMap(this.database);
                break;
            }
            case "airport": {
                if (this.routeMap == null) {
                    throw new SAXException("The route map does not exist. We cannot add airports without it.");
                }
                AirportElement airportElement = new AirportElement(atts);
                try {
                    this.routeMap.addAirport(airportElement.fetchName());
                    break;
                }
                catch (AirRaiser e) {
                    throw new SAXException(e);
                }
            }
            case "flight": {
                if (this.routeMap == null) {
                    throw new SAXException("The route map does not exist. We cannot add flights without it.");
                }
                FlightElement flightElement = new FlightElement(atts);
                Airport origin = this.routeMap.fetchAirport(flightElement.getOrigin());
                Airport destination = this.routeMap.fetchAirport(flightElement.obtainDst());
                if (origin == null || destination == null) {
                    this.startElementHerder();
                }
                this.routeMap.addFlight(origin, destination, flightElement.obtainCost(), flightElement.takeDistance(), flightElement.takeTime(), flightElement.obtainCrew(), flightElement.pullWeight(), flightElement.pullPassengers());
            }
        }
    }

    private void startElementHerder() throws SAXException {
        throw new SAXException("Origin airport and destination airport need to be created before a flight can be created");
    }

    private void startElementHome() throws SAXException {
        throw new SAXException("We cannot handle nested route maps");
    }

    public RouteMap getRouteMap() {
        return this.routeMap;
    }

    private static class FlightElement {
        private String origin;
        private String dst;
        private Integer cost;
        private Integer distance;
        private Integer time;
        private Integer crew;
        private Integer weight;
        private Integer passengers;

        FlightElement(Attributes atts) {
            this.origin = atts.getValue("origin");
            this.dst = atts.getValue("dst");
            this.cost = Integer.parseInt(atts.getValue("cost"));
            this.distance = Integer.parseInt(atts.getValue("distance"));
            this.time = Integer.parseInt(atts.getValue("time"));
            this.crew = Integer.parseInt(atts.getValue("crew"));
            this.weight = Integer.parseInt(atts.getValue("weight"));
            this.passengers = Integer.parseInt(atts.getValue("passengers"));
        }

        public String obtainDst() {
            return this.dst;
        }

        public String getOrigin() {
            return this.origin;
        }

        public Integer obtainCost() {
            return this.cost;
        }

        public Integer takeDistance() {
            return this.distance;
        }

        public Integer takeTime() {
            return this.time;
        }

        public Integer obtainCrew() {
            return this.crew;
        }

        public Integer pullWeight() {
            return this.weight;
        }

        public Integer pullPassengers() {
            return this.passengers;
        }
    }

    private static class AirportElement {
        private String name;

        AirportElement(Attributes atts) {
            this.name = atts.getValue("name");
        }

        public String fetchName() {
            return this.name;
        }
    }

}

