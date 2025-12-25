package MLModelsTests;

import ML.SimpleRandomForest;
import Problems.DiabetesDataset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleRandomForestTest
{
     /* TODO BY
                Ahmed Kamel
     */

    private static final String CSV_FILE_PATH = "D:\\SoftComputing Project\\src\\main\\resources\\oversampled_dataset.csv";
    private static DiabetesDataset dataset;
    private static DiabetesDataset.DatasetSplit split;

    @BeforeAll
    public static void setUpOnce() throws IOException {
        dataset = DiabetesDataset.loadFromCsv(CSV_FILE_PATH);
        split = dataset.split(0.8); // 80% train, 20% test

        System.out.println("=== Dataset Information ===");
        System.out.println("Total samples: " + dataset.getSize());
        System.out.println("Number of features: " + dataset.getFeatureCount());
        System.out.println("Training samples: " + split.getTrainSet().size());
        System.out.println("Test samples: " + split.getTestSet().size());
        System.out.print("Feature names: ");
        for (String name : dataset.getFeatureNames()) {
            System.out.print(name + " ");
        }
        System.out.println("\n");
    }

    @Test
    public void testDatasetLoading() {
        assertNotNull(dataset, "Dataset should be loaded");
        assertTrue(dataset.getSize() > 0, "Dataset should have samples");
        assertTrue(dataset.getFeatureCount() > 0, "Dataset should have features");
    }

    @Test
    public void testDatasetSplit() {
        assertNotNull(split, "Split should not be null");
        assertTrue(split.getTrainSet().size() > 0, "Train set should not be empty");
        assertTrue(split.getTestSet().size() > 0, "Test set should not be empty");

        int totalSize = split.getTrainSet().size() + split.getTestSet().size();
        assertEquals(dataset.getSize(), totalSize, "Split sizes should sum to total dataset size");
    }

    @Test
    public void testFeatureExtraction() {
        double[][] trainFeatures = split.getTrainingFeatures();
        int[] trainLabels = split.getTrainingLabels();

        assertEquals(split.getTrainSet().size(), trainFeatures.length,
                "Train features length should match train set size");
        assertEquals(split.getTrainSet().size(), trainLabels.length,
                "Train labels length should match train set size");
        assertEquals(dataset.getFeatureCount(), trainFeatures[0].length,
                "Feature count should match");

        double[][] testFeatures = split.getTestFeatures();
        int[] testLabels = split.getTestLabels();

        assertEquals(split.getTestSet().size(), testFeatures.length,
                "Test features length should match test set size");
        assertEquals(split.getTestSet().size(), testLabels.length,
                "Test labels length should match test set size");
    }

    @Test
    public void testLabelDistribution() {
        int[] trainLabels = split.getTrainingLabels();
        int[] testLabels = split.getTestLabels();

        int trainPositive = 0, trainNegative = 0;
        for (int label : trainLabels) {
            if (label == 1) trainPositive++;
            else trainNegative++;
        }

        int testPositive = 0, testNegative = 0;
        for (int label : testLabels) {
            if (label == 1) testPositive++;
            else testNegative++;
        }

        System.out.println("=== Label Distribution ===");
        System.out.println("Training set - Positive: " + trainPositive + ", Negative: " + trainNegative);
        System.out.println("Test set - Positive: " + testPositive + ", Negative: " + testNegative);

        assertTrue(trainPositive > 0 && trainNegative > 0,
                "Training set should have both positive and negative samples");
        assertTrue(testPositive > 0 && testNegative > 0,
                "Test set should have both positive and negative samples");
    }

    @Test
    public void testRandomForestTraining() {
        SimpleRandomForest model = new SimpleRandomForest(50);

        double[][] trainFeatures = split.getTrainingFeatures();
        int[] trainLabels = split.getTrainingLabels();

        assertDoesNotThrow(() -> model.train(trainFeatures, trainLabels),
                "Training should not throw exception");
    }

    @Test
    public void testRandomForestPrediction() {
        SimpleRandomForest model = new SimpleRandomForest(50);

        double[][] trainFeatures = split.getTrainingFeatures();
        int[] trainLabels = split.getTrainingLabels();
        double[][] testFeatures = split.getTestFeatures();

        model.train(trainFeatures, trainLabels);

        int[] predictions = model.predict(testFeatures);

        assertNotNull(predictions, "Predictions should not be null");
        assertEquals(testFeatures.length, predictions.length,
                "Predictions length should match test set size");

        for (int pred : predictions) {
            assertTrue(pred == 0 || pred == 1, "Prediction should be 0 or 1");
        }
    }

    @Test
    public void testRandomForestAccuracy() {
        SimpleRandomForest model = new SimpleRandomForest(100);

        double[][] trainFeatures = split.getTrainingFeatures();
        int[] trainLabels = split.getTrainingLabels();
        double[][] testFeatures = split.getTestFeatures();
        int[] testLabels = split.getTestLabels();

        model.train(trainFeatures, trainLabels);

        double trainAccuracy = model.accuracy(trainFeatures, trainLabels);
        double testAccuracy = model.accuracy(testFeatures, testLabels);

        System.out.println("=== Model Performance ===");
        System.out.println("Training Accuracy: " + String.format("%.4f", trainAccuracy));
        System.out.println("Test Accuracy: " + String.format("%.4f", testAccuracy));

        assertTrue(trainAccuracy > 0.5, "Training accuracy should be better than random");
        assertTrue(testAccuracy > 0.5, "Test accuracy should be better than random");
        assertTrue(trainAccuracy <= 1.0, "Training accuracy should not exceed 1.0");
        assertTrue(testAccuracy <= 1.0, "Test accuracy should not exceed 1.0");
    }
}
