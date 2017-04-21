/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.RouteMapLoader;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
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
    public RouteMap loadRouteMap(String fileName, AirDatabase database) throws FileNotFoundException, AirRaiser {
        RouteMap routeMap;
        routeMap = new RouteMap(database);
        File file = new File(fileName);
        if (file.length() > 12288) {
            return this.loadRouteMapEngine();
        }
        Scanner scanner = new Scanner(file);
        Throwable throwable = null;
        try {
            int numOfAirports = scanner.nextInt();
            for (int k = 0; k < numOfAirports; ++k) {
                this.loadRouteMapSupervisor(routeMap, scanner);
            }
            int numOfFlights = scanner.nextInt();
            for (int j = 0; j < numOfFlights; ++j) {
                this.loadRouteMapAssist(routeMap, scanner);
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

    private void loadRouteMapAssist(RouteMap routeMap, Scanner scanner) {
        String originName = scanner.next();
        String destinationName = scanner.next();
        int fuelCosts = scanner.nextInt();
        int distance = scanner.nextInt();
        int travelTime = scanner.nextInt();
        int crewMembers = scanner.nextInt();
        int weightLimit = scanner.nextInt();
        int passengerLimit = scanner.nextInt();
        Airport origin = routeMap.fetchAirport(originName);
        Airport destination = routeMap.fetchAirport(destinationName);
        routeMap.addFlight(origin, destination, fuelCosts, distance, travelTime, crewMembers, weightLimit, passengerLimit);
    }

    private void loadRouteMapSupervisor(RouteMap routeMap, Scanner scanner) throws AirRaiser {
        String airportName = scanner.next();
        routeMap.addAirport(airportName);
    }

    private RouteMap loadRouteMapEngine() throws AirRaiser {
        throw new AirRaiser("This route map is too large for the system.");
    }

    @Override
    public List<String> takeExtensions() {
        return Arrays.asList(EXTENSIONS);
    }
}

