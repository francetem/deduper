package org.ehu.dedupe.derive.strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DocFrequency {
    Map<String, Integer> docFrequency = new HashMap<>();

    public DocFrequency() {
    }

    public void documentCount(Set<String> words) {
        for (String word : words) {
            if (docFrequency.containsKey(word)) {
                docFrequency.put(word, docFrequency.get(word) + 1);
            } else {
                docFrequency.put(word, 1);
            }
        }
    }

    public int getSize() {
        return docFrequency.size();
    }

    public Optional<Integer> get(String word) {
        return Optional.ofNullable(docFrequency.get(word));
    }
}