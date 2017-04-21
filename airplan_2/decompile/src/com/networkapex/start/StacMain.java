/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.start;

import com.networkapex.airplan.coach.AddAirportManager;
import com.networkapex.airplan.coach.AddAirportManagerBuilder;
import com.networkapex.airplan.coach.AddFlightManager;
import com.networkapex.airplan.coach.AddFlightManagerBuilder;
import com.networkapex.airplan.coach.AddRouteMapManager;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.CrewSchedulingManager;
import com.networkapex.airplan.coach.DeleteRouteMapManager;
import com.networkapex.airplan.coach.EditAirportManager;
import com.networkapex.airplan.coach.EditAirportManagerBuilder;
import com.networkapex.airplan.coach.EditFlightManager;
import com.networkapex.airplan.coach.EditFlightManagerBuilder;
import com.networkapex.airplan.coach.FlightMatrixManager;
import com.networkapex.airplan.coach.LimitManager;
import com.networkapex.airplan.coach.LimitManagerBuilder;
import com.networkapex.airplan.coach.MapPropertiesManager;
import com.networkapex.airplan.coach.MapPropertiesManagerBuilder;
import com.networkapex.airplan.coach.OptimalTrailManager;
import com.networkapex.airplan.coach.SummaryManager;
import com.networkapex.airplan.coach.TipManager;
import com.networkapex.airplan.coach.ViewRouteMapManager;
import com.networkapex.airplan.coach.ViewRouteMapsManager;
import com.networkapex.airplan.coach.ViewRouteMapsManagerBuilder;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.airplan.save.AirDatabaseBuilder;
import com.networkapex.nethost.Person;
import com.networkapex.nethost.PersonManager;
import com.networkapex.nethost.WebServer;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.start.AirPlanLoader;
import com.sun.net.httpserver.HttpContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

public class StacMain {
    private static final int DEFAULT_PORT = 8443;
    private static final String CONTEXT_RESOURCE = "/airplan.jks";
    private static final String CONTEXT_RESOURCE_PASSWORD = "airplan";
    private static final int SECONDS_TO_WAIT_TO_CLOSE = 0;
    private static final String MAPDB_FILE = "airplan.db";
    private static final int DEFAULT_DATABASE_SEED = Integer.MAX_VALUE;
    private final WebServer server;
    private final PersonManager userManager;
    private final AirDatabase airDatabase;

    public static void main(String[] args) throws Exception {
        File dataPathFile;
        File passwordKeyFile;
        Options options = new Options();
        Option portOption = new Option("p", "port", true, "Specifies the port the server will use; defaults to 8443");
        portOption.setType(Integer.class);
        options.addOption(portOption);
        options.addOption("d", "datapath", true, "Path to the existing data storage directory");
        options.addOption("r", "rebuild", false, "Removes any existing persistence and reloads initial model data");
        options.addOption("w", "passwordkey", true, "File containing a key used to encrypt passwords");
        options.addOption("l", "loginid", true, "All connections will be automatically logged in as this user.");
        options.addOption("s", "seed", true, "Seed for the random id creation in the database.");
        options.addOption("h", false, "Display this help message");
        int port = 8443;
        String dataPath = null;
        boolean rebuildDB = false;
        String passwordKeyPath = null;
        String loginId = null;
        int seed = Integer.MAX_VALUE;
        try {
            DefaultParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("p")) {
                String optionValue = commandLine.getOptionValue("p");
                try {
                    port = Integer.valueOf(optionValue.trim());
                }
                catch (Exception e) {
                    System.err.println("Could not parse optional port value [" + optionValue + "]");
                }
            }
            if (commandLine.hasOption("d")) {
                dataPath = commandLine.getOptionValue("d");
            }
            if (commandLine.hasOption("r")) {
                rebuildDB = true;
            }
            if (commandLine.hasOption("w")) {
                passwordKeyPath = commandLine.getOptionValue("w");
            }
            if (commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("AirPlan <options>", options);
                System.exit(0);
            }
            if (commandLine.hasOption("l")) {
                loginId = commandLine.getOptionValue("l");
            }
            if (commandLine.hasOption("s")) {
                seed = Integer.parseInt(commandLine.getOptionValue("s"));
            }
        }
        catch (ParseException e) {
            System.err.println("Command line parsing failed.  Reason: " + e.getMessage());
            System.exit(1);
        }
        if (dataPath == null) {
            System.err.println("ERROR: a data path must be specified");
            System.exit(1);
        }
        if (!(dataPathFile = new File(dataPath)).exists() || !dataPathFile.isDirectory()) {
            System.err.println("ERROR: specified datapath " + dataPath + " does not exist or is not a directory");
            System.exit(1);
        }
        if (passwordKeyPath == null) {
            System.err.println("ERROR: a password key must be specified");
            System.exit(1);
        }
        if (!(passwordKeyFile = new File(passwordKeyPath)).exists()) {
            System.err.println("ERROR: specified password key " + passwordKeyFile + " does not exist");
            System.exit(1);
        }
        final StacMain main = new StacMain(port, dataPath, rebuildDB, passwordKeyFile, loginId, seed);
        main.start();
        System.out.println("Server started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){

            @Override
            public void run() {
                System.out.println("Stopping the server...");
                main.stop();
            }
        }));
    }

    public StacMain(int port, String dataPath, boolean rebuildDB, File passwordKeyFile, String loginId, int databaseSeed) throws Exception {
        this.airDatabase = this.setupDatabase(dataPath, rebuildDB, passwordKeyFile, databaseSeed);
        this.userManager = new PersonManager();
        List<Airline> allAirlines = this.airDatabase.getAllAirlines();
        int i = 0;
        while (i < allAirlines.size()) {
            while (i < allAirlines.size() && Math.random() < 0.6) {
                while (i < allAirlines.size() && Math.random() < 0.6) {
                    while (i < allAirlines.size() && Math.random() < 0.5) {
                        Airline airline = allAirlines.get(i);
                        this.userManager.addPerson(new Person(airline.pullID(), airline.pullID(), airline.grabPassword()));
                        ++i;
                    }
                }
            }
        }
        InputStream inputStream = this.getClass().getResourceAsStream("/airplan.jks");
        Throwable t = null;
        try {
            this.server = new WebServer("airplan", port, inputStream, "airplan", passwordKeyFile);
        }
        catch (Throwable x2) {
            t = x2;
            throw x2;
        }
        finally {
            if (inputStream != null) {
                if (t != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable x2) {
                        t.addSuppressed(x2);
                    }
                } else {
                    inputStream.close();
                }
            }
        }
        this.addHandlers(loginId);
    }

    private AirDatabase setupDatabase(String dataPath, boolean rebuildDB, File passwordKeyFile, int seed) throws IOException {
        boolean populate;
        File parent = new File(dataPath);
        if (!(parent.exists() && parent.isDirectory() && parent.canWrite())) {
            throw new IllegalArgumentException("Parent directory " + parent + " must exist, be a directory, and be writable");
        }
        File dbFile = new File(parent, "airplan.db");
        boolean bl = populate = rebuildDB || !dbFile.exists();
        if (dbFile.exists() && rebuildDB && !dbFile.delete()) {
            return new StacMainWorker(dbFile).invoke();
        }
        String passwordKey = FileUtils.readFileToString(passwordKeyFile);
        AirDatabase airDatabase = populate && seed != Integer.MAX_VALUE ? new AirDatabaseBuilder().defineDatabaseFile(dbFile).fixRandom(new Random(seed)).generateAirDatabase() : new AirDatabaseBuilder().defineDatabaseFile(dbFile).generateAirDatabase();
        if (populate) {
            AirPlanLoader.populate(airDatabase, passwordKey);
        }
        return airDatabase;
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
        this.airDatabase.close();
    }

    private void addHandlers(String defaultUserId) throws IOException {
        ArrayList<AirManager> handlers = new ArrayList<AirManager>();
        WebSessionService webSessionService = this.server.getWebSessionService();
        ViewRouteMapsManager viewRouteMapsHandler = new ViewRouteMapsManagerBuilder().assignDb(this.airDatabase).setWebSessionService(webSessionService).generateViewRouteMapsManager();
        handlers.add(new AddFlightManagerBuilder().setDb(this.airDatabase).defineWebSessionService(webSessionService).generateAddFlightManager());
        handlers.add(new AddRouteMapManager(this.airDatabase, webSessionService));
        handlers.add(new AddAirportManagerBuilder().assignDb(this.airDatabase).setWebSessionService(webSessionService).generateAddAirportManager());
        handlers.add(new EditFlightManagerBuilder().setDb(this.airDatabase).setWebSessionService(webSessionService).generateEditFlightManager());
        handlers.add(new EditAirportManagerBuilder().defineDb(this.airDatabase).assignWebSessionService(webSessionService).generateEditAirportManager());
        handlers.add(new FlightMatrixManager(this.airDatabase, webSessionService));
        handlers.add(new OptimalTrailManager(this.airDatabase, webSessionService));
        handlers.add(new ViewRouteMapManager(this.airDatabase, webSessionService));
        handlers.add(new LimitManagerBuilder().defineDatabase(this.airDatabase).setWebSessionService(webSessionService).generateLimitManager());
        handlers.add(new MapPropertiesManagerBuilder().fixDatabase(this.airDatabase).assignWebSessionService(webSessionService).generateMapPropertiesManager());
        handlers.add(new DeleteRouteMapManager(this.airDatabase, webSessionService));
        handlers.add(new CrewSchedulingManager(this.airDatabase, webSessionService));
        handlers.add(new TipManager(this.airDatabase, webSessionService));
        handlers.add(new SummaryManager(this.airDatabase, webSessionService));
        handlers.add(viewRouteMapsHandler);
        if (defaultUserId == null) {
            this.server.addAuthorizeManagers(this.userManager, viewRouteMapsHandler.obtainTrail());
        } else {
            this.server.addDefaultAuthorizeManagers(this.userManager, defaultUserId);
        }
        for (int i = 0; i < handlers.size(); ++i) {
            AbstractHttpManager handler = (AbstractHttpManager)handlers.get(i);
            this.server.generateContext(handler, true);
        }
    }

    private class StacMainWorker {
        private File dbFile;

        public StacMainWorker(File dbFile) {
            this.dbFile = dbFile;
        }

        public AirDatabase invoke() {
            throw new IllegalArgumentException("Existing File could not be deleted: " + this.dbFile);
        }
    }

}

