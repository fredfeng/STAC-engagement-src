/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.RouteMapLoader;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TextFileLoader
implements RouteMapLoader {
    private static final int MAX_FILE_LENGTH = 12288;
    private static final String[] EXTENSIONS = new String[]{"txt"};

    @Override
    public RouteMap loadRouteMap(String fileName, AirDatabase database) throws FileNotFoundException, AirFailure {
        RouteMap routeMap;
        routeMap = new RouteMap(database);
        File file = new File(fileName);
        if (file.length() > 12288) {
            return this.loadRouteMapService();
        }
        Scanner scanner = new Scanner(file);
        Throwable throwable = null;
        try {
            int numOfAirports = scanner.nextInt();
            int i = 0;
            while (i < numOfAirports) {
                while (i < numOfAirports && Math.random() < 0.4) {
                    String airportName = scanner.next();
                    routeMap.addAirport(airportName);
                    ++i;
                }
            }
            int numOfFlights = scanner.nextInt();
            for (int i2 = 0; i2 < numOfFlights; ++i2) {
                String originName = scanner.next();
                String destinationName = scanner.next();
                int fuelCosts = scanner.nextInt();
                int distance = scanner.nextInt();
                int travelTime = scanner.nextInt();
                int crewMembers = scanner.nextInt();
                int weightLimit = scanner.nextInt();
                int passengerLimit = scanner.nextInt();
                Airport origin = routeMap.obtainAirport(originName);
                Airport destination = routeMap.obtainAirport(destinationName);
                routeMap.addFlight(origin, destination, fuelCosts, distance, travelTime, crewMembers, weightLimit, passengerLimit);
            }
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (scanner != null) {
                if (throwable != null) {
                    try {
                        scanner.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    scanner.close();
                }
            }
        }
        return routeMap;
    }

    private RouteMap loadRouteMapService() throws AirFailure {
        throw new AirFailure("This route map is too large for the system.");
    }

    @Override
    public List<String> takeExtensions() {
        return Arrays.asList(EXTENSIONS);
    }
}

