package Problems;

import Core._Chromosome;
import Core._Gene;
import ML.SimpleRandomForest;
import OperatorsContracts.IFitnessFunction;
import Validation.IChromosomeValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DiabetesFitnessFunction<T> implements IFitnessFunction<T> {

    private final double[][] X_train_sampled;
    private final int[] y_train_sampled;
    private final double[][] X_test_sampled;
    private final int[] y_test_sampled;
    private final IChromosomeValidator<T> validator;
    private final double featurePenalty;
    private final int totalFeatures;
    private final int numTrees;

    private final Map<String, Double> fitnessCache;

    private final ThreadLocal<SimpleRandomForest> threadLocalModel;

    private int cacheHits = 0;
    private int cacheMisses = 0;

    private static final int MAX_TRAIN_SAMPLES = 5000;
    private static final int MAX_TEST_SAMPLES = 500;

    public DiabetesFitnessFunction(double[][] X_train, int[] y_train,
                                   double[][] X_test, int[] y_test,
                                   IChromosomeValidator<T> validator,
                                   double featurePenalty,
                                   int numTrees,
                                   int totalFeatures) {

        if (X_train.length > MAX_TRAIN_SAMPLES) {
            System.out.println("  Performing stratified sampling on training data: " + X_train.length + " → " + MAX_TRAIN_SAMPLES);
            int[][] stratifiedIndices = stratifiedSample(y_train, MAX_TRAIN_SAMPLES);
            this.X_train_sampled = new double[stratifiedIndices.length][];
            this.y_train_sampled = new int[stratifiedIndices.length];
            for (int i = 0; i < stratifiedIndices.length; i++) {
                this.X_train_sampled[i] = X_train[stratifiedIndices[i][0]];
                this.y_train_sampled[i] = stratifiedIndices[i][1];
            }
            printClassDistribution("Train", y_train_sampled);
        } else {
            this.X_train_sampled = X_train;
            this.y_train_sampled = y_train;
        }

        if (X_test.length > MAX_TEST_SAMPLES) {
            System.out.println("  Performing stratified sampling on test data: " + X_test.length + " → " + MAX_TEST_SAMPLES);
            int[][] stratifiedIndices = stratifiedSample(y_test, MAX_TEST_SAMPLES);
            this.X_test_sampled = new double[stratifiedIndices.length][];
            this.y_test_sampled = new int[stratifiedIndices.length];
            for (int i = 0; i < stratifiedIndices.length; i++) {
                this.X_test_sampled[i] = X_test[stratifiedIndices[i][0]];
                this.y_test_sampled[i] = stratifiedIndices[i][1];
            }
            printClassDistribution("Test", y_test_sampled);
        } else {
            this.X_test_sampled = X_test;
            this.y_test_sampled = y_test;
        }

        this.validator = validator;
        this.featurePenalty = featurePenalty;
        this.numTrees = numTrees;
        this.totalFeatures = totalFeatures;

        this.fitnessCache = new ConcurrentHashMap<>();
        this.threadLocalModel = ThreadLocal.withInitial(() ->
                new SimpleRandomForest(numTrees, 10, 10)
        );

        System.out.println("  Fitness function initialized");
        System.out.println("  Train samples: " + X_train_sampled.length);
        System.out.println("  Test samples: " + X_test_sampled.length);
        System.out.println("  Trees per forest: " + numTrees);
    }

    private int[][] stratifiedSample(int[] labels, int targetSize) {
        // Separate indices by class
        List<Integer> class0Indices = new ArrayList<>();
        List<Integer> class1Indices = new ArrayList<>();

        for (int i = 0; i < labels.length; i++) {
            if (labels[i] == 0) {
                class0Indices.add(i);
            } else {
                class1Indices.add(i);
            }
        }

        // Calculate samples per class (equal distribution)
        int samplesPerClass = targetSize / 2;

        // Shuffle and sample from each class
        Collections.shuffle(class0Indices, ThreadLocalRandom.current());
        Collections.shuffle(class1Indices, ThreadLocalRandom.current());

        List<Integer> sampledClass0 = class0Indices.subList(0, Math.min(samplesPerClass, class0Indices.size()));
        List<Integer> sampledClass1 = class1Indices.subList(0, Math.min(samplesPerClass, class1Indices.size()));

        // Combine and create result array
        int totalSampled = sampledClass0.size() + sampledClass1.size();
        int[][] result = new int[totalSampled][2];

        int idx = 0;
        for (int i : sampledClass0) {
            result[idx][0] = i;  // original index
            result[idx][1] = 0;  // label
            idx++;
        }
        for (int i : sampledClass1) {
            result[idx][0] = i;  // original index
            result[idx][1] = 1;  // label
            idx++;
        }

        // Shuffle combined result to mix classes
        shuffleArray(result);

        return result;
    }

    /**
     * Fisher-Yates shuffle for 2D array
     */
    private void shuffleArray(int[][] array) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Prints class distribution for verification
     */
    private void printClassDistribution(String datasetName, int[] labels) {
        int class0 = 0, class1 = 0;
        for (int label : labels) {
            if (label == 0) class0++;
            else class1++;
        }
        double ratio = class0 > 0 ? (double) class1 / class0 : 0;
        System.out.printf("  %s class distribution - Class 0: %d, Class 1: %d (ratio: %.2f)\n",
                datasetName, class0, class1, ratio);
    }

    @Override
    public double calculate(_Chromosome<T> chromosome) {
        String key = getChromosomeKey(chromosome);
        Double cached = fitnessCache.get(key);
        if (cached != null) {
            cacheHits++;
            return cached;
        }

        cacheMisses++;

        if (!validator.isValid(chromosome)) {
            fitnessCache.put(key, 0.0);
            return 0.0;
        }

        List<Integer> selectedFeatures = extractSelectedFeatures(chromosome);
        if (selectedFeatures.isEmpty() || selectedFeatures.size() < 5) {
            fitnessCache.put(key, 0.0);
            return 0.0;
        }

        try {
            SimpleRandomForest model = threadLocalModel.get();

            double[][] X_train_sel = extractColumns(X_train_sampled, selectedFeatures);
            double[][] X_test_sel = extractColumns(X_test_sampled, selectedFeatures);

            model.train(X_train_sel, y_train_sampled);
            double accuracy = model.accuracy(X_test_sel, y_test_sampled);

            double featureRatio = (double) selectedFeatures.size() / totalFeatures;
            double penalty = featurePenalty * featureRatio;
            double fitness = Math.max(0.0, accuracy - penalty);

            fitnessCache.put(key, fitness);
            return fitness;

        } catch (Exception e) {
            System.err.println(" Fitness error: " + e.getMessage());
            e.printStackTrace();
            fitnessCache.put(key, 0.0);
            return 0.0;
        }
    }

    private String getChromosomeKey(_Chromosome<T> chromosome) {
        StringBuilder key = new StringBuilder();
        for (_Gene<T> gene : chromosome.getGenes()) {
            T value = gene.getValue();
            key.append(value instanceof Boolean ? ((Boolean) value ? "1" : "0") : value.toString());
        }
        return key.toString();
    }

    private List<Integer> extractSelectedFeatures(_Chromosome<T> chromosome) {
        List<Integer> selected = new ArrayList<>();
        List<_Gene<T>> genes = chromosome.getGenes();

        if (genes.isEmpty()) {
            return selected;
        }

        T firstValue = genes.get(0).getValue();

        if (firstValue instanceof Boolean) {
            for (int i = 0; i < genes.size(); i++) {
                T value = genes.get(i).getValue();
                if ((Boolean) value) {
                    selected.add(i);
                }
            }

        } else if (firstValue instanceof Integer) {
            for (int i = 0; i < genes.size(); i++) {
                T value = genes.get(i).getValue();
                selected.add((Integer) value - 1);
            }

        } else if (firstValue instanceof Double) {
            double threshold = 0.5;
            for (int i = 0; i < genes.size(); i++) {
                T value = genes.get(i).getValue();
                if ((Double) value > threshold) {
                    selected.add(i);
                }
            }

        } else {
            throw new IllegalArgumentException(
                    "Unsupported gene type: " + firstValue.getClass().getName()
            );
        }

        return selected;
    }

    private double[][] extractColumns(double[][] data, List<Integer> columns) {
        int numRows = data.length;
        int numCols = columns.size();
        double[][] result = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i][j] = data[i][columns.get(j)];
            }
        }
        return result;
    }

    public void printCacheStats() {
        int total = cacheHits + cacheMisses;
        double hitRate = total > 0 ? (cacheHits * 100.0 / total) : 0.0;
        System.out.println("\n Cache Statistics:");
        System.out.printf("  Hits: %d, Misses: %d, Rate: %.1f%%\n",
                cacheHits, cacheMisses, hitRate);
    }

    public void clearCache() {
        fitnessCache.clear();
        cacheHits = 0;
        cacheMisses = 0;
    }
}
