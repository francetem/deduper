package org.ehu.dedupe.classifier;

import org.ehu.dedupe.classifier.weka.WekaUtils;
import org.ehu.dedupe.data.Buckets;
import org.testng.annotations.Test;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.FileInputStream;

import static org.testng.Assert.assertNotNull;

public class DeduperTest {

    @Test
    public void testDedup() throws Exception {
        Instances dataSet = WekaUtils.getCsvInstances("./src/test/resources/org/ehu/dedupe/classifier/weka/myDataSet.csv");
        AbstractClassifier classifier = (FilteredClassifier) SerializationHelper.read(new FileInputStream("./src/test/resources/org/ehu/dedupe/classifier/my.model"));
        Buckets<String> dedup = Deduper.dedup(dataSet, classifier);

        assertNotNull(dedup);
    }

}