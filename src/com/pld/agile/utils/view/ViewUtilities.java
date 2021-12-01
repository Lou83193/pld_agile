/*
 * ViewUtilities
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.view;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

public class ViewUtilities {

    public static Color GREY = Color.web("#545454");
    public static Color ORANGE = Color.web("#ED6A08");
    public static Color BLUE = Color.web("#1F69ED");
    public static Color DARK_ORANGE = Color.web("#ED3213");
    public static Color TURQUOISE = Color.web("#1FED92");
    public static Color YELLOW = Color.web("#EDA813");
    public static Color DARK_PURPLE = Color.web("#270DA1");
    public static Color PURPLE = Color.web("#431FED");
    public static Color GREEN = Color.web("#21ED1F");

    public static double lerpValue(final double x, final double y, final double p) {
        return x + p * (y - x);
    }

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

    public static double mercatorInv(final double mercAng) {
        return (Math.atan(Math.exp(mercAng * Math.PI / 180)) - Math.PI / 4) * 360 / Math.PI;
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

    public static double[] projectMercatorLatLonInv(final double x, final double y, final double minLat, final double minLon, final double maxLat, final double maxLon, final double viewPortSize) {
        double mercMinLat = mercator(minLat);
        double mercMaxLat = mercator(maxLat);
        double mapWidth = viewPortSize * (maxLon - minLon) / (mercMaxLat - mercMinLat);
        double mapHeight = viewPortSize;
        double lon = ViewUtilities.mapValue(x, 0, mapWidth, minLon, maxLon);
        double mercLat = ViewUtilities.mapValue(y, mapHeight, 0, mercMinLat, mercMaxLat);
        double lat = mercatorInv(mercLat);
        return new double[] {lat, lon};
    }

    public static Color stringToColour(final String s) {
        int hash = s.hashCode();
        double r = ((hash & 0xFF0000) >> 16) / 255.0;
        double g = ((hash & 0x00FF00) >> 8) / 255.0;
        double b = (hash & 0x0000FF) / 255.0;
        return new Color(r, g, b, 1.0).brighter();
    }

    public static double distanceLatLon(final double lat1, final double lon1, final double lat2, final double lon2) {
        double earthRadius = 6378137.0;
        double lat1r = lat1 * Math.PI / 180;
        double lon1r = lon1 * Math.PI / 180;
        double lat2r = lat2 * Math.PI / 180;
        double lon2r = lon2 * Math.PI / 180;
        return Math.acos(Math.sin(lat1r) * Math.sin(lat2r) + Math.cos(lat1r) * Math.cos(lat2r) * Math.cos(lon2r - lon1r)) * earthRadius;
    }

    public static double distance(final double[] p1, final double[] p2) {
        return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
    }

    public static double direction(final double[] p1, final double[] p2) {
        return Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
    }

    public static Color mixColours(final Color c1, final Color c2, final double p) {
        double r = lerpValue(c1.getRed(), c2.getRed(), p);
        double g = lerpValue(c1.getGreen(), c2.getGreen(), p);
        double b = lerpValue(c1.getBlue(), c2.getBlue(), p);
        double a = lerpValue(c1.getOpacity(), c2.getOpacity(), p);
        return new Color(r, g, b, a);
    }

    // Source: https://stackoverflow.com/questions/12837592/how-to-scroll-to-make-a-node-within-the-content-of-a-scrollpane-visible
    public static void ensureVisible(ScrollPane pane, Node node) {
        double width = pane.getContent().getBoundsInParent().getWidth();
        double height = pane.getContent().getBoundsInParent().getHeight();
        double x = node.getBoundsInParent().getMaxX();
        double yMax = node.getBoundsInParent().getMaxY();
        double yMin = node.getBoundsInParent().getMinY();
        double yCurr = pane.getVvalue() * height;
        Bounds paneBounds = pane.localToScene(pane.getBoundsInParent());
        Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
        if (paneBounds.intersects(nodeBounds)) {
            return;
        }
        if (yMin < 1) {
            pane.setVvalue(0);
        } else if (yMax > yCurr) {
            pane.setVvalue(yMax / height);
        } else if (yMin < yCurr) {
            pane.setVvalue(yMin / height);
        }
        pane.setHvalue(x / width);
        node.requestFocus();
    }

}
