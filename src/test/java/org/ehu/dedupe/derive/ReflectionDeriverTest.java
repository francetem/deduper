package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ReflectionDeriverTest {

    @Test
    public void testGet() throws Exception {
        ReflectionDeriver<Float, Float> latDeriver = new ReflectionDeriver<>("lat", (Float lat1, Float lat2) -> new SimpleResult<>((lat1 + lat2) / 2));
        LatDataRow dataRow = new LatDataRow(1, 2, true);
        assertEquals(latDeriver.calculate(1f, 3f, dataRow).getResult().process(), 2f);
    }

    @Test
    public void testAssignErrorMessage() throws Exception {
        ReflectionDeriver<String, String> deriver = new ReflectionDeriver<>("any", (x, y) -> null);
        try {
            deriver.assign(new CalculationResult<>(deriver, new DataRow<>(2, 4, true), new SimpleResult<>("result")));
            Assert.fail();
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "not able to set property: any");
        }
    }

}
