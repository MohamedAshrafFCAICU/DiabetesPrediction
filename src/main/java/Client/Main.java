package Client;

import Config.GeneticOperators;
import Config.GeneticSettings;
import Core._Chromosome;
import Core._GeneticAlgorithm;
import Operators.*;
import OperatorsContracts.IEncoder;
import Problems.DiabetesDataset;
import Problems.DiabetesFitnessFunction;
import Validation.BinaryFeatureValidator;
import Validation.IntegerFeatureValidator;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String CSV_PATH =
            "C:\\Users\\Asus\\Desktop\\DiabetesPrediction\\src\\main\\resources\\oversampled_dataset.csv";

    public static void main(String[] args) {
        try {
            System.out.println(" Loading diabetes dataset from CSV...");
            DiabetesDataset dataset = DiabetesDataset.loadFromCsv(CSV_PATH);

            System.out.println("  Dataset loaded successfully!");
            System.out.println("  Total patients: " + dataset.getSize());
            System.out.println("  Features: " + dataset.getFeatureCount());
            System.out.println("  Feature names: " + String.join(", ", dataset.getFeatureNames()));
            System.out.println();

            System.out.println("  Splitting data (80% train, 20% test)...");
            DiabetesDataset.DatasetSplit split = dataset.split(0.8);

            System.out.println("  Data split complete");
            System.out.println("  Training samples: " + split.getTrainSet().size());
            System.out.println("  Test samples: " + split.getTestSet().size());
            System.out.println();

            runBinaryGA(split, dataset.getFeatureNames());

        } catch (IOException e) {
            System.err.println("   Error loading dataset: " + e.getMessage());
            System.err.println("   Please check the file path: " + CSV_PATH);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("   Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runBinaryGA(DiabetesDataset.DatasetSplit split,
                                             String[] featureNames) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BINARY REPRESENTATION [0,1,1,0,...]");
        System.out.println("=".repeat(70));
        System.out.println("Configuration:");
        System.out.println("  - Population: 50");
        System.out.println("  - Generations: 100");
        System.out.println("  - Trees: 50 (optimized)");
        System.out.println("  - Crossover: Single Point (0.8)");
        System.out.println("  - Mutation: Bit Flip (0.1)");
        System.out.println("  - Selection: Roulette Wheel");
        System.out.println("  - Replacement: Elitist (5 elites)");
        System.out.println("  - Min Features: 5");
        System.out.println("  - Max Features: 10");
        System.out.println("===============================================================================================================================================");
        System.out.println();

        GeneticSettings settings = new GeneticSettings.Builder()
                .populationSize(50)
                .chromosomeLength(featureNames.length)
                .crossoverRate(0.8)
                .mutationRate(0.1)
                .maxGenerations(100)
                .eliteCount(5)
                .verbose(true)
                .build();

        BinaryFeatureValidator validator = new BinaryFeatureValidator(5, 10);

        DiabetesFitnessFunction<Boolean> fitnessFunction = new DiabetesFitnessFunction<>(
                split.getTrainingFeatures(),
                split.getTrainingLabels(),
                split.getTestFeatures(),
                split.getTestLabels(),
                validator,
                0.01,
                50,
                featureNames.length
        );

        GeneticOperators<Boolean> operators = new GeneticOperators.Builder<Boolean>()
                .encoder(new BinaryEncoder(validator, 5, 10))
                .fitnessFunction(fitnessFunction)
                .selector(new RouletteWheelSelector<>())
                .crossover(new SinglePointCrossover<>(0.7))
                .mutator(new BitFlipMutation(0.1))
                .replacementStrategy(new ElitistReplacement<>(5))
                .build();

        System.out.println("  Starting Genetic Algorithm...");
        System.out.println();

        _GeneticAlgorithm<Boolean> ga = new _GeneticAlgorithm<>(settings, operators);

        long startTime = System.currentTimeMillis();
        _Chromosome<Boolean> best = ga.evolve();
        long endTime = System.currentTimeMillis();

        displayResults(best, operators.getEncoder(), featureNames, endTime - startTime);

        fitnessFunction.printCacheStats();
    }

    private static void runIntegerGA(DiabetesDataset.DatasetSplit split,
                                    String[] featureNames) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BINARY REPRESENTATION [0,1,1,0,...]");
        System.out.println("=".repeat(70));
        System.out.println("Configuration:");
        System.out.println("  - Population: 50");
        System.out.println("  - Generations: 100");
        System.out.println("  - Trees: 50 (optimized)");
        System.out.println("  - Crossover: Uniform (0.8)");
        System.out.println("  - Mutation: Integer (0.1)");
        System.out.println("  - Selection: Tournament");
        System.out.println("  - Replacement: Generational");
        System.out.println("  - Min Features: 6");
        System.out.println("  - Max Features: 6");
        System.out.println("===============================================================================================================================================");
        System.out.println();

        GeneticSettings settings = new GeneticSettings.Builder()
                .populationSize(50)
                .chromosomeLength(6)
                .crossoverRate(0.8)
                .mutationRate(0.1)
                .maxGenerations(10)
                .eliteCount(5)
                .verbose(true)
                .build();

        IntegerFeatureValidator validator = new IntegerFeatureValidator(1, 20, 6);

        DiabetesFitnessFunction fitnessFunction = new DiabetesFitnessFunction<>(
                split.getTrainingFeatures(),
                split.getTrainingLabels(),
                split.getTestFeatures(),
                split.getTestLabels(),
                validator,
                0.01,
                50,
                featureNames.length
        );

        GeneticOperators<Integer> operators = new GeneticOperators.Builder<Integer>()
                .encoder(new IntegerEncoder(validator, 1, 20))
                .fitnessFunction(fitnessFunction)
                .selector(new TournamentSelector(3))
                .crossover(new UniformCrossover(validator, 0.7))
                .mutator(new IntegerMutation(0.1, 1, 20, validator))
                .replacementStrategy(new GenerationalReplacement())
                .build();

        System.out.println("  Starting Genetic Algorithm...");
        System.out.println();

        _GeneticAlgorithm<Integer> ga = new _GeneticAlgorithm<>(settings, operators);

        long startTime = System.currentTimeMillis();
        _Chromosome<Integer> best = ga.evolve();
        long endTime = System.currentTimeMillis();

        displayResults(best, operators.getEncoder(), featureNames, endTime - startTime);

        fitnessFunction.printCacheStats();
    }

    private static <T> void displayResults(_Chromosome<T> chromosome,
                                           IEncoder<T> encoder,
                                           String[] featureNames,
                                           long executionTimeMs) {

        System.out.println("======================================FINAL RESULT==============================");

        long seconds = executionTimeMs / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        System.out.println();
        System.out.printf("   Total Runtime: %d min %d sec (%d ms)%n",
                minutes, remainingSeconds, executionTimeMs);
        System.out.printf("   Best Fitness: %.4f%n", chromosome.getFitness());
        System.out.println();

        List<Integer> selectedFeatures = (List<Integer>) encoder.decode(chromosome);

        System.out.println("  Selected Features: " + selectedFeatures.size() + " / " + featureNames.length);
        System.out.println();

        if (selectedFeatures.isEmpty()) {
            System.out.println("   No features selected!");
        } else {
            System.out.println("Selected Feature Indices and Names:");
            System.out.println("-----------------------------------------------------------------------------------------");
            for (int i = 0; i < selectedFeatures.size(); i++) {
                int featureIndex = selectedFeatures.get(i);
                System.out.printf("  %2d. Feature %2d: %-20s%n",
                        i + 1, featureIndex, featureNames[featureIndex]);
            }
            System.out.println("------------------------------------------------------------------------------------------");
        }

        System.out.println();
        System.out.println("  Chromosome Representation:");
        System.out.println("   " + chromosome);
        System.out.println();

        System.out.println("  Performance Analysis:");
        System.out.println("----------------------------------------------------------------------------------------------");


        System.out.println("  Status:  TOTAL TIME: " + executionTimeMs / 60000);


        double avgTimePerGen = executionTimeMs / 100.0;
        System.out.printf("   Avg time per generation: %.2f ms%n", avgTimePerGen);

        double estimatedPerChrom = avgTimePerGen / 50.0;
        System.out.printf("   Est. time per chromosome: %.2f ms%n", estimatedPerChrom);

    }
}