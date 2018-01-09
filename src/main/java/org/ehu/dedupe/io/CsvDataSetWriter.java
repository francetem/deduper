package org.ehu.dedupe.io;

import org.ehu.dedupe.data.DataRow;
import org.ehu.dedupe.derive.FeatureDeriver;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvDataSetWriter {

    private static final String CSV_SEPARATOR = ";";
    private List<FeatureDeriver> featureDerivers;


    public CsvDataSetWriter(List<FeatureDeriver> featureDerivers) {

        this.featureDerivers = featureDerivers;
    }

    public void writeToCSV(List<DataRow<Object>> dataSet, String datasetFileName) {

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datasetFileName), "UTF-8"))) {
            header(bw);
            for (DataRow<Object> dataRowAccommodation : dataSet) {
                StringBuilder line = new StringBuilder()
                        .append(dataRowAccommodation.getId1())
                        .append(CSV_SEPARATOR)
                        .append(dataRowAccommodation.getId2())
                        .append(CSV_SEPARATOR);
                featureDerivers.stream()
                        .map(feature -> feature.get(dataRowAccommodation))
                        .forEach(value -> line.append(value).append(CSV_SEPARATOR));
                line.append(dataRowAccommodation.isDuplicate());
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    void header(BufferedWriter bw) throws IOException {
        bw.write("id1;id2;");
        for (FeatureDeriver featureDeriver : featureDerivers) {
            bw.write(featureDeriver.getName() + ";");
        }
        bw.write("class");
        bw.newLine();
    }
}