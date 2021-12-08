/*
 * SyntaxException
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.exception;

/**
 * Exception raised when a loaded XML file is of incoherent syntax that doesn't make it possible to load a tour / map.
 */
public class SyntaxException extends Exception {
    public SyntaxException(String errMsg) {
        super(errMsg);
    }
}
