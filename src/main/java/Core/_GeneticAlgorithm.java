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

        // Configure operators with settings
        operators.getCrossover().setCrossoverRate(settings.getCrossoverRate());
        operators.getMutator().setMutationRate(settings.getMutationRate());
    }

}