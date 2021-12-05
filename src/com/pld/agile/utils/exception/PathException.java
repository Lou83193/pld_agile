/*
 * SyntaxException
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.exception;

/**
 * Exception raised when there is an error computing a path.
 */
public class PathException extends Exception {
    public PathException(String errMsg) {
        super(errMsg);
    }
}
