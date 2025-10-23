package Core;

import Config.GeneticOperators;
import Config.GeneticSettings;

import java.util.ArrayList;
import java.util.List;


public class _GeneticAlgorithm<T> {
    private final GeneticSettings settings;
    private final GeneticOperators<T> operators;

    private _Population<T> currentPopulation;
    private int currentGeneration;
    private _Chromosome<T> bestChromosome;
    private final List<Double> fitnessHistory;
    private final List<Double> avgFitnessHistory;

    public _GeneticAlgorithm(GeneticSettings settings, GeneticOperators<T> operators) {
        if (settings == null) {
            throw new IllegalArgumentException("Settings cannot be null");
        }
        if (operators == null) {
            throw new IllegalArgumentException("Operators cannot be null");
        }

        this.settings = settings;
        this.operators = operators;
        this.currentGeneration = 0;
        this.fitnessHistory = new ArrayList<>();
        this.avgFitnessHistory = new ArrayList<>();

        operators.getCrossover().setCrossoverRate(settings.getCrossoverRate());
        operators.getMutator().setMutationRate(settings.getMutationRate());
    }

    public _Chromosome<T> evolve() {
        initialize();

        while (!shouldTerminate()) {
            if (settings.isVerbose()) {
                printGenerationStats();
            }

            _Population<T> offspring = createOffspring();

            currentPopulation = operators.getReplacementStrategy()
                    .replace(currentPopulation, offspring);

            operators.getFitnessFunction().evaluate(currentPopulation);

            updateBestChromosome();

            currentGeneration++;
        }

        if (settings.isVerbose()) {
            printFinalStats();
        }

        return bestChromosome;
    }

    private void initialize() {
        System.out.println("🚀 Initializing population...");

        currentPopulation = operators.getEncoder().createInitialPopulation(
                settings.getPopulationSize(),
                settings.getChromosomeLength()
        );

        operators.getFitnessFunction().evaluate(currentPopulation);
        updateBestChromosome();
        currentGeneration = 0;

        System.out.println("✓ Initial population created");
        System.out.println("  Population size: " + currentPopulation.getSize());
        System.out.println("  Chromosome length: " + settings.getChromosomeLength());
        System.out.println();
    }

    private _Population<T> createOffspring() {
        _Population<T> offspring = new _Population<>(settings.getPopulationSize());

        while (offspring.getSize() < settings.getPopulationSize()) {
            _Chromosome<T> parent1 = operators.getSelector().select(currentPopulation);
            _Chromosome<T> parent2 = operators.getSelector().select(currentPopulation);

            List<_Chromosome<T>> children = operators.getCrossover()
                    .crossover(parent1, parent2);

            for (_Chromosome<T> child : children) {
                operators.getMutator().mutate(child);
                offspring.addChromosome(child);

                if (offspring.getSize() >= settings.getPopulationSize()) {
                    break;
                }
            }
        }

        return offspring;
    }

    private boolean shouldTerminate() {
        if (currentGeneration >= settings.getMaxGenerations()) {
            return true;
        }

        if (bestChromosome != null &&
                bestChromosome.getFitness() >= settings.getTargetFitness()) {
            return true;
        }

        return false;
    }

    private void updateBestChromosome() {
        _Chromosome<T> currentBest = currentPopulation.getBestChromosome();

        if (bestChromosome == null ||
                currentBest.getFitness() > bestChromosome.getFitness()) {
            bestChromosome = currentBest.copy();
        }

        fitnessHistory.add(currentBest.getFitness());
        avgFitnessHistory.add(currentPopulation.getAverageFitness());
    }

    private void printGenerationStats() {
        double avgFitness = currentPopulation.getAverageFitness();
        double bestFitness = currentPopulation.getBestChromosome().getFitness();
        double worstFitness = currentPopulation.getWorstChromosome().getFitness();

        System.out.printf("Gen %3d | Best: %8.4f | Avg: %8.4f | Worst: %8.4f%n",
                currentGeneration, bestFitness, avgFitness, worstFitness);
    }

    private void printFinalStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EVOLUTION COMPLETE!");
        System.out.println("=".repeat(60));
        System.out.println("Total Generations: " + currentGeneration);
        System.out.printf("Best Fitness: %.4f%n", bestChromosome.getFitness());
        System.out.println("Best Solution: " + operators.getEncoder().decode(bestChromosome));
        System.out.println("=".repeat(60) + "\n");
    }

    public _Population<T> getCurrentPopulation() {
        return currentPopulation;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public _Chromosome<T> getBestChromosome() {
        return bestChromosome;
    }

    public List<Double> getFitnessHistory() {
        return new ArrayList<>(fitnessHistory);
    }

    public List<Double> getAvgFitnessHistory() {
        return new ArrayList<>(avgFitnessHistory);
    }
}