package org.ehu.dedupe.derive.geo;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class CoordinatesTest {

    @Test
    public void testDistFrom() throws Exception {
        Coordinates coordinates1 = new Coordinates(76.520781f, 200.7050673f);
        Coordinates coordinates2 = new Coordinates(41.403825f, 2.177091f);

        assertEquals(coordinates1.distFrom(coordinates2), BigDecimal.valueOf(6836947.5));
    }

    @Test
    public void testToString() throws Exception {
        String coordinates = new Coordinates(76.520781f, 200.7050673f).toString();
        assertEquals(coordinates, "76.52078,200.70506");
    }
}