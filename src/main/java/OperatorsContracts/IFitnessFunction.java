package OperatorsContracts;

import Core._Chromosome;
import Core._Population;

public interface IFitnessFunction<T> {

    double calculate(_Chromosome<T> chromosome);

    default void evaluate(_Population<T> population) {
        population.getChromosomes().forEach(chromosome -> {
            if (!chromosome.isFitnessCalculated()) {
                double fitness = calculate(chromosome);
                chromosome.setFitness(fitness);
            }
        });
    }

    default boolean isMaximization() {
        return true;
    }
}
