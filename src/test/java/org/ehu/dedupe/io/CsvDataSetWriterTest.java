package org.ehu.dedupe.io;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.FeatureDeriver;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CsvDataSetWriterTest {

    @Test
    public void testWriteToCsv() throws Exception {
        FeatureDeriver featureDeriver = Mockito.mock(FeatureDeriver.class);
        CsvDataSetWriter csvDataSetWriter = new CsvDataSetWriter(Collections.singletonList(featureDeriver));
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);

        csvDataSetWriter.writeToCSV(Collections.singletonList(new DataRow<>(1, 2, true)), bufferedWriter);

        verify(featureDeriver).get(any());
        verify(bufferedWriter, times(2)).newLine();
    }

}