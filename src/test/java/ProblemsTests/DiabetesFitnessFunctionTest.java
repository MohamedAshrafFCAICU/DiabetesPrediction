package ProblemsTests;

import Core.BinaryGene;
import Core._Chromosome;
import Core._Gene;
import Problems.DiabetesDataset;
import Problems.DiabetesFitnessFunction;
import Validation.IChromosomeValidator;
import Validation.ValidationResult;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for optimized DiabetesFitnessFunction
 * Tests include: basic functionality, thread safety, caching, and performance
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiabetesFitnessFunctionTest {

    /* TODO BY Ahmed Kamel */

    private DiabetesFitnessFunction<Boolean> fitnessFunction;
    private double[][] X_train;
    private int[] y_train;
    private double[][] X_test;
    private int[] y_test;
    private int totalFeatures;
    private IChromosomeValidator<Boolean> validator;

    private static final double FEATURE_PENALTY = 0.01;
    private static final int NUM_TREES = 100; // Optimized from 500
    private static final String CSV_PATH =
            "C:\\Users\\Asus\\Desktop\\DiabetesPrediction\\src\\main\\resources\\oversampled_dataset.csv";

    @BeforeEach
    void setUp() throws IOException {
        System.out.println("\n🔧 Setting up test environment...");

        // Load dataset
        DiabetesDataset dataset = DiabetesDataset.loadFromCsv(CSV_PATH);
        DiabetesDataset.DatasetSplit split = dataset.split(0.8);

        X_train = split.getTrainingFeatures();
        y_train = split.getTrainingLabels();
        X_test = split.getTestFeatures();
        y_test = split.getTestLabels();
        totalFeatures = dataset.getFeatureCount();

        System.out.println("   Training samples: " + X_train.length);
        System.out.println("   Test samples: " + X_test.length);
        System.out.println("   Total features: " + totalFeatures);

        // Create validator with feature count constraints
        validator = new IChromosomeValidator<Boolean>() {
            @Override
            public boolean isValid(_Chromosome<Boolean> chromosome) {
                if (chromosome == null || chromosome.getGenes() == null) {
                    return false;
                }

                int geneCount = chromosome.getGenes().size();
                if (geneCount != totalFeatures) {
                    return false;
                }

                // Count selected features
                int selectedCount = 0;
                for (_Gene<Boolean> gene : chromosome.getGenes()) {
                    if (gene.getValue()) {
                        selectedCount++;
                    }
                }

                // Must have 5-10 features selected
                return selectedCount >= 5 && selectedCount <= 10;
            }

            @Override
            public ValidationResult validate(_Chromosome<Boolean> chromosome) {
                if (chromosome == null) {
                    return ValidationResult.invalid("Chromosome is null");
                }
                if (chromosome.getGenes() == null) {
                    return ValidationResult.invalid("Genes are null");
                }

                int geneCount = chromosome.getGenes().size();
                if (geneCount != totalFeatures) {
                    return ValidationResult.invalid(
                            "Expected " + totalFeatures + " genes, got " + geneCount
                    );
                }

                int selectedCount = 0;
                for (_Gene<Boolean> gene : chromosome.getGenes()) {
                    if (gene.getValue()) {
                        selectedCount++;
                    }
                }

                if (selectedCount < 5) {
                    return ValidationResult.invalid(
                            "Too few features selected: " + selectedCount
                    );
                }
                if (selectedCount > 10) {
                    return ValidationResult.invalid(
                            "Too many features selected: " + selectedCount
                    );
                }

                return ValidationResult.valid();
            }
        };

        // Create OPTIMIZED fitness function (new constructor)
        fitnessFunction = new DiabetesFitnessFunction<>(
                X_train,
                y_train,
                X_test,
                y_test,
                validator,
                FEATURE_PENALTY,
                NUM_TREES,        // Pass numTrees instead of model instance
                totalFeatures
        );

        System.out.println("✓ Setup complete\n");
    }

    @AfterEach
    void tearDown() {
        if (fitnessFunction != null) {
            fitnessFunction.clearCache();
        }
    }

    // ===================================================================
    // BASIC FUNCTIONALITY TESTS
    // ===================================================================

    @Test
    @Order(1)
    @DisplayName("Test fitness with valid feature selection (7 features)")
    void testValidFeatureSelection() {
        System.out.println("🧪 Test: Valid feature selection");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(7);

        long startTime = System.currentTimeMillis();
        double fitness = fitnessFunction.calculate(chromosome);
        long duration = System.currentTimeMillis() - startTime;

        assertTrue(fitness > 0.0, "Fitness should be positive for valid chromosome");
        System.out.printf("   ✓ Fitness: %.4f (took %d ms)%n", fitness, duration);

        // Fitness should be reasonable (between 0 and 1)
        assertTrue(fitness <= 1.0, "Fitness should not exceed 1.0");
    }

    @Test
    @Order(2)
    @DisplayName("Test fitness with minimum features (5)")
    void testMinimumFeatures() {
        System.out.println("🧪 Test: Minimum features");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(5);
        double fitness = fitnessFunction.calculate(chromosome);

        assertTrue(fitness > 0.0, "Fitness should be positive with 5 features");
        System.out.printf("   ✓ Fitness with 5 features: %.4f%n", fitness);
    }

    @Test
    @Order(3)
    @DisplayName("Test fitness with maximum features (10)")
    void testMaximumFeatures() {
        System.out.println("🧪 Test: Maximum features");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(10);
        double fitness = fitnessFunction.calculate(chromosome);

        assertTrue(fitness > 0.0, "Fitness should be positive with 10 features");
        System.out.printf("   ✓ Fitness with 10 features: %.4f%n", fitness);
    }

    @Test
    @Order(4)
    @DisplayName("Test fitness with too few features (returns 0)")
    void testTooFewFeatures() {
        System.out.println("🧪 Test: Too few features");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(3);
        double fitness = fitnessFunction.calculate(chromosome);

        assertEquals(0.0, fitness, 0.001,
                "Fitness should be 0 with too few features");
        System.out.println("   ✓ Correctly returns 0 for invalid chromosome");
    }

    @Test
    @Order(5)
    @DisplayName("Test fitness with too many features (returns 0)")
    void testTooManyFeatures() {
        System.out.println("🧪 Test: Too many features");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(15);
        double fitness = fitnessFunction.calculate(chromosome);

        assertEquals(0.0, fitness, 0.001,
                "Fitness should be 0 with too many features");
        System.out.println("   ✓ Correctly returns 0 for invalid chromosome");
    }

    @Test
    @Order(6)
    @DisplayName("Test fitness with no features selected")
    void testNoFeaturesSelected() {
        System.out.println("🧪 Test: No features selected");

        _Chromosome<Boolean> chromosome = createChromosome(totalFeatures, false);
        double fitness = fitnessFunction.calculate(chromosome);

        assertEquals(0.0, fitness, 0.001,
                "Fitness should be 0 with no features");
        System.out.println("   ✓ Correctly handles empty selection");
    }

    // ===================================================================
    // CACHING TESTS
    // ===================================================================

    @Test
    @Order(7)
    @DisplayName("Test fitness caching (same chromosome evaluated twice)")
    void testFitnessCaching() {
        System.out.println("🧪 Test: Fitness caching");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(7);

        // First evaluation (cache miss)
        long startTime1 = System.currentTimeMillis();
        double fitness1 = fitnessFunction.calculate(chromosome);
        long duration1 = System.currentTimeMillis() - startTime1;

        // Second evaluation (should hit cache)
        long startTime2 = System.currentTimeMillis();
        double fitness2 = fitnessFunction.calculate(chromosome);
        long duration2 = System.currentTimeMillis() - startTime2;

        assertEquals(fitness1, fitness2, 0.0001,
                "Cached fitness should match original");

        System.out.printf("   ✓ First eval: %.4f (took %d ms)%n", fitness1, duration1);
        System.out.printf("   ✓ Cached eval: %.4f (took %d ms)%n", fitness2, duration2);
        System.out.printf("   ✓ Speedup: %.2fx%n", (double) duration1 / duration2);

        // Second evaluation should be much faster
        assertTrue(duration2 < duration1,
                "Cached evaluation should be faster");
    }

    @Test
    @Order(8)
    @DisplayName("Test cache with different chromosomes")
    void testCacheWithDifferentChromosomes() {
        System.out.println("🧪 Test: Cache with different chromosomes");

        _Chromosome<Boolean> chrom1 = createChromosomeWithNFeatures(5);
        _Chromosome<Boolean> chrom2 = createChromosomeWithNFeatures(7);
        _Chromosome<Boolean> chrom3 = createChromosomeWithNFeatures(5); // Same as chrom1

        double fitness1 = fitnessFunction.calculate(chrom1);
        double fitness2 = fitnessFunction.calculate(chrom2);
        double fitness3 = fitnessFunction.calculate(chrom3);

        assertEquals(fitness1, fitness3, 0.0001,
                "Identical chromosomes should have same fitness");
        assertNotEquals(fitness1, fitness2,
                "Different chromosomes should have different fitness");

        System.out.printf("   ✓ Chrom1 (5 feat): %.4f%n", fitness1);
        System.out.printf("   ✓ Chrom2 (7 feat): %.4f%n", fitness2);
        System.out.printf("   ✓ Chrom3 (5 feat): %.4f%n", fitness3);

        fitnessFunction.printCacheStats();
    }

    // ===================================================================
    // THREAD SAFETY TESTS
    // ===================================================================

    @Test
    @Order(9)
    @DisplayName("Test parallel fitness evaluation (thread safety)")
    void testParallelEvaluation() throws InterruptedException, ExecutionException {
        System.out.println("🧪 Test: Parallel evaluation");

        int numChromosomes = 20;
        List<_Chromosome<Boolean>> chromosomes = new ArrayList<>();

        // Create diverse chromosomes
        for (int i = 0; i < numChromosomes; i++) {
            int numFeatures = 5 + (i % 6); // 5 to 10 features
            chromosomes.add(createChromosomeWithNFeatures(numFeatures));
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<Double>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // Submit all evaluations in parallel
        for (_Chromosome<Boolean> chromosome : chromosomes) {
            Future<Double> future = executor.submit(() ->
                    fitnessFunction.calculate(chromosome)
            );
            futures.add(future);
        }

        // Collect results
        List<Double> fitnesses = new ArrayList<>();
        for (Future<Double> future : futures) {
            fitnesses.add(future.get());
        }

        long duration = System.currentTimeMillis() - startTime;
        executor.shutdown();

        // Verify all evaluations succeeded
        assertEquals(numChromosomes, fitnesses.size(),
                "All chromosomes should be evaluated");

        for (Double fitness : fitnesses) {
            assertNotNull(fitness, "Fitness should not be null");
            assertTrue(fitness >= 0.0, "Fitness should be non-negative");
        }

        System.out.printf("   ✓ Evaluated %d chromosomes in parallel%n", numChromosomes);
        System.out.printf("   ✓ Total time: %d ms%n", duration);
        System.out.printf("   ✓ Avg per chromosome: %.2f ms%n",
                (double) duration / numChromosomes);
    }

    @Test
    @Order(10)
    @DisplayName("Test concurrent access to same chromosome")
    void testConcurrentAccessSameChromosome() throws InterruptedException {
        System.out.println("🧪 Test: Concurrent access to same chromosome");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(7);
        int numThreads = 10;
        CountDownLatch latch = new CountDownLatch(numThreads);

        List<Double> results = new CopyOnWriteArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    double fitness = fitnessFunction.calculate(chromosome);
                    results.add(fitness);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(numThreads, results.size(),
                "All threads should complete");

        // All results should be identical (same chromosome)
        double firstResult = results.get(0);
        for (Double result : results) {
            assertEquals(firstResult, result, 0.0001,
                    "All concurrent evaluations should return same fitness");
        }

        System.out.printf("   ✓ All %d threads returned same fitness: %.4f%n",
                numThreads, firstResult);
    }

    // ===================================================================
    // PERFORMANCE TESTS
    // ===================================================================

    @Test
    @Order(11)
    @DisplayName("Test performance with 100 trees")
    void testPerformanceWith100Trees() {
        System.out.println("🧪 Test: Performance with 100 trees");

        _Chromosome<Boolean> chromosome = createChromosomeWithNFeatures(7);

        // Warm-up
        fitnessFunction.calculate(chromosome);
        fitnessFunction.clearCache();

        // Measure actual performance
        long startTime = System.currentTimeMillis();
        double fitness = fitnessFunction.calculate(chromosome);
        long duration = System.currentTimeMillis() - startTime;

        System.out.printf("   ✓ Fitness: %.4f%n", fitness);
        System.out.printf("   ✓ Evaluation time: %d ms%n", duration);

        // Should complete in reasonable time (< 5 seconds)
        assertTrue(duration < 5000,
                "Evaluation should complete in under 5 seconds");

        // For 50 pop × 100 gen = 5000 evaluations
        // Estimate total time
        long estimatedTotal = (duration * 5000) / 1000; // seconds
        System.out.printf("   📊 Estimated for 5000 evals: %d seconds (%.1f minutes)%n",
                estimatedTotal, estimatedTotal / 60.0);
    }

    @Test
    @Order(12)
    @DisplayName("Test batch evaluation performance")
    void testBatchEvaluationPerformance() {
        System.out.println("🧪 Test: Batch evaluation performance");

        int batchSize = 50; // Simulate one generation
        List<_Chromosome<Boolean>> batch = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            int numFeatures = 5 + (i % 6);
            batch.add(createChromosomeWithNFeatures(numFeatures));
        }

        long startTime = System.currentTimeMillis();

        for (_Chromosome<Boolean> chromosome : batch) {
            fitnessFunction.calculate(chromosome);
        }

        long duration = System.currentTimeMillis() - startTime;

        System.out.printf("   ✓ Evaluated %d chromosomes%n", batchSize);
        System.out.printf("   ✓ Total time: %d ms (%.2f seconds)%n",
                duration, duration / 1000.0);
        System.out.printf("   ✓ Avg per chromosome: %.2f ms%n",
                (double) duration / batchSize);

        // Estimate for 100 generations
        long estimatedFor100Gens = (duration * 100) / 1000; // seconds
        System.out.printf("   📊 Estimated for 100 generations: %d seconds (%.1f minutes)%n",
                estimatedFor100Gens, estimatedFor100Gens / 60.0);

        fitnessFunction.printCacheStats();
    }

    // ===================================================================
    // HELPER METHODS
    // ===================================================================

    /**
     * Create chromosome with all genes set to same value
     */
    private _Chromosome<Boolean> createChromosome(int size, boolean value) {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(new BinaryGene(value));
        }
        return new _Chromosome<>(genes);
    }

    /**
     * Create chromosome with exactly N features selected
     */
    private _Chromosome<Boolean> createChromosomeWithNFeatures(int numFeatures) {
        List<_Gene<Boolean>> genes = new ArrayList<>();

        // Select first N features
        for (int i = 0; i < totalFeatures; i++) {
            genes.add(new BinaryGene(i < numFeatures));
        }

        return new _Chromosome<>(genes);
    }

    /**
     * Create chromosome with random feature selection
     */
    private _Chromosome<Boolean> createRandomChromosome(int minFeatures, int maxFeatures) {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < totalFeatures; i++) {
            indices.add(i);
        }

        java.util.Collections.shuffle(indices);

        int numToSelect = minFeatures +
                (int) (Math.random() * (maxFeatures - minFeatures + 1));

        for (int i = 0; i < totalFeatures; i++) {
            genes.add(new BinaryGene(indices.get(i) < numToSelect));
        }

        return new _Chromosome<>(genes);
    }
}