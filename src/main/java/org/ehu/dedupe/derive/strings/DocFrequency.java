package org.ehu.dedupe.derive.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class DocFrequency {

    public static final LongAdder EMPTY = new LongAdder();

    private Map<String, LongAdder> docFrequency = new ConcurrentHashMap<>();

    public DocFrequency() {
    }

    public List<String> document(String doc) {
        List<String> words1 = words(doc);
        documentCount(words1);
        return words1;
    }

    void documentCount(List<String> words) {
        words.forEach(word -> docFrequency.computeIfAbsent(word, x -> new LongAdder()).increment());
    }

    private static List<String> words(String name) {
        StringTokenizer tokenizer = new StringTokenizer(name);
        List<String> words1 = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            words1.add(tokenizer.nextToken());
        }
        return words1;
    }

    public int getSize() {
        return docFrequency.size();
    }

    public Integer get(String word) {
        return docFrequency.getOrDefault(word, EMPTY).intValue();
    }

    public List<String> orderedByFrequency() {
        return docFrequency.keySet().stream().sorted((x, y) -> docFrequency.get(y).intValue() - docFrequency.get(x).intValue()).collect(Collectors.toList());
    }
}
