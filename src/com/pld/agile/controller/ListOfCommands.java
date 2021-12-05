/*
 * ListOfCommands
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import java.util.LinkedList;

public class ListOfCommands {
    /**
     * List that stores all the executed commands up to this point, in order in which they have been executed.
     */
    private LinkedList<Command> list;

    /**
     * Index of the last executed command (position in the list).
     */
    private int lastCommandIndex;

    /**
     * Constructor for class ListOfCommands.
     */
    public ListOfCommands() {
        lastCommandIndex = -1;
        list = new LinkedList<>();
    }

    /**
     * Adds a command to the bottom of the list of commands, and updates lastCommandIndex accordingly.
     * @param command Command to add to the list of commands.
     */
    public void add(Command command) {
        lastCommandIndex++;
        list.add(lastCommandIndex, command);
        command.doCommand();
    }

    /**
     * Undoes last command of the list.
     */
    public void undo() {
        if (lastCommandIndex >= 0) {
            list.get(lastCommandIndex).undoCommand();
            lastCommandIndex--;
        }
    }

    /**
     * Redoes last undone command, if there is one.
     */
    public void redo() {
        if (lastCommandIndex < (list.size() - 1)) {
            lastCommandIndex++;
            list.get(lastCommandIndex).doCommand();
        }
    }

}
