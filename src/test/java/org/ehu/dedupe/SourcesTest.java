package org.ehu.dedupe;

import org.apache.commons.lang3.tuple.Pair;
import org.ehu.dedupe.data.Source;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

public class SourcesTest {

    @Test
    public void testBlock() throws Exception {
        List<Pair<Source<Integer>, Source<Integer>>> pairs = IntStream.of(1, 2, 3)
                .boxed()
                .map(Source::new)
                .collect(Sources.collector())
                .block((x, y) -> x.getId() + y.getId() < 5)
                .getPairs();

        assertEquals(pairs.size(), 2);
    }

}