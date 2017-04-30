package com.graphhopper.tour.util;

public class SecurityUtil {
	
	//add dummy padding string to avoid side channel attack.
	public static String padding(String org, int bound) {
		int orgLen = org.length();
		assert (orgLen < bound);
		if (orgLen < bound) {
			for (int i = 0; i < bound - orgLen; i++) {
				org += " ";
			}
		}
		return org;
	}
	
}
