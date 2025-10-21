package ProblemsTests;

import Core.BinaryGene;
import Core._Chromosome;
import Core._Gene;
import ML.SimpleRandomForest;
import Problems.DiabetesDataset;
import Problems.DiabetesFitnessFunction;
import Validation.IChromosomeValidator;
import Validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiabetesFitnessFunctionTest
{
     /* TODO BY
                 Ahmed Kamel
     */

    private DiabetesFitnessFunction<Boolean> fitnessFunction;
    private double[][] X_train;
    private int[] y_train;
    private double[][] X_test;
    private int[] y_test;
    private int totalFeatures;
    private SimpleRandomForest model;
    private static final double FEATURE_PENALTY = 0.01;
    private static final String CSV_PATH = "D:\\SoftComputing Project\\src\\main\\resources\\oversampled_dataset.csv";

    @BeforeEach
    void setUp() throws IOException {
        // Load dataset
        DiabetesDataset dataset = DiabetesDataset.loadFromCsv(CSV_PATH);
        DiabetesDataset.DatasetSplit split = dataset.split(0.8);

        X_train = split.getTrainingFeatures();
        y_train = split.getTrainingLabels();
        X_test = split.getTestFeatures();
        y_test = split.getTestLabels();
        totalFeatures = dataset.getFeatureCount();

        model = new SimpleRandomForest(50);

        IChromosomeValidator<Boolean> validator = new IChromosomeValidator<Boolean>() {
            @Override
            public boolean isValid(_Chromosome<Boolean> chromosome) {
                if (chromosome == null || chromosome.getGenes() == null) {
                    return false;
                }
                return chromosome.getGenes().size() == totalFeatures;
            }

            @Override
            public ValidationResult validate(_Chromosome<Boolean> chromosome) {
                if (chromosome == null) {
                    return ValidationResult.invalid("Chromosome is null");
                }
                if (chromosome.getGenes() == null) {
                    return ValidationResult.invalid("Genes are null");
                }
                if (chromosome.getGenes().size() != totalFeatures) {
                    return ValidationResult.invalid(
                            "Expected " + totalFeatures + " genes, got " + chromosome.getGenes().size());
                }
                return ValidationResult.valid();
            }
        };

        fitnessFunction = new DiabetesFitnessFunction<>(
                X_train, y_train, X_test, y_test,
                validator, FEATURE_PENALTY, model, totalFeatures
        );
    }

    @Test
    @DisplayName("Test fitness calculation with all features selected")
    void testAllFeaturesSelected() {
        _Chromosome<Boolean> chromosome = createChromosome(totalFeatures, true);
        double fitness = fitnessFunction.calculate(chromosome);

        assertTrue(fitness >= 0.0, "Fitness should be non-negative");
        System.out.println("Fitness with all features: " + fitness);
    }

    @Test
    @DisplayName("Test fitness calculation with half features selected")
    void testHalfFeaturesSelected() {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < totalFeatures; i++) {
            genes.add(new BinaryGene(i % 2 == 0));
        }
        _Chromosome<Boolean> chromosome = new _Chromosome<>(genes);

        double fitness = fitnessFunction.calculate(chromosome);
        assertTrue(fitness >= 0.0, "Fitness should be non-negative");
        System.out.println("Fitness with half features: " + fitness);
    }

    @Test
    @DisplayName("Test fitness calculation with random feature selection")
    void testRandomFeaturesSelected() {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        int selectedCount = 0;
        for (int i = 0; i < totalFeatures; i++) {
            boolean selected = Math.random() > 0.5;
            genes.add(new BinaryGene(selected));
            if (selected) selectedCount++;
        }
        _Chromosome<Boolean> chromosome = new _Chromosome<>(genes);

        double fitness = fitnessFunction.calculate(chromosome);
        assertTrue(fitness >= 0.0, "Fitness should be non-negative");
        System.out.println("Fitness with " + selectedCount + " random features: " + fitness);
    }

    private _Chromosome<Boolean> createChromosome(int size, boolean value) {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(new BinaryGene(value));
        }
        return new _Chromosome<>(genes);
    }

}
