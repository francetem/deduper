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


## Pair resolution & clustering
````java
        ByteArrayOutputStream stream;
        BufferedWriter bufferedWriter;
        Instances instances;

        stream = new ByteArrayOutputStream();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(stream));

        sources.stream()
                .collect(Sources.collector())
                .onlyIn(test)
                .block(this::blockingPredicate)
                .deriving()
                .withFeatureDerivers(getFeatureDerivers())
                .withBuckets(test)
                .derive()
                .writeToCsv(bufferedWriter);

        bufferedWriter.close();

        instances = WekaUtils.getCsvInstances(new BufferedInputStream(new ByteArrayInputStream(stream.toByteArray())));

        PairResolution resolution = Solver.pairResolve(abstractClassifier, instances, threshold);
        Buckets<String> clusters = resolution.toNormalizedClusters();
````
## Evaluation
````java
   GMD gmd = new GMD();
   BigDecimal cost = gmd.cost(clusters, buckets);
````

[![Build Status](https://travis-ci.org/francetem/deduper.svg?branch=master)](https://travis-ci.org/francetem/deduper)
