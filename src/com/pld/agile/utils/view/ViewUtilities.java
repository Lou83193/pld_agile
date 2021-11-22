package com.pld.agile.utils.view;

import javafx.scene.paint.Color;

public class ViewUtilities {

    public static double mapValue(double x, double min1, double max1, double min2, double max2) {
        return min2 + (x-min1)/(max1-min1) * (max2-min2);
    }

    public static double[] projectLatLon(double lat, double lon, double minLat, double minLon, double maxLat, double maxLon, double width, double height) {
        double x = ViewUtilities.mapValue(lon, minLon, maxLon, 0, width);
        double y = ViewUtilities.mapValue(lat, minLat, maxLat, height, 0);
        return new double[] {x, y};
    }

    public static Color stringToColour(String s) {
        int hash = s.hashCode();
        double r = ((hash & 0xFF0000) >> 16)/255.0;
        double g = ((hash & 0x00FF00) >> 8)/255.0;
        double b = (hash & 0x0000FF)/255.0;
        return new Color(r, g, b, 1.0);
    }

}
