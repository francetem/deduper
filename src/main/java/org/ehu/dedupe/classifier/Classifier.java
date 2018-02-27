package org.ehu.dedupe.classifier;

import weka.core.Instance;

public interface Classifier {
    double classify(Instance instance) throws ClassifierException;
}
