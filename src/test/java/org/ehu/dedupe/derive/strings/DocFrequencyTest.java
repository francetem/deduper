package org.ehu.dedupe.derive.strings;

import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class DocFrequencyTest {

    @Test
    public void testOrderedByFrequency() throws Exception {
        DocFrequency docFrequency = new DocFrequency();
        docFrequency.documentCount(Stream.of("a", "b").collect(Collectors.toSet()));
        docFrequency.documentCount(Stream.of("b").collect(Collectors.toSet()));

        List<String> strings = docFrequency.orderedByFrequency();
        assertEquals(strings.get(0), "b");
        assertEquals(strings.get(1), "a");
    }

}