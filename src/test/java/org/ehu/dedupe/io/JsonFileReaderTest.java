package org.ehu.dedupe.io;

import com.google.gson.reflect.TypeToken;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class JsonFileReaderTest {
    @Test
    public void testReadJsonResourceFile() throws Exception {
        List<Integer> list = JsonFileReader.readJsonFile(new TypeToken<List<Integer>>() {
        }, "./src/test/resources/org/ehu/dedupe/io/new.json");
        Assert.assertTrue(list.containsAll(Arrays.asList(1, 2, 3)));
    }

}