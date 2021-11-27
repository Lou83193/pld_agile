/*
 * ViewUtilities
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.view;

import javafx.scene.paint.Color;

public class ViewUtilities {

    public static double mapValue(final double x, final double min1, final double max1, final double min2, final double max2) {
        return min2 + (x - min1) / (max1 - min1) * (max2 - min2);
    }

    public static double clamp(double x, double min, double max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public static double mercator(final double ang) {
        return Math.log(Math.tan((Math.PI / 4) + (ang * Math.PI / 360))) * 180 / Math.PI;
    }

    public static double[] projectMercatorLatLon(final double lat, final double lon, final double minLat, final double minLon, final double maxLat, final double maxLon, final double viewPortSize) {
        double mercMinLat = mercator(minLat);
        double mercMaxLat = mercator(maxLat);
        double mercLat = mercator(lat);
        double mapWidth = viewPortSize * (maxLon - minLon) / (mercMaxLat - mercMinLat);
        double mapHeight = viewPortSize;
        double x = ViewUtilities.mapValue(lon, minLon, maxLon, 0, mapWidth);
        double y = ViewUtilities.mapValue(mercLat, mercMinLat, mercMaxLat, mapHeight, 0);
        return new double[] {x, y};
    }

    public static Color stringToColour(final String s) {
        int hash = s.hashCode();
        double r = ((hash & 0xFF0000) >> 16)/255.0;
        double g = ((hash & 0x00FF00) >> 8)/255.0;
        double b = (hash & 0x0000FF)/255.0;
        return new Color(r, g, b, 1.0).brighter();
    }

    public static double distance(final double[] p1, final double[] p2) {
        return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
    }

    public static double direction(final double[] p1, final double[] p2) {
        return Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
    }

}
