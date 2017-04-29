/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.graphhopper.tour.Matrix;
import com.graphhopper.tour.Places;
import com.graphhopper.tour.TourCalculator;
import com.graphhopper.tour.TourResponse;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;

public class TourServlet extends GHBaseServlet {
	@Inject
	private List<GHPlace> places;
	@Inject
	private TourCalculator tourCalculator;
	@Inject
	private TourSerializer tourSerializer;
	private Map<String, GHPlace> nameIndex;

	@Override
	public void init() {
		this.nameIndex = Places.nameIndex(this.places);
	}
	
	public TourServlet() {
		tourCalculator = new TourCalculator(null);
	}

	@Override
	public void doGet(HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
		List<GHPoint> points = this.getPoints(req, "point");
		TourResponse tourRsp = new TourResponse();
		tourRsp = this.tourCalculator.calcTour(points);
		List<String> map = this.tourSerializer.toList(tourRsp);
		PrintWriter writer = res.getWriter();
		writer.append(map.toString());
	}

	protected List<GHPoint> getPoints(HttpServletRequest req, String key) {
		String[] pointsAsStr = this.getParams(req, key);
		ArrayList<GHPoint> points = new ArrayList<GHPoint>(pointsAsStr.length);
		for (String str : pointsAsStr) {
			GHPoint point = GHPoint.parse(str);
			points.add(point);
		}
		return points;
	}

}
