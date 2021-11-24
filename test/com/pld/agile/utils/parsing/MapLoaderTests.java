package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {

    @Test
    // Test n°1.1
    public void test5Intersections4Segments() {
        String filePath = "test/resources/loadMap_5Inter4Seg.xml";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        boolean res = mapLoader.load();
        assertTrue(res);

        String expected = "MapData{intersections={1=Intersection{id='0', latitude=45.0, longitude=4.0}, 2=Intersection{id='1', latitude=45.0, longitude=4.00128}, 3=Intersection{id='2', latitude=45.0009, longitude=4.0}, 4=Intersection{id='3', latitude=45.0, longitude=3.99872}, 5=Intersection{id='4', latitude=44.9991, longitude=4.0}}, segments=[Segment{length=100.0, name='Avenue Général Frère', origin=Intersection{id='0', latitude=45.0, longitude=4.0}, destination=Intersection{id='1', latitude=45.0, longitude=4.00128}}, Segment{length=100.0, name='Boulevard Général Frère', origin=Intersection{id='0', latitude=45.0, longitude=4.0}, destination=Intersection{id='2', latitude=45.0009, longitude=4.0}}, Segment{length=100.0, name='Rue de la Meuse', origin=Intersection{id='0', latitude=45.0, longitude=4.0}, destination=Intersection{id='3', latitude=45.0, longitude=3.99872}}, Segment{length=100.0, name='Rue de la Moselle', origin=Intersection{id='0', latitude=45.0, longitude=4.0}, destination=Intersection{id='4', latitude=44.9991, longitude=4.0}}]}";
        assertEquals(expected, mapData.toString());
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
