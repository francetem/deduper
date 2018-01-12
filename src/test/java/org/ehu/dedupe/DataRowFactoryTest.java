package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.derive.CalculationResult;
import org.ehu.dedupe.derive.FeatureCalculator;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testng.Assert.assertTrue;

public class DataRowFactoryTest {
    @Test
    public void testFrom() throws Exception {

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DataRowBuilder<Integer, Source<Integer>, DataRowTest> builder = new DataRowBuilder<>(DataRowTest.class).withSources(sources);
        List<DataRowTest> dataRows = new DataRowFactory().from(builder);
        assertTrue(dataRows.isEmpty());

        builder.withFeatureCalculators(Collections.singletonList(new IdentityFeatureCalculator()));
        dataRows = new DataRowFactory().from(builder);
        assertTrue(dataRows.containsAll(Arrays.asList(new DataRowTest(2, 1, false), new DataRowTest(1, 3, false), new DataRowTest(2, 3, false))));
    }

    private static final class DataRowTest extends DataRow<Integer> {

        public DataRowTest(Integer id1, Integer id2, Boolean duplicate) {
            super(id1, id2, duplicate);
        }
    }

    private static final class IdentityFeatureCalculator implements FeatureCalculator {

        @Override
        public void assign(CalculationResult calculationResult) {
            //nothing
        }

        @Override
        public CalculationResult calculate(Object x, Object y, DataRow dataRow) {
            return new CalculationResult(this, dataRow, null);
        }
    }

}