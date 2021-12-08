/*
 * ControllerTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.view.Window;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTest {

    @Test
    public void testController(){
        Window window = new Window();
        Controller controller = new Controller(window);
        assertEquals(window.toString(),controller.getWindow().toString());
        assertEquals(controller.getCurrState().toString(),controller.getInitialState().toString());
    }

}