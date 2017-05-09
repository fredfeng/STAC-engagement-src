package com.graphhopper.tour.util;

public class SecurityUtil {
	
	//add dummy padding string to avoid side channel attack.
	public static String padding(String org, int bound) {
		int orgLen = org.length();
		assert (orgLen < bound);
		int delta = bound - orgLen;
		if (orgLen < bound) {
			for (int i = 0; i < delta; i++) {
				org += " ";
			}
		}
		return org;
	}
	
	public static String padding(int delta) {
		String str = "";
		for (int i = 0; i < delta; i++) {
			str += " ";
		}
		return str;
	}
	
}
