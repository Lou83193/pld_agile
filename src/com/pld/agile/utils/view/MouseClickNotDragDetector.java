/*
 * MouseClickNotDragDetector
 *
 * Source: https://stackoverflow.com/questions/41655507/javafx-distinguish-drag-and-click
 */

package com.pld.agile.utils.view;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;

public class MouseClickNotDragDetector {

    private final Node node;

    private Consumer<MouseEvent> onClickedNotDragged;
    private boolean wasDragged;
    private long timePressed;
    private long timeReleased;
    private long pressedDurationTreshold;

    private MouseClickNotDragDetector(Node node) {
        this.node = node;

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, (mouseEvent) -> {
            this.timePressed = currentTimeMillis();
        });

        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, (mouseEvent) -> {
            this.wasDragged = true;
        });

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, (mouseEvent) -> {
            this.timeReleased = currentTimeMillis();
            this.fireEventIfWasClickedNotDragged(mouseEvent);
            this.clear();
        });

        this.pressedDurationTreshold = 200;
    }

    public static MouseClickNotDragDetector clickNotDragDetectingOn(Node node) {
        return new MouseClickNotDragDetector(node);
    }

    public MouseClickNotDragDetector withPressedDurationThreshold(long durationTreshold) {
        this.pressedDurationTreshold = durationTreshold;
        return this;
    }

    public MouseClickNotDragDetector setOnMouseClickedNotDragged(Consumer<MouseEvent> onClickedNotDragged) {
        this.onClickedNotDragged = onClickedNotDragged;
        return this;
    }

    private void clear() {
        this.wasDragged = false;
        this.timePressed = 0;
        this.timeReleased = 0;
    }

    private void fireEventIfWasClickedNotDragged(MouseEvent mouseEvent) {
        if (this.wasDragged) {
            return;
        }
        if (this.mousePressedDuration() > this.pressedDurationTreshold) {
            return;
        }
        this.onClickedNotDragged.accept(mouseEvent);
    }

    private long mousePressedDuration() {
        return this.timeReleased - this.timePressed;
    }
}