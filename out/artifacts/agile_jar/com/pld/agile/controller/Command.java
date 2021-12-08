/*
 * Command
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

/**
 * A class that implements Command design pattern.
 */
public interface Command {

    /**
     * Does a command.
     */
    void doCommand();

    /**
     * Undoes a command.
     */
    void undoCommand();

}
