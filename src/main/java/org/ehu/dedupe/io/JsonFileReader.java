package org.ehu.dedupe.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonFileReader {

    private static final Gson GSON = new Gson();

    public static <T> T readJsonResourceFile(TypeToken<T> typeToken, String name, Class clazz) throws IOException {
        String filename = clazz.getResource(name).getFile();
        String data = readJsonFile(filename);
        return toJson(typeToken, data);
    }

    public static <T> T readJsonFile(TypeToken<T> typeToken, String name) throws IOException {
        String data = readJsonFile(name);
        return toJson(typeToken, data);
    }

    private static <T> T toJson(TypeToken<T> typeToken, String data) {
        Type type = typeToken.getType();
        return GSON.fromJson(data, type);
    }

    private static String readJsonFile(String filename) throws IOException {
        File file = new File(filename);
        return FileUtils.readFileToString(file, "UTF-8");
    }
}