package ML;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.BaseVector;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;

import java.util.Objects;
import java.util.Properties;

public class SimpleRandomForest {
    private RandomForest model;
    private final int numTrees;
    private final int maxDepth;
    private final int minNodeSize;
    private int numFeatures = -1;

    public SimpleRandomForest(int numTrees) {
        this.numTrees = numTrees;
        this.maxDepth = 10;
        this.minNodeSize = 10;
    }

    public SimpleRandomForest(int numTrees, int maxDepth, int minNodeSize) {
        this.numTrees = numTrees;
        this.maxDepth = maxDepth;
        this.minNodeSize = minNodeSize;
    }

    public void train(double[][] features, int[] labels) {
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(labels, "labels must not be null");

        if (features.length == 0) {
            throw new IllegalArgumentException("features must not be empty");
        }
        if (features.length != labels.length) {
            throw new IllegalArgumentException("features and labels must have the same length");
        }

        this.numFeatures = features[0].length;

        DataFrame df = createDataFrame(features, labels);

        Properties props = new Properties();
        props.setProperty("smile.random.forest.trees", String.valueOf(numTrees));
        props.setProperty("smile.random.forest.max.depth", String.valueOf(maxDepth));
        props.setProperty("smile.random.forest.node.size", String.valueOf(minNodeSize));

        this.model = RandomForest.fit(Formula.lhs("Diabetes"), df, props);
    }

    public int[] predict(double[][] features) {
        if (model == null) {
            throw new IllegalStateException("Model not trained. Call train(...) first.");
        }
        Objects.requireNonNull(features, "features must not be null");

        if (features.length == 0) {
            throw new IllegalArgumentException("features must not be empty");
        }
        if (features[0].length != numFeatures) {
            throw new IllegalArgumentException(
                    "Feature count mismatch: expected " + numFeatures +
                            " but got " + features[0].length);
        }

        DataFrame df = createPredictionDataFrame(features);

        int n = features.length;
        int[] preds = new int[n];
        for (int i = 0; i < n; i++) {
            preds[i] = model.predict(df.get(i));
        }
        return preds;
    }

    public double accuracy(double[][] features, int[] actualLabels) {
        Objects.requireNonNull(actualLabels, "actualLabels must not be null");
        if (features.length != actualLabels.length) {
            throw new IllegalArgumentException("features and actualLabels must have the same length");
        }

        int[] preds = predict(features);
        int correct = 0;
        for (int i = 0; i < preds.length; i++) {
            if (preds[i] == actualLabels[i]) {
                correct++;
            }
        }
        return (double) correct / preds.length;
    }

    private DataFrame createDataFrame(double[][] features, int[] labels) {
        int numSamples = features.length;
        int numFeatures = features[0].length;

        String[] colNames = new String[numFeatures + 1];
        for (int i = 0; i < numFeatures; i++) {
            colNames[i] = "Feature_" + i;
        }
        colNames[numFeatures] = "Diabetes";

        BaseVector[] columns = new BaseVector[numFeatures + 1];

        for (int i = 0; i < numFeatures; i++) {
            double[] colData = new double[numSamples];
            for (int j = 0; j < numSamples; j++) {
                colData[j] = features[j][i];
            }
            columns[i] = DoubleVector.of(colNames[i], colData);
        }

        columns[numFeatures] = IntVector.of(colNames[numFeatures], labels);

        return DataFrame.of(columns);
    }

    private DataFrame createPredictionDataFrame(double[][] features) {
        int numSamples = features.length;
        int numFeatures = features[0].length;

        String[] colNames = new String[numFeatures + 1];
        for (int i = 0; i < numFeatures; i++) {
            colNames[i] = "Feature_" + i;
        }
        colNames[numFeatures] = "Diabetes";

        BaseVector[] columns = new BaseVector[numFeatures + 1];

        for (int i = 0; i < numFeatures; i++) {
            double[] colData = new double[numSamples];
            for (int j = 0; j < numSamples; j++) {
                colData[j] = features[j][i];
            }
            columns[i] = DoubleVector.of(colNames[i], colData);
        }

        int[] dummyLabels = new int[numSamples];
        columns[numFeatures] = IntVector.of(colNames[numFeatures], dummyLabels);

        return DataFrame.of(columns);
    }
}