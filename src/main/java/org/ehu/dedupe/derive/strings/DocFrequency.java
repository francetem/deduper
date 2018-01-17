package org.ehu.dedupe.derive.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class DocFrequency {

    private Map<String, Integer> docFrequency = new HashMap<>();

    public DocFrequency() {
    }

    public List<String> document(String doc) {
        List<String> words1 = words(doc);
        documentCount(words1);
        return words1;
    }

    void documentCount(List<String> words) {
        for (String word : words) {
            if (docFrequency.containsKey(word)) {
                docFrequency.put(word, docFrequency.get(word) + 1);
            } else {
                docFrequency.put(word, 1);
            }
        }
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

    public Optional<Integer> get(String word) {
        return Optional.ofNullable(docFrequency.get(word));
    }

    public List<String> orderedByFrequency() {
        return docFrequency.keySet().stream().sorted((x, y) -> docFrequency.get(y) - docFrequency.get(x)).collect(Collectors.toList());
    }
}
