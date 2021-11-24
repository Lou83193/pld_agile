package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MapLoaderTests {

    @Test
    // Test n°1.1
    public void test5Intersections4Segments() {

    }

    @Test
    //Test n°1.4
    public void testNoMapNode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //redirect the System-output (normally the console) to a variable
        System.setErr(new PrintStream(outContent));

        String filePath = "test/resources/loadMap_noMapNode.xml";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        boolean res = mapLoader.load();
        assertFalse(res);
        //check if the beginning of the error message is the one expected
        assertEquals("org.dom4j.DocumentException: Error on line 3 of document", outContent.toString().substring(0, 56));
    }

}
