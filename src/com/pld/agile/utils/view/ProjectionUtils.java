package com.pld.agile.utils.view;

public class ProjectionUtils {

    public static double mapValue(double x, double min1, double max1, double min2, double max2) {
        return min2 + (x-min1)/(max1-min1) * (max2-min2);
    }

    public static double[] projectLatLon(double lat, double lon, double minLat, double minLon, double maxLat, double maxLon, double width, double height) {
        double x = ProjectionUtils.mapValue(lon, minLon, maxLon, 0, width);
        double y = ProjectionUtils.mapValue(lat, minLat, maxLat, height, 0);
        return new double[] {x, y};
    }

}
