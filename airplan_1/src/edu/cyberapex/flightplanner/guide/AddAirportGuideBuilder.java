/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import edu.cyberapex.flightplanner.guide.AddAirportGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;

public class AddAirportGuideBuilder {
    private AirDatabase db;
    private WebSessionService webSessionService;

    public AddAirportGuideBuilder fixDb(AirDatabase db) {
        this.db = db;
        return this;
    }

    public AddAirportGuideBuilder defineWebSessionService(WebSessionService webSessionService) {
        this.webSessionService = webSessionService;
        return this;
    }

    public AddAirportGuide generateAddAirportGuide() {
        return new AddAirportGuide(this.db, this.webSessionService);
    }
}

