package org.ehu.dedupe.derive.strings.levenshtein;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.CalculationResult;
import org.testng.annotations.Test;

import java.util.function.Function;

import static org.testng.Assert.assertEquals;

public class LevenshteinFeatureDeriverTest {

    @Test
    public void testLevenshteinFeatureDeriver() {
        LevenshteinFeatureDeriver<String> deriver = new LevenshteinFeatureDeriver<>("any", Function.identity());
        CalculationResult<Integer, DataRow> calculate = deriver.calculate("Hedgerose Heights Inn  The 490 E. Paces Ferry Rd. NE Atlanta 404-233-7673Continental", "Hedgerose Heights Inn 490 E. Paces Ferry Rd. Atlanta 404/233-7673 International", new DataRow(1, 2, true));
        assertEquals(calculate.getResult().process(), Integer.valueOf(18));
    }
}