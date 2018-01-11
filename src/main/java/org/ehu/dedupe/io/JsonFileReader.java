package org.ehu.dedupe.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonFileReader {

    private static final Gson GSON = new Gson();

    public static <T> T readJsonResourceFile(TypeToken<T> typeToken, String name) throws IOException {
        ClassLoader classLoader = JsonFileReader.class.getClassLoader();
        return readJsonResourceFile(typeToken, name, classLoader);
    }

    public static <T> T readJsonResourceFile(TypeToken<T> typeToken, String name, ClassLoader classLoader) throws IOException {
        String data = readJsonResourceFile(name, classLoader);
        return toJson(typeToken, data);
    }

    public static <T> T readJsonFile(TypeToken<T> typeToken, String name) throws IOException {
        String data= readJsonFile(name);
        return toJson(typeToken, data);
    }

    private static <T> T toJson(TypeToken<T> typeToken, String data) {
        Type type = typeToken.getType();
        return GSON.fromJson(data, type);
    }

    private static String readJsonResourceFile(String name, ClassLoader classLoader) throws IOException {
        String filename = classLoader.getResource(name).getFile();
        return readJsonFile(filename);
    }

    private static String readJsonFile(String filename) throws IOException {
        File file = new File(filename);
        return FileUtils.readFileToString(file, "UTF-8");
    }
}