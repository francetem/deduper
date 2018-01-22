package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.data.Source;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DataRowFactoryTest {

    @Test
    public void testFromWithNoDerivers() throws Exception {

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DataRowBuilder<Integer, Source<Integer>, DataRowTest> builder = new DataRowBuilder<>(DataRowTest.class).withSources(sources);
        Set<DataRowTest> dataRows = new DataRowFactory().from(builder);
        assertTrue(dataRows.isEmpty());
    }

    @Test
    public void testFromWithOneDeriver() throws Exception {

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DataRowBuilder<Integer, Source<Integer>, DataRowTest> builder = new DataRowBuilder<>(DataRowTest.class)
                .withSources(sources)
                .withFeatureCalculators(Collections.singletonList(new IdentityFeatureCalculator()));
        Set<DataRowTest> dataRows = new DataRowFactory().from(builder);

        assertEquals(dataRows, Stream.of(new DataRowTest(2, 1, false), new DataRowTest(1, 3, false), new DataRowTest(2, 3, false)).collect(Collectors.toSet()));
    }

    @Test
    public void testFromWithSeveralDerivers() throws Exception {
        List<IdentityFeatureCalculator> derivers = Arrays.asList(new IdentityFeatureCalculator(), new IdentityFeatureCalculator());

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DataRowBuilder<Integer, Source<Integer>, DataRowTest> builder = new DataRowBuilder<>(DataRowTest.class)
                .withSources(sources)
                .withFeatureCalculators(derivers);
        Set<DataRowTest> dataRows = new DataRowFactory().from(builder);

        assertEquals(dataRows, Stream.of(new DataRowTest(2, 1, false), new DataRowTest(1, 3, false), new DataRowTest(2, 3, false)).collect(Collectors.toSet()));
    }

    private static final class DataRowTest extends DataRow<Integer> {

        public DataRowTest(Integer id1, Integer id2, Boolean duplicate) {
            super(id1, id2, duplicate);
        }

    }

}