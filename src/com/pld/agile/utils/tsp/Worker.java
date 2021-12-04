/*
 * Worker
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Worker<S, T> extends Task<S> {

    private List<T> chunks = new ArrayList<>();
    private final Object lock = new Object();
    private boolean chunkUpdating = false;

    protected final void publish(T... results) {
        synchronized (lock) {
            chunks.addAll(Arrays.asList(results));
            if (!chunkUpdating) {
                chunkUpdating = true;
                Platform.runLater(() -> {
                    List<T> cs;
                    synchronized (lock) {
                        cs = chunks;
                        // create new list to not unnecessary lock worker thread
                        chunks = new ArrayList<>();
                        chunkUpdating = false;
                    }
                    try {
                        process(cs);
                    } catch (RuntimeException ex) {
                    }
                });
            }
        }
    }

    protected void process(List<T> chunks) {
    }

}