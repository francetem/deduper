package org.ehu.dedupe;

import org.ehu.dedupe.data.Buckets;
import org.ehu.dedupe.data.Source;
import org.ehu.dedupe.data.dyna.DynaDataRow;
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

public class DatasetFactoryTest {

    @Test
    public void testFromWithNoDerivers() throws Exception {
        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>> builder = new DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>>().withSources(sources);

        DataSet<Integer> dataSet = new DatasetFactory().from(builder);
        assertTrue(dataSet.isEmpty());
    }

    @Test
    public void testFromWithOneDeriver() throws Exception {

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>> builder = new DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>>()
                .withSources(sources)
                .withFeatureDerivers(Collections.singletonList(new IdentityFeatureCalculator()));
        DataSet<Integer> dataSet = new DatasetFactory().from(builder);

        Set<DynaDataRow<Integer>> rowTestSet = Stream.of(new DynaDataRow<>(2, 1, false), new DynaDataRow<>(1, 3, false), new DynaDataRow<>(2, 3, false)).collect(Collectors.toSet());
        assertEquals(dataSet.getRows(), rowTestSet);
    }

    @Test
    public void testFromWithSeveralDerivers() throws Exception {
        List<IdentityFeatureCalculator> derivers = Arrays.asList(new IdentityFeatureCalculator(), new IdentityFeatureCalculator());

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>> builder = new DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>>()
                .withSources(sources)
                .withFeatureDerivers(derivers);
        DataSet<Integer> dataSet = new DatasetFactory().from(builder);

        assertEquals(dataSet.getRows(), Stream.of(new DynaDataRow<>(2, 1, false), new DynaDataRow<>(1, 3, false), new DynaDataRow<>(2, 3, false)).collect(Collectors.toSet()));
    }

    @Test
    public void testOnInBucketFilter() throws Exception {

        List<Source<Integer>> sources = IntStream.of(1, 2, 3).boxed().map(Source::new).collect(Collectors.toList());

        DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>> builder = new DatasetBuilder<Integer, Source<Integer>, DynaDataRow<Integer>>()
                .withSources(sources)
                .withBuckets(Buckets.empty())
                .inBucket(true)
                .withFeatureDerivers(Collections.singletonList(new IdentityFeatureCalculator()));
        DataSet<Integer> dataSet = new DatasetFactory().from(builder);

        assertTrue(dataSet.isEmpty());
    }

}
