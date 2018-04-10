package org.ehu.dedupe.derive.strings.cosine;

import org.ehu.dedupe.derive.strings.DocFrequency;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class CosineResultTest {
    @Test
    public void testProcess() throws Exception {
        DocFrequency docFrequency = new DocFrequency();
        docFrequency.document("a a c");
        docFrequency.document("b c");
        CosineResult cosineResult1 = new CosineResult(new ArrayList<>(Arrays.asList("a", "c", "c")), new ArrayList<>(Arrays.asList("b", "c")), docFrequency);
        BigDecimal process1 = cosineResult1.process();
        CosineResult cosineResult2 = new CosineResult(new ArrayList<>(Arrays.asList("b", "c")), new ArrayList<>(Arrays.asList("a", "c", "c")), docFrequency);
        BigDecimal process2 = cosineResult2.process();
        assertEquals(process1, process2);
    }

    @Test
    public void testProcessSame() throws Exception {
        CosineResult cosineResult = new CosineResult(new ArrayList<>(Arrays.asList("a", "b")), new ArrayList<>(Arrays.asList("a", "b")), new DocFrequency());
        BigDecimal process = cosineResult.process();
        assertEquals(process, BigDecimal.ONE);
    }

}