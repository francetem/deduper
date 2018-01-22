package org.ehu.dedupe.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.unsupervised.attribute.RemoveByName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Logger;

public class WekaUtils {

    private static final Logger LOGGER = Logger.getLogger("weka");

    public static Instances getCsvInstances(String datasetFileName) throws IOException {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setFieldSeparator(";");
        csvLoader.setFile(new File(datasetFileName));
        Instances dataSet = csvLoader.getDataSet();
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        return dataSet;
    }

    public static AbstractClassifier buildModel(Instances instances) throws Exception {
        RemoveByName removeByName = new RemoveByName();
        removeByName.setExpression("^id.$");

        RandomForest randomForest = new RandomForest();
        randomForest.setComputeAttributeImportance(true);

        FilteredClassifier fc = new FilteredClassifier();
        fc.setFilter(removeByName);
        fc.setClassifier(randomForest);
        fc.buildClassifier(instances);

        LOGGER.info(randomForest.toString());

        return fc;
    }

    public static Evaluation crossValidateModel(Instances dataSet, Classifier randomForest) throws Exception {
        Evaluation eval = new Evaluation(dataSet);
        eval.crossValidateModel(randomForest, dataSet, 10, new Random(1));

        return eval;
    }

    public static void saveModel(AbstractClassifier classifier, String file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        saveModel(classifier, oos);
    }

    public static void saveModel(AbstractClassifier classifier, ObjectOutputStream oos) throws IOException {
        oos.writeObject(classifier);
        oos.flush();
        oos.close();
    }
}
