package org.ehu.dedupe.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileWriter {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static <T> void toJsonFile(T data, String file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        Files.write(Paths.get(file), json.getBytes(CHARSET));
    }
}
