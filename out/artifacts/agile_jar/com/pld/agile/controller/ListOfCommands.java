/*
 * ListOfCommands
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import java.util.LinkedList;
import java.util.List;

public class ListOfCommands {
    /**
     * List that stores all the executed commands up to this point, in order in which they have been executed.
     */
    private List<Command> list;

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
        int i = lastCommandIndex + 1;
        while(i < list.size()) {
            list.remove(i);
        }
        lastCommandIndex++;
        list.add(lastCommandIndex, command);
        command.doCommand();
    }

    /**
     * Undoes last command of the list.
     */
    public void undo() {
        if (canUndo()) {
            list.get(lastCommandIndex).undoCommand();
            lastCommandIndex--;
        }
    }

    /**
     * Redoes last undone command, if there is one.
     */
    public void redo() {
        if (canRedo()) {
            lastCommandIndex++;
            list.get(lastCommandIndex).doCommand();
        }
    }

    /**
     * Checks whether there are any undoable commands.
     * @return boolean result.
     */
    public boolean canUndo() {
        return (lastCommandIndex >= 0);
    }

    /**
     * Checks whether there are any redoable commands.
     * @return boolean result.
     */
    public boolean canRedo() {
        return (lastCommandIndex < (list.size() - 1));
    }

}
