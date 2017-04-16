/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import com.graphhopper.GraphHopper;
import com.graphhopper.http.RouteSerializer;
import com.graphhopper.http.SimpleRouteSerializer;
import com.graphhopper.http.TourSerializer;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.tour.Matrix;
import com.graphhopper.tour.TourCalculator;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.TranslationMap;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPlace;
import java.lang.annotation.Annotation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultModule
extends AbstractModule {
    private final Logger logger;
    protected final CmdArgs args;
    private GraphHopper graphHopper;

    public DefaultModule(CmdArgs args) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.args = CmdArgs.readFromConfigAndMerge(args, "config", "graphhopper.config");
    }

    public GraphHopper getGraphHopper() {
        if (this.graphHopper == null) {
            throw new IllegalStateException("createGraphHopper not called");
        }
        return this.graphHopper;
    }

    protected GraphHopper createGraphHopper(CmdArgs args) {
        GraphHopper tmp = new GraphHopper().forServer().init(args);
        tmp.importOrLoad();
        this.logger.info("loaded graph at:" + tmp.getGraphHopperLocation() + ", source:" + tmp.getOSMFile() + ", flagEncoders:" + tmp.getEncodingManager() + ", class:" + tmp.getGraphHopperStorage().toDetailsString());
        return tmp;
    }

    @Override
    protected void configure() {
        try {
            this.graphHopper = this.createGraphHopper(this.args);
            this.bind(GraphHopper.class).toInstance(this.graphHopper);
            this.bind(TranslationMap.class).toInstance(this.graphHopper.getTranslationMap());
            long timeout = this.args.getLong("web.timeout", 3000);
            this.bind(Long.class).annotatedWith(Names.named("timeout")).toInstance(timeout);
            boolean jsonpAllowed = this.args.getBool("web.jsonpAllowed", false);
            if (!jsonpAllowed) {
                this.logger.info("jsonp disabled");
            }
            this.bind(Boolean.class).annotatedWith(Names.named("jsonpAllowed")).toInstance(jsonpAllowed);
            this.bind(RouteSerializer.class).toInstance(new SimpleRouteSerializer(this.graphHopper.getGraphHopperStorage().getBounds()));
            Matrix<GHPlace> matrix = Matrix.load(this.args);
            this.bind(new TypeLiteral<List<GHPlace>>(){}).toInstance(matrix.getPoints());
            this.bind(TourCalculator.class).toInstance(new TourCalculator<GHPlace>(matrix, this.graphHopper));
            this.bind(TourSerializer.class).toInstance(new TourSerializer());
        }
        catch (Exception ex) {
            throw new IllegalStateException("Couldn't load graph", ex);
        }
    }

}

