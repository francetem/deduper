package org.ehu.dedupe.classifier;

import org.ehu.dedupe.classifier.weka.WekaUtils;
import org.ehu.dedupe.data.Buckets;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class DeduperTest {

    private Instances dataSet;
    private AbstractClassifier classifier;

    @BeforeMethod
    public void setUp() throws Exception {
        dataSet = WekaUtils.getCsvInstances("./src/test/resources/org/ehu/dedupe/classifier/weka/myDataSet.csv");
        classifier = (FilteredClassifier) SerializationHelper.read(new FileInputStream("./src/test/resources/org/ehu/dedupe/classifier/my.model"));
    }

    @Test
    public void testDedup() {
        Deduper deduper = new Deduper(dataSet, new DefaultClassifier(dataSet, true, classifier));
        Buckets<String> dedup = deduper.dedup();

        Collection<Set<String>> clusters = dedup.clusters();
        int size = clusters.size();
        assertFalse(clusters.isEmpty());

        deduper = new Deduper(dataSet, new DefaultClassifier(dataSet, true, classifier, classifier));
        dedup = deduper.dedup();
        assertEquals(size, dedup.clusters().size());
    }

    @Test
    public void testDedupForce() {
        Deduper deduper = new Deduper(dataSet, new DefaultClassifier(dataSet, true, classifier));
        Buckets<String> dedup = deduper.dedup(0.5);
        assertFalse(dedup.clusters().isEmpty());
    }
}
