package org.ehu.dedupe.classifier;

import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.logging.Logger;

public class DefaultClassifier implements Classifier {

    private static final Logger LOGGER = Logger.getLogger("classifier");

    private final AbstractClassifier[] classifiers;
    private final boolean partialOnError;
    private final int indexOfDuplicateClassValue;

    public DefaultClassifier(boolean partialOnError, int indexOfDuplicateClassValue, AbstractClassifier... classifiers) {
        this.classifiers = classifiers;
        this.partialOnError = partialOnError;
        this.indexOfDuplicateClassValue = indexOfDuplicateClassValue;
    }

    public DefaultClassifier(Instances dataSet, boolean partialOnError, AbstractClassifier... classifiers) {
        this(partialOnError, getTrueIndexForClassAttribute(dataSet), classifiers);
    }

    public static int getTrueIndexForClassAttribute(Instances dataSet) {
        return dataSet.classAttribute().indexOfValue("true");
    }

    public boolean isPartialOnError() {
        return partialOnError;
    }

    public int getIndexOfDuplicateClassValue() {
        return indexOfDuplicateClassValue;
    }

    @Override
    public double classify(Instance instance) throws ClassifierException {
        double value = 0;
        int count = 0;
        for (AbstractClassifier classifier : classifiers) {
            try {
                value += classifier.distributionForInstance(instance)[getIndexOfDuplicateClassValue()];
                count++;
            } catch (Exception e) {
                LOGGER.warning("couldn't evaluate: " + instance.stringValue(0) + " " + instance.stringValue(1) + " for classifier: " + count + " error: " + e.getMessage());
            }
        }

        if (isPartialOnError() && count == 0 || !isPartialOnError() && count < classifiers.length) {
            throw new ClassifierException("no classification for: " + instance.stringValue(0) + " " + instance.stringValue(1));
        }

        value /= count;
        return value;
    }
}
