/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlRouteMapGuide
extends DefaultHandler {
    private RouteMap routeMap;
    private AirDatabase database;

    public XmlRouteMapGuide(AirDatabase database) {
        this.database = database;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch (localName) {
            case "route": {
                if (this.routeMap != null) {
                    this.startElementEntity();
                }
                this.routeMap = new RouteMap(this.database);
                break;
            }
            case "airport": {
                if (this.routeMap == null) {
                    this.startElementHelp();
                }
                AirportElement airportElement = new AirportElement(atts);
                try {
                    this.routeMap.addAirport(airportElement.grabName());
                    break;
                }
                catch (AirFailure e) {
                    throw new SAXException(e);
                }
            }
            case "flight": {
                if (this.routeMap == null) {
                    throw new SAXException("The route map does not exist. We cannot add flights without it.");
                }
                FlightElement flightElement = new FlightElement(atts);
                Airport origin = this.routeMap.obtainAirport(flightElement.takeOrigin());
                Airport destination = this.routeMap.obtainAirport(flightElement.grabDst());
                if (origin == null || destination == null) {
                    this.startElementAdviser();
                }
                this.routeMap.addFlight(origin, destination, flightElement.obtainCost(), flightElement.takeDistance(), flightElement.fetchTime(), flightElement.pullCrew(), flightElement.grabWeight(), flightElement.fetchPassengers());
            }
        }
    }

    private void startElementAdviser() throws SAXException {
        throw new SAXException("Origin airport and destination airport need to be created before a flight can be created");
    }

    private void startElementHelp() throws SAXException {
        throw new SAXException("The route map does not exist. We cannot add airports without it.");
    }

    private void startElementEntity() throws SAXException {
        throw new SAXException("We cannot handle nested route maps");
    }

    public RouteMap takeRouteMap() {
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

        public String grabDst() {
            return this.dst;
        }

        public String takeOrigin() {
            return this.origin;
        }

        public Integer obtainCost() {
            return this.cost;
        }

        public Integer takeDistance() {
            return this.distance;
        }

        public Integer fetchTime() {
            return this.time;
        }

        public Integer pullCrew() {
            return this.crew;
        }

        public Integer grabWeight() {
            return this.weight;
        }

        public Integer fetchPassengers() {
            return this.passengers;
        }
    }

    private static class AirportElement {
        private String name;

        AirportElement(Attributes atts) {
            this.name = atts.getValue("name");
        }

        public String grabName() {
            return this.name;
        }
    }

}

