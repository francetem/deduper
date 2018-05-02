package org.ehu.dedupe.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.RemoveByName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Logger;

public class WekaUtils {

    private static final Logger LOGGER = Logger.getLogger("weka");

    public static Instances getCsvInstances(String datasetFileName) throws IOException {
        CSVLoader csvLoader = getCsvLoader();
        csvLoader.setFile(new File(datasetFileName));
        return getInstances(csvLoader);
    }

    public static Instances getCsvInstances(InputStream source) throws IOException {
        CSVLoader csvLoader = getCsvLoader();
        csvLoader.setSource(source);
        return getInstances(csvLoader);
    }

    public static Instances getInstances(CSVLoader csvLoader) throws IOException {
        Instances dataSet = csvLoader.getDataSet();
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        return dataSet;
    }

    public static CSVLoader getCsvLoader() {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setFieldSeparator(";");
        return csvLoader;
    }

    public static AbstractClassifier buildDefaultClassifier(Instances instances) throws Exception {

        RandomForest randomForest = new RandomForest();
        randomForest.setComputeAttributeImportance(true);

        return buildClassifier(instances, randomForest);
    }

    public static AbstractClassifier buildClassifier(Instances instances, AbstractClassifier classifier) throws Exception {
        RemoveByName removeByName = new RemoveByName();
        removeByName.setExpression("^id.$");

        FilteredClassifier fc = new FilteredClassifier();
        fc.setFilter(removeByName);
        fc.setClassifier(classifier);
        fc.buildClassifier(instances);

        LOGGER.info(classifier.toString());

        return fc;
    }

    public static Evaluation crossValidateModel(Instances dataSet, Classifier classifier) throws Exception {
        Evaluation eval = new Evaluation(dataSet);
        eval.crossValidateModel(classifier, dataSet, 10, new Random());

        return eval;
    }

    public static void saveModel(AbstractClassifier classifier, String file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        saveModel(classifier, oos);
    }

    public static void saveArff(Instances instances, String pathname) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(pathname));
        saver.writeBatch();
    }

    public static Instances readArff(String pathname) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(pathname);
        return source.getDataSet();
    }

    public static void saveModel(AbstractClassifier classifier, ObjectOutputStream oos) throws IOException {
        oos.writeObject(classifier);
        oos.flush();
        oos.close();
    }
}
