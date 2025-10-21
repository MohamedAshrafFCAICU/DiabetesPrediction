package ML;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;

import java.util.Objects;


                    /*
                        TODO BY AHMED KAMEL
                    */

public class SimpleRandomForest {
    private RandomForest model;
    private int numTrees = 100;
    private int numFeatures = -1;


    public SimpleRandomForest() {
    }

    public SimpleRandomForest(int numTrees) {
        this.numTrees = numTrees;
    }


    public void train(double[][] features, int[] labels) {
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(labels, "labels must not be null");

        if (features.length == 0) throw new IllegalArgumentException("features must not be empty");
        if (features.length != labels.length)
            throw new IllegalArgumentException("features and labels must have the same length");

        this.numFeatures = features[0].length;

        DataFrame df = createDataFrame(features, labels);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("smile.random.forest.trees", String.valueOf(numTrees));

        this.model = RandomForest.fit(Formula.lhs("Diabetes"), df, props);
    }


    public int[] predict(double[][] features) {
        if (model == null) throw new IllegalStateException("Model not trained. Call train(...) first.");
        Objects.requireNonNull(features, "features must not be null");

        if (features.length == 0)
            throw new IllegalArgumentException("features must not be empty");
        if (features[0].length != numFeatures)
            throw new IllegalArgumentException(
                    "Feature count mismatch: expected " + numFeatures +
                            " but got " + features[0].length);

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
        if (features.length != actualLabels.length)
            throw new IllegalArgumentException("features and actualLabels must have the same length");

        int[] preds = predict(features);
        int correct = 0;
        for (int i = 0; i < preds.length; i++) {
            if (preds[i] == actualLabels[i]) correct++;
        }
        return (double) correct / preds.length;
    }

    private smile.data.DataFrame createDataFrame(double[][] features, int[] labels) {
        int numSamples = features.length;
        int numFeatures = features[0].length;

        String[] colNames = new String[numFeatures + 1];
        String[] featureNames = {"HighBP", "HighChol", "CholCheck", "BMI", "Smoker", "Stroke",
                "HeartDiseaseorAttack", "PhysActivity", "Fruits", "Veggies",
                "HvyAlcoholConsump", "AnyHealthcare", "NoDocbcCost", "GenHlth",
                "MentHlth", "PhysHlth", "DiffWalk", "Sex", "Age", "Education"};

        System.arraycopy(featureNames, 0, colNames, 0, numFeatures);
        colNames[numFeatures] = "Diabetes";

        double[][] featureData = new double[numSamples][numFeatures];
        int[] labelData = new int[numSamples];

        for (int i = 0; i < numSamples; i++) {
            System.arraycopy(features[i], 0, featureData[i], 0, numFeatures);
            labelData[i] = labels[i];
        }

        smile.data.vector.BaseVector[] columns = new smile.data.vector.BaseVector[numFeatures + 1];
        for (int i = 0; i < numFeatures; i++) {
            double[] colData = new double[numSamples];
            for (int j = 0; j < numSamples; j++) {
                colData[j] = featureData[j][i];
            }
            columns[i] = smile.data.vector.DoubleVector.of(colNames[i], colData);
        }
        columns[numFeatures] = smile.data.vector.IntVector.of(colNames[numFeatures], labelData);

        return DataFrame.of(columns);
    }


    private DataFrame createPredictionDataFrame(double[][] features) {
        int numSamples = features.length;
        int numFeatures = features[0].length;

        String[] featureNames = {"HighBP", "HighChol", "CholCheck", "BMI", "Smoker", "Stroke",
                "HeartDiseaseorAttack", "PhysActivity", "Fruits", "Veggies",
                "HvyAlcoholConsump", "AnyHealthcare", "NoDocbcCost", "GenHlth",
                "MentHlth", "PhysHlth", "DiffWalk", "Sex", "Age", "Education"};

        smile.data.vector.BaseVector[] columns = new smile.data.vector.BaseVector[numFeatures + 1];
        for (int i = 0; i < numFeatures; i++) {
            double[] colData = new double[numSamples];
            for (int j = 0; j < numSamples; j++) {
                colData[j] = features[j][i];
            }
            columns[i] = smile.data.vector.DoubleVector.of(featureNames[i], colData);
        }

        int[] dummyLabels = new int[numSamples];
        columns[numFeatures] = smile.data.vector.IntVector.of("Diabetes", dummyLabels);

        return DataFrame.of(columns);
    }
}