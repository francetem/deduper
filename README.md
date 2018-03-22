# deduper

deduper is a set of functionalities that helps the user to implement a deduplication process.

## Blocking, derive and dataset creation

````java
mySourcesCollection
                .collect(Sources.collector())
                .block(this::blockingPredicate)
                .deriving()
                .withFeatureDerivers(getFeatureDerivers())
                .derive()
                .writeToCsv("myDataSet.csv");
````

[![Build Status](https://travis-ci.org/francetem/deduper.svg?branch=master)](https://travis-ci.org/francetem/deduper)
