package org.ehu.dedupe.derive.strings.cosine;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.strings.DocFrequency;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.testng.Assert.assertEquals;

public class CosineSimilarityFeatureDeriverTest {

    @Test
    public void testCosine(){
        CosineSimilarityFeatureDeriver<String> deriver = new CosineSimilarityFeatureDeriver<>("any", Function.identity(), new DocFrequency());
        CalculationResult<BigDecimal, DataRow> calculation1 = deriver.calculate("Ciboulette Restaurant 1529 Piedmont Ave. Atlanta 404-874-7600 French (New)", "Ciboulette 1529 Piedmont Ave. Atlanta 404/874-7600 French", new DataRow(1, 2, true));
        CalculationResult<BigDecimal, DataRow> calculation2 = deriver.calculate("Delectables 1 Margaret Mitchell Sq. Atlanta 404-681-2909 Cafeterias", "Delectables 1 Margaret Mitchell Sq. Atlanta 404/681-2909 American", new DataRow(3, 4, true));
        CalculationResult<BigDecimal, DataRow> calculation3 = deriver.calculate("Ciboulette Restaurant 1529 Piedmont Ave. Atlanta 404-874-7600 French (New)", "Delectables 1 Margaret Mitchell Sq. Atlanta 404/681-2909 American", new DataRow(1, 4, false));
        CalculationResult<BigDecimal, DataRow> calculation4 = deriver.calculate("Ciboulette Restaurant 1529 Piedmont Ave. Atlanta 404-874-7600 French (New)", "Delectables 1 Margaret Mitchell Sq. Atlanta 404-681-2909 Cafeterias", new DataRow(1, 3, false));

        BigDecimal result1 = calculation1.getResult().process();
        BigDecimal result2 = calculation2.getResult().process();
        BigDecimal result3 = calculation3.getResult().process();
        BigDecimal result4 = calculation4.getResult().process();

        assertEquals(result1, new BigDecimal("0.5774061"));
        assertEquals(result2, new BigDecimal("0.5597349"));
        assertEquals(result3, new BigDecimal("0.03243799"));
        assertEquals(result4, new BigDecimal("0.03243799"));

    }

}