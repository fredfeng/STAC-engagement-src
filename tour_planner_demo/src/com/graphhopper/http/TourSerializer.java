/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.tour.TourResponse;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourSerializer {
	public List<String> toList(TourResponse<?> rsp) {
		ArrayList<String> jsonPoints = new ArrayList<String>();
		for (GHPoint p : rsp.getPoints()) {
			jsonPoints.add(p.toString());
		}
		return jsonPoints;
	}

}
