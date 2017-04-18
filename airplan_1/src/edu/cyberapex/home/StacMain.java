/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.cli.CommandLine
 *  org.apache.commons.cli.DefaultParser
 *  org.apache.commons.cli.HelpFormatter
 *  org.apache.commons.cli.Option
 *  org.apache.commons.cli.Options
 *  org.apache.commons.cli.ParseException
 *  org.apache.commons.io.FileUtils
 */
package edu.cyberapex.home;

import com.sun.net.httpserver.HttpContext;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.guide.AddAirportGuide;
import edu.cyberapex.flightplanner.guide.AddAirportGuideBuilder;
import edu.cyberapex.flightplanner.guide.AddFlightGuide;
import edu.cyberapex.flightplanner.guide.AddRouteMapGuide;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.CrewSchedulingGuide;
import edu.cyberapex.flightplanner.guide.CrewSchedulingGuideBuilder;
import edu.cyberapex.flightplanner.guide.DeleteRouteMapGuide;
import edu.cyberapex.flightplanner.guide.EditAirportGuide;
import edu.cyberapex.flightplanner.guide.EditFlightGuide;
import edu.cyberapex.flightplanner.guide.FlightMatrixGuide;
import edu.cyberapex.flightplanner.guide.LimitGuide;
import edu.cyberapex.flightplanner.guide.MapPropertiesGuide;
import edu.cyberapex.flightplanner.guide.MapPropertiesGuideBuilder;
import edu.cyberapex.flightplanner.guide.OptimalPathGuide;
import edu.cyberapex.flightplanner.guide.OptimalPathGuideBuilder;
import edu.cyberapex.flightplanner.guide.SummaryGuide;
import edu.cyberapex.flightplanner.guide.TipGuide;
import edu.cyberapex.flightplanner.guide.ViewRouteMapGuide;
import edu.cyberapex.flightplanner.guide.ViewRouteMapGuideBuilder;
import edu.cyberapex.flightplanner.guide.ViewRouteMapsGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.home.AirPlanLoader;
import edu.cyberapex.server.Member;
import edu.cyberapex.server.MemberBuilder;
import edu.cyberapex.server.MemberFailure;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebServer;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.AbstractHttpGuide;
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
    private final MemberOverseer userManager;
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
            StacMain.mainEngine(passwordKeyFile);
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

    private static void mainEngine(File passwordKeyFile) {
        System.err.println("ERROR: specified password key " + passwordKeyFile + " does not exist");
        System.exit(1);
    }

    public StacMain(int port, String dataPath, boolean rebuildDB, File passwordKeyFile, String loginId, int databaseSeed) throws Exception {
        this.airDatabase = this.setupDatabase(dataPath, rebuildDB, passwordKeyFile, databaseSeed);
        this.userManager = new MemberOverseer();
        List<Airline> allAirlines = this.airDatabase.obtainAllAirlines();
        for (int i = 0; i < allAirlines.size(); ++i) {
            new StacMainWorker(allAirlines, i).invoke();
        }
        InputStream inputStream = this.getClass().getResourceAsStream("/airplan.jks");
        Throwable throwable = null;
        try {
            this.server = new WebServer("airplan", port, inputStream, "airplan", passwordKeyFile);
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (inputStream != null) {
                if (throwable != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    inputStream.close();
                }
            }
        }
        this.addHandlers(loginId);
    }

    private AirDatabase setupDatabase(String dataPath, boolean rebuildDB, File passwordKeyFile, int seed) throws IOException {
        File parent = new File(dataPath);
        if (!(parent.exists() && parent.isDirectory() && parent.canWrite())) {
            throw new IllegalArgumentException("Parent directory " + parent + " must exist, be a directory, and be writable");
        }
        File dbFile = new File(parent, "airplan.db");
        boolean populate = rebuildDB || !dbFile.exists();
        if (dbFile.exists() && rebuildDB) {
            this.setupDatabaseService(dbFile);
        }
        String passwordKey = FileUtils.readFileToString((File)passwordKeyFile);
        AirDatabase airDatabase = populate && seed != Integer.MAX_VALUE ? new AirDatabase(dbFile, new Random(seed)) : new AirDatabase(dbFile);
        if (populate) {
            AirPlanLoader.populate(airDatabase, passwordKey);
        }
        return airDatabase;
    }

    private void setupDatabaseService(File dbFile) {
        if (!dbFile.delete()) {
            throw new IllegalArgumentException("Existing File could not be deleted: " + dbFile);
        }
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
        this.airDatabase.close();
    }

    private void addHandlers(String defaultUserId) throws IOException {
        ArrayList<AirGuide> handlers = new ArrayList<AirGuide>();
        WebSessionService webSessionService = this.server.getWebSessionService();
        ViewRouteMapsGuide viewRouteMapsHandler = new ViewRouteMapsGuide(this.airDatabase, webSessionService);
        handlers.add(new AddFlightGuide(this.airDatabase, webSessionService));
        handlers.add(new AddRouteMapGuide(this.airDatabase, webSessionService));
        handlers.add(new AddAirportGuideBuilder().fixDb(this.airDatabase).defineWebSessionService(webSessionService).generateAddAirportGuide());
        handlers.add(new EditFlightGuide(this.airDatabase, webSessionService));
        handlers.add(new EditAirportGuide(this.airDatabase, webSessionService));
        handlers.add(new FlightMatrixGuide(this.airDatabase, webSessionService));
        handlers.add(new OptimalPathGuideBuilder().assignDb(this.airDatabase).fixWebSessionService(webSessionService).generateOptimalPathGuide());
        handlers.add(new ViewRouteMapGuideBuilder().setDb(this.airDatabase).defineWebSessionService(webSessionService).generateViewRouteMapGuide());
        handlers.add(new LimitGuide(this.airDatabase, webSessionService));
        handlers.add(new MapPropertiesGuideBuilder().fixDatabase(this.airDatabase).fixWebSessionService(webSessionService).generateMapPropertiesGuide());
        handlers.add(new DeleteRouteMapGuide(this.airDatabase, webSessionService));
        handlers.add(new CrewSchedulingGuideBuilder().fixAirDatabase(this.airDatabase).defineSessionService(webSessionService).generateCrewSchedulingGuide());
        handlers.add(new TipGuide(this.airDatabase, webSessionService));
        handlers.add(new SummaryGuide(this.airDatabase, webSessionService));
        handlers.add(viewRouteMapsHandler);
        if (defaultUserId == null) {
            this.server.addAuthenticateGuides(this.userManager, viewRouteMapsHandler.getPath());
        } else {
            new StacMainSupervisor(defaultUserId).invoke();
        }
        for (int i = 0; i < handlers.size(); ++i) {
            AbstractHttpGuide handler = (AbstractHttpGuide)handlers.get(i);
            this.server.generateContext(handler, true);
        }
    }

    private class StacMainSupervisor {
        private String defaultUserId;

        public StacMainSupervisor(String defaultUserId) {
            this.defaultUserId = defaultUserId;
        }

        public void invoke() {
            StacMain.this.server.addDefaultAuthenticateGuides(StacMain.this.userManager, this.defaultUserId);
        }
    }

    private class StacMainWorker {
        private List<Airline> allAirlines;
        private int i;

        public StacMainWorker(List<Airline> allAirlines, int i) {
            this.allAirlines = allAirlines;
            this.i = i;
        }

        public void invoke() throws MemberFailure {
            Airline airline = this.allAirlines.get(this.i);
            StacMain.this.userManager.addMember(new MemberBuilder().defineIdentity(airline.obtainID()).fixUsername(airline.obtainID()).fixPassword(airline.grabPassword()).generateMember());
        }
    }

}

