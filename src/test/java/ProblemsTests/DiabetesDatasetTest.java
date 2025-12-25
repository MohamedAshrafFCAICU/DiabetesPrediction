package ProblemsTests;

import Problems.DiabetesDataset;
import Problems.DiabetesPatient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiabetesDatasetTest
{
     /* TODO BY
                Ahmed Kamel
     */

    private static final String CSV_PATH = "D:\\SoftComputing Project\\src\\main\\resources\\oversampled_dataset.csv";
    private DiabetesDataset dataset;

    @BeforeEach
    void setUp() throws IOException {
        dataset = DiabetesDataset.loadFromCsv(CSV_PATH);
    }

    @Test
    @DisplayName("Test dataset loads successfully")
    void testDatasetLoads() {
        assertNotNull(dataset, "Dataset should not be null");
        assertTrue(dataset.getSize() > 0, "Dataset should contain patients");
        System.out.println("Dataset size: " + dataset.getSize());
    }

    @Test
    @DisplayName("Test feature names are loaded")
    void testFeatureNames() {
        String[] featureNames = dataset.getFeatureNames();
        assertNotNull(featureNames, "Feature names should not be null");
        assertTrue(featureNames.length > 0, "Should have at least one feature");
        System.out.println("Number of features: " + featureNames.length);
    }

    @Test
    @DisplayName("Test patients have correct feature count")
    void testPatientFeatureCount() {
        List<DiabetesPatient> patients = dataset.getPatients();
        assertFalse(patients.isEmpty(), "Should have patients");

        int expectedFeatureCount = dataset.getFeatureCount();
        for (DiabetesPatient patient : patients) {
            assertEquals(expectedFeatureCount, patient.getFeatures().length,
                    "Patient should have correct number of features");
        }
    }

    @Test
    @DisplayName("Test dataset split with 80-20 ratio")
    void testSplit80_20() {
        DiabetesDataset.DatasetSplit split = dataset.split(0.8);

        assertNotNull(split.getTrainSet(), "Training set should not be null");
        assertNotNull(split.getTestSet(), "Test set should not be null");

        int trainSize = split.getTrainSet().size();
        int testSize = split.getTestSet().size();

        assertTrue(trainSize > 0, "Training set should not be empty");
        assertTrue(testSize > 0, "Test set should not be empty");
        assertEquals(dataset.getSize(), trainSize + testSize,
                "Train + test size should equal original dataset size");

        System.out.println("Train size: " + trainSize);
        System.out.println("Test size: " + testSize);
    }

    @Test
    @DisplayName("Test labels are binary (0 or 1)")
    void testLabelsBinary() {
        List<DiabetesPatient> patients = dataset.getPatients();

        for (DiabetesPatient patient : patients) {
            int label = patient.getLabel();
            assertTrue(label == 0 || label == 1,
                    "Label should be 0 or 1, got: " + label);
        }
    }
}
