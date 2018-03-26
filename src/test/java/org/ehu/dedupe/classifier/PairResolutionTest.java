package org.ehu.dedupe.classifier;

import org.ehu.dedupe.data.Buckets;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class PairResolutionTest {
    @Test
    public void testRenderGraphs() throws Exception {
        Map<String, Set<String>> duplicates = new HashMap<>();
        duplicates.put("r1", Stream.of("r2").collect(Collectors.toSet()));
        PairResolution pairResolution = new PairResolution(duplicates, Collections.emptyMap());
        Buckets<String> normalizedClusters = pairResolution.toNormalizedClusters();
        Buckets<String> plainClusters = pairResolution.toPlainClusters();
        assertEquals(normalizedClusters.clusters().size(), plainClusters.clusters().size());
    }

}