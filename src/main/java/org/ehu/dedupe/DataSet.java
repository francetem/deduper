package org.ehu.dedupe;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.FeatureDeriver;
import org.ehu.dedupe.io.CsvDataSetWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DataSet<K extends Comparable<K>> {
    private final Set<DataRow<K>> rows;
    private final List<? extends FeatureDeriver> featureDerivers;

    public DataSet(Set<DataRow<K>> rows) {
        this.rows = rows;
        this.featureDerivers = Collections.emptyList();
    }

    public DataSet(Set<DataRow<K>> rows, List<? extends FeatureDeriver> featureDerivers) {
        this.rows = rows;
        this.featureDerivers = featureDerivers;
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Set<DataRow<K>> getRows() {
        return rows;
    }

    public void writeToCsv(String datasetFileName) throws IOException {
        new CsvDataSetWriter(featureDerivers).writeToCsv(rows, datasetFileName);
    }

    public void writeToCsv(BufferedWriter bufferedWriter) throws IOException {
        new CsvDataSetWriter(featureDerivers).writeToCsv(rows, bufferedWriter);
    }
}
