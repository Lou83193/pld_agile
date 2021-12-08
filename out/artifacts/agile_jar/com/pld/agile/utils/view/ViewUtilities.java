/*
 * ViewUtilities
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.view;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Utility class providing static methods for various purposes.
 */
public class ViewUtilities {

    /**
     * Map of colours containing the colour palette of the application.
     */
    public static final Map<String, Color> COLOURS;
    static {
        Map<String, Color> map = new HashMap<>();
        map.put("GREY", Color.web("#545454"));
        map.put("ORANGE", Color.web("#ED6A08"));
        map.put("BLUE", Color.web("#1F69ED"));
        map.put("DARK_ORANGE", Color.web("#ED3213"));
        map.put("TURQUOISE", Color.web("#1FED92"));
        map.put("YELLOW", Color.web("#EDA813"));
        map.put("DARK_PURPLE", Color.web("#270DA1"));
        map.put("PURPLE", Color.web("#431FED"));
        map.put("GREEN", Color.web("#21ED1F"));
        COLOURS = Collections.unmodifiableMap(map);
    }

    /**
     * Image used for the textual view's stop "Delete" button
     */
    public static final Image DELETE_ICON = new Image("deleteIcon.png", 20, 20, true, true);
    /**
     * Image used for the textual view's stop "Shift Up" button
     */
    public static final Image UP_ARROW_ICON = new Image("upArrowIcon.png", 20, 20, true, true);
    /**
     * Image used for the textual view's stop "Shift Down" button
     */
    public static final Image DOWN_ARROW_ICON = new Image("downArrowIcon.png", 20, 20, true, true);

    /**
     * Returns the linear interpolation between two values.
     * @param x The first value.
     * @param y The second value.
     * @param p The percentage of interpolation (between 0 and 1).
     * @return The interpolated value.
     */
    public static double lerpValue(final double x, final double y, final double p) {
        return x + p * (y - x);
    }

    /**
     * Returns the linear interpolation of a value from a range to another range.
     * @param x The value to be interpolated.
     * @param min1 The min of the initial range.
     * @param max1 The max of the initial range.
     * @param min2 The min of the destination range.
     * @param max2 The max of the destination range.
     * @return The interpolated value.
     */
    public static double mapValue(final double x, final double min1, final double max1, final double min2, final double max2) {
        return min2 + (x - min1) / (max1 - min1) * (max2 - min2);
    }

    /**
     * Clamps a value between a range.
     * @param x The value to be clamped.
     * @param min The min of the range.
     * @param max The max of the range.
     * @return The clamped value.
     */
    public static double clamp(double x, double min, double max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * Performs a mercator transformation on a latitude angle.
     * @param ang The latitude angle.
     * @return The mercator transformation.
     */
    public static double mercator(final double ang) {
        return Math.log(Math.tan((Math.PI / 4) + (ang * Math.PI / 360))) * 180 / Math.PI;
    }

    /**
     * Performs an inverse mercator transformation on a mercator-transformed latitude angle.
     * @param mercAng The mercator-transformed latitude angle.
     * @return The initial latitude angle.
     */
    public static double mercatorInv(final double mercAng) {
        return (Math.atan(Math.exp(mercAng * Math.PI / 180)) - Math.PI / 4) * 360 / Math.PI;
    }

    /**
     * Converts a world latitude / longitude coordinate into an on-screen pixel coordinate.
     * A coordinate that is on the edge of the lat/lon set of coordinates will end up on the edge of the viewport.
     * @param lat The input latitude.
     * @param lon The input longitude.
     * @param minLat The min of all latitudes.
     * @param minLon The min of all longitudes.
     * @param maxLat The max of all latitudes.
     * @param maxLon The max of all longitudes.
     * @param viewPortSize The size of the viewport.
     * @return A pixel (x, y) coordinate.
     */
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

    /**
     * Converts an on-screen pixel coordinate into a world latitude / longitude coordinate.
     * A coordinate that is on the edge of the viewport will end up on the edge of the lat/lon set of coordinates.
     * @param x The input x coordinate.
     * @param y The input y coordinate.
     * @param minLat The min of all latitudes.
     * @param minLon The min of all longitudes.
     * @param maxLat The max of all latitudes.
     * @param maxLon The max of all longitudes.
     * @param viewPortSize The size of the viewport.
     * @return A world (lat, lon) coordinate.
     */
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

    /**
     * Hashes a given string into a Color.
     * @param s The string to be hashed.
     * @return The resulting Color.
     */
    public static Color stringToColour(final String s) {
        int hash = s.hashCode();
        double r = ((hash & 0xFF0000) >> 16) / 255.0;
        double g = ((hash & 0x00FF00) >> 8) / 255.0;
        double b = (hash & 0x0000FF) / 255.0;
        return new Color(r, g, b, 1.0).brighter();
    }

    /**
     * Returns a random colour based on the given random seed.
     * @param seed The random seed.
     * @return A random colour.
     */
    public static Color getRandomColour(final int seed) {
        Random rand = new Random(seed);
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b, 1.0);
        return randomColor;
    }

    /**
     * Computes the distance between two world lat/lon coordinates, in meters.
     * @param lat1 The latitude of the first coordinate.
     * @param lon1 The longitude of the first coordinate.
     * @param lat2 The latitude of the second coordinate.
     * @param lon2 The longitude of the second coordinate.
     * @return The distance between the two coordinates, in meters.
     */
    public static double distanceLatLon(final double lat1, final double lon1, final double lat2, final double lon2) {
        double earthRadius = 6378137.0;
        double lat1r = lat1 * Math.PI / 180;
        double lon1r = lon1 * Math.PI / 180;
        double lat2r = lat2 * Math.PI / 180;
        double lon2r = lon2 * Math.PI / 180;
        return Math.acos(Math.sin(lat1r) * Math.sin(lat2r) + Math.cos(lat1r) * Math.cos(lat2r) * Math.cos(lon2r - lon1r)) * earthRadius;
    }

    /**
     * Computes the euclidean distance between two set of cartesian pixel coordinates.
     * @param p1 The first pixel coordinate.
     * @param p2 The second pixel coordinate.
     * @return The distance between the coordinates.
     */
    public static double distance(final double[] p1, final double[] p2) {
        return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
    }

    /**
     * Computes the angle between two set of cartesian pixel coordinates.
     * @param p1 The first pixel coordinate.
     * @param p2 The second pixel coordinate.
     * @return The angle between the coordinates, in radians.
     */
    public static double direction(final double[] p1, final double[] p2) {
        return Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
    }

    /**
     * Interpolates two colours together.
     * @param c1 The first colour.
     * @param c2 The second colour.
     * @param p The interpolation percentage (between 0 and 1).
     * @return The resulting colour.
     */
    public static Color mixColours(final Color c1, final Color c2, final double p) {
        double r = lerpValue(c1.getRed(), c2.getRed(), p);
        double g = lerpValue(c1.getGreen(), c2.getGreen(), p);
        double b = lerpValue(c1.getBlue(), c2.getBlue(), p);
        double a = lerpValue(c1.getOpacity(), c2.getOpacity(), p);
        return new Color(r, g, b, a);
    }

    /**
     * Ensures a ScrollPane's viewport contains the given node, by automatically scrolling to it.
     * Prerequisites: The Node is part of the ScrollPane.
     * @param pane The ScrollPane.
     * @param node The Node that must be visible in the ScrollPane.
     */
    public static void ensureVisible(ScrollPane pane, Node node) {
        Bounds viewPortBound = pane.getViewportBounds();
        Bounds nodeBounds = node.getBoundsInParent();
        Bounds contentBounds = pane.getContent().getLayoutBounds();
        double viewPortMidPoint = (viewPortBound.getMaxY() + viewPortBound.getMinY())/2;
        double nodeMidPoint = (nodeBounds.getMaxY() + nodeBounds.getMinY())/2;
        double currTopY = ViewUtilities.mapValue(pane.getVvalue(), pane.getVmin(), pane.getVmax(), contentBounds.getMinY(), contentBounds.getMaxY() - viewPortBound.getHeight());
        if (nodeBounds.getMinY() >= currTopY && nodeBounds.getMaxY() <= currTopY + viewPortBound.getHeight()) {
            return;
        }
        double desiredY;
        if (nodeMidPoint - currTopY < viewPortMidPoint) {
            desiredY = nodeBounds.getMinY();
        } else {
            desiredY = nodeBounds.getMaxY() - viewPortBound.getHeight();
        }
        double v = ViewUtilities.mapValue(desiredY, contentBounds.getMinY(), contentBounds.getMaxY() - viewPortBound.getHeight(), pane.getVmin(), pane.getVmax());
        pane.setVvalue(ViewUtilities.clamp(v, pane.getVmin(), pane.getVmax()));
        //node.requestFocus();
    }

}
