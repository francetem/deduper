package org.ehu.dedupe.io;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.FeatureDeriver;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;

public class CsvDataSetWriter {

    private static final String CSV_SEPARATOR = ";";
    private List<? extends FeatureDeriver> featureDerivers;


    public CsvDataSetWriter(List<? extends FeatureDeriver> featureDerivers) {
        this.featureDerivers = featureDerivers;
    }

    public void writeToCsv(Collection<? extends DataRow> dataSet, String datasetFileName) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datasetFileName), "UTF-8"))) {
            writeToCsv(dataSet, bw);
        }
    }

    public void writeToCsv(Collection<? extends DataRow> dataSet, BufferedWriter bw) throws IOException {
        writeHeader(bw);
        for (DataRow dataRow : dataSet) {
            write(bw, dataRow);
        }
    }

    public void write(BufferedWriter bw, DataRow dataRow) throws IOException {
        StringBuilder line = new StringBuilder()
                .append(dataRow.getId1())
                .append(CSV_SEPARATOR)
                .append(dataRow.getId2())
                .append(CSV_SEPARATOR);
        featureDerivers.stream()
                .map(feature -> feature.get(dataRow))
                .forEach(value -> line.append(value).append(CSV_SEPARATOR));
        line.append(dataRow.isDuplicate());
        bw.write(line.toString());
        bw.newLine();
    }

    public void writeHeader(BufferedWriter bw) throws IOException {
        bw.write("id1;id2;");
        for (FeatureDeriver featureDeriver : featureDerivers) {
            bw.write(featureDeriver.getName() + ";");
        }
        bw.write("class");
        bw.newLine();
    }
}
