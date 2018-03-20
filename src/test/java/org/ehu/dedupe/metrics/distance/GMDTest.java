package org.ehu.dedupe.metrics.distance;

import org.ehu.dedupe.data.Buckets;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GMDTest {

    @Test
    public void testCost() throws Exception {
        GMD gmd = new GMD();
        Set<String> cluster1 = Stream.of("r1", "r3", "r5").collect(Collectors.toSet());
        Set<String> cluster2 = Stream.of("r2", "r4", "r6").collect(Collectors.toSet());

        Set<String> cluster3 = Stream.of("r1", "r2", "r3").collect(Collectors.toSet());
        Set<String> cluster4 = Stream.of("r4", "r5", "r6").collect(Collectors.toSet());

        BigDecimal cost = gmd.cost(Buckets.from(Arrays.asList(cluster1, cluster2)), Buckets.from(Arrays.asList(cluster3, cluster4)));
        assertTrue(cost.compareTo(BigDecimal.valueOf(4)) == 0);
    }

    @Test
    public void testMerges() throws Exception {
        GMD gmd = new GMD();
        Set<String> cluster1 = Stream.of("r1").collect(Collectors.toSet());
        Set<String> cluster2 = Stream.of("r2").collect(Collectors.toSet());
        Set<String> cluster3 = Stream.of("r3").collect(Collectors.toSet());

        Set<String> cluster4 = Stream.of("r1","r2","r3").collect(Collectors.toSet());

        Buckets<String> bucket1 = Buckets.from(Arrays.asList(cluster1, cluster2, cluster3));
        Buckets<String> bucket2 = Buckets.from(Collections.singletonList(cluster4));

        gmd.cost(bucket1, bucket2);
        assertEquals(gmd.getMerges().size(), 1);

        gmd.cost(bucket2, bucket1);
        assertEquals(gmd.getSplits().size(), 1);
    }
}
