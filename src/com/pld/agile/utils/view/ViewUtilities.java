/*
 * ViewUtilities
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.view;

import javafx.scene.paint.Color;

public class ViewUtilities {

    private static double padding = 0.02;

    public static double mapValue(double x, double min1, double max1, double min2, double max2) {
        return min2 + (x-min1)/(max1-min1) * (max2-min2);
    }

    public static double[] projectLatLon(double lat, double lon, double minLat, double minLon, double maxLat, double maxLon, double width, double height) {
        double x = ViewUtilities.mapValue(lon, minLon, maxLon, width*(padding), width*(1-padding));
        double y = ViewUtilities.mapValue(lat, minLat, maxLat, height*(1-padding), height*(padding));
        return new double[] {x, y};
    }

    public static Color stringToColour(String s) {
        int hash = s.hashCode();
        double r = ((hash & 0xFF0000) >> 16)/255.0;
        double g = ((hash & 0x00FF00) >> 8)/255.0;
        double b = (hash & 0x0000FF)/255.0;
        return new Color(r, g, b, 1.0).brighter();
    }

    public static double distance(double[] p1, double[] p2) {
        return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
    }

    public static double direction(double[] p1, double[] p2) {
        return Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
    }

}
