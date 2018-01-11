package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ReflectionDeriverTest {
    @Test
    public void testGet() throws Exception {
        ReflectionDeriver<Float, Float> latDeriver = new ReflectionDeriver<>("lat", (Float lat1, Float lat2) -> (lat1 + lat2) / 2);
        LatDataRow dataRow = new LatDataRow(1, 2, true);
        latDeriver.calculate(1f, 3f, dataRow);
        assertEquals(latDeriver.get(dataRow), 2f);
    }

    public static class LatDataRow extends DataRow {

        private Float lat;

        public LatDataRow(Comparable id1, Comparable id2, Boolean duplicate) {
            super(id1, id2, duplicate);
            this.lat = lat;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }
    }
}