package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class DataSetTest {

    @Test
    public void testWriteToCsv() throws Exception {
        DataSet<Integer> set = new DataSet<>(Stream.of(new DataRow<>(1, 2, false)).collect(Collectors.toSet()));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(stream));
        set.writeToCsv(bufferedWriter);
        bufferedWriter.flush();
        byte[] bytes = stream.toByteArray();
        String s = new String(bytes, StandardCharsets.UTF_8);
        assertEquals("id1;id2;class\n1;2;false\n", s.replace("\r\n", "\n"));
    }

}
