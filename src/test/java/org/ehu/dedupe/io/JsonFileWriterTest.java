package org.ehu.dedupe.io;

import org.testng.annotations.Test;

import java.util.Arrays;

public class JsonFileWriterTest {

    @Test
    public void testToJsonFile() throws Exception {
        JsonFileWriter.toJsonFile(Arrays.asList(1, 2, 3), "./target/new.json");
    }

}