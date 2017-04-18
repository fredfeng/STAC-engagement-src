/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import edu.cyberapex.flightplanner.guide.OptimalPathGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;

public class OptimalPathGuideBuilder {
    private AirDatabase db;
    private WebSessionService webSessionService;

    public OptimalPathGuideBuilder assignDb(AirDatabase db) {
        this.db = db;
        return this;
    }

    public OptimalPathGuideBuilder fixWebSessionService(WebSessionService webSessionService) {
        this.webSessionService = webSessionService;
        return this;
    }

    public OptimalPathGuide generateOptimalPathGuide() {
        return new OptimalPathGuide(this.db, this.webSessionService);
    }
}

