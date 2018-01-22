package org.ehu.dedupe.data;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BucketsTest {
    private final Set<String> cluster1 = Stream.of("r1", "r3", "r5").collect(Collectors.toSet());
    private final Set<String> cluster2 = Stream.of("r2", "r4", "r6").collect(Collectors.toSet());

    private final Buckets<String> buckets = Buckets.from(Arrays.asList(cluster1, cluster2));

    @Test
    public void testFrom() throws Exception {
        assertEquals(buckets.get(0), cluster1);
        assertEquals(buckets.get(1), cluster2);
    }

    @Test
    public void testIsSameBucket() throws Exception {
        assertTrue(buckets.isSameBucket("r1", "r3"));
        Assert.assertFalse(buckets.isSameBucket("r1", "r2"));
    }

    @Test
    public void testClusters() throws Exception {
        assertEquals(buckets.clusters(), new HashSet<>(Arrays.asList(cluster1, cluster2)));
    }

    @Test
    public void testContaining() throws Exception {
        assertEquals(buckets.containing("r1").iterator().next().intValue(), 0);
        assertEquals(buckets.containing("r2").iterator().next().intValue(), 1);
    }

    @Test
    public void testDuplicates() throws Exception {
        assertEquals(buckets.duplicates("r1"), Stream.of("r3", "r5").collect(Collectors.toSet()));
    }

}