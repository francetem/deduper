package org.ehu.dedupe.classifier.weka;

import org.mockito.Mockito;
import org.testng.annotations.Test;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import static org.testng.Assert.assertEquals;

public class WekaUtilsTest {

    @Test
    public void testCrossValidateModel() throws Exception {
        Instances csvInstances = WekaUtils.getCsvInstances("./src/test/resources/org/ehu/dedupe/classifier/weka/myDataSet.csv");
        AbstractClassifier classifier = WekaUtils.buildModel(csvInstances);
        Evaluation evaluation = WekaUtils.crossValidateModel(csvInstances, classifier);

        assertEquals(evaluation.toMatrixString(), "=== Confusion Matrix ===\n" +
                "\n" +
                "  a  b   <-- classified as\n" +
                "  4  0 |  a = true\n" +
                "  0 11 |  b = false\n");

        ByteArrayOutputStream out = Mockito.mock(ByteArrayOutputStream.class);
        WekaUtils.saveModel(classifier, new ObjectOutputStream(out));

        Mockito.verify(out).close();

    }

}