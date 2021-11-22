package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.MapLoader;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {

    @Test
    public void testLoadFileNoMapNode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //redirect the System-output (normally the console) to a variable
        System.setErr(new PrintStream(outContent));

        String filePath = "test/resources/loadMap_noMapNode";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        boolean res = mapLoader.load();
        assertFalse(res);
        //check if your error message is in the output variable
        assertEquals("org.dom4j.DocumentException: Error on line 3 of document", outContent.toString().substring(0, 56));
    }

}
