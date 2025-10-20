package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.ICrossover;
import Validation.IChromosomeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UniformCrossover<T> implements ICrossover<T> {
    private final IChromosomeValidator validator;
    private double crossoverRate;
    private final Random random;

    public UniformCrossover(IChromosomeValidator validator, double crossoverRate) {
        this.validator = validator;
        this.crossoverRate = crossoverRate;
        this.random = new Random();
    }

    public UniformCrossover(IChromosomeValidator validator, double crossoverRate, long seed) {
        this.validator = validator;
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
    }

    @Override
    public List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2) {
        /*TODO
              by Anas Mahmoud

              Done
        */

        if (parent1 == null || parent2 == null || parent1.getLength() == 0 || parent2.getLength() == 0) {
            throw new IllegalArgumentException("Parents should not be null or empty");
        }

        if (parent1.getLength() != parent2.getLength()) {
            throw new IllegalArgumentException("Parents must have the same length");
        }

        _Chromosome<T> offspring1;
        _Chromosome<T> offspring2;
        int maxAttempts = 100;
        int attempts = 0;

        do {
            offspring1 = parent1.copy();
            offspring2 = parent2.copy();

            if (random.nextDouble() <= crossoverRate) {
                for (int i = 0; i < parent1.getLength(); i++) {
                    if (random.nextBoolean()) {
                        _Gene<T> temp = offspring1.getGene(i);
                        offspring1.setGene(i, offspring2.getGene(i));
                        offspring2.setGene(i, temp);
                    }
                }
            }
            attempts++;
        } while ((!validator.isValid(offspring1) || !validator.isValid(offspring2))
                && attempts < maxAttempts);

        if (attempts >= maxAttempts) {
            throw new IllegalStateException("Could not generate valid offspring after " + maxAttempts + " attempts");
        }

        List<_Chromosome<T>> offsprings = new ArrayList<>();
        offsprings.add(offspring1);
        offsprings.add(offspring2);
        return offsprings;
    }

    @Override
    public void setCrossoverRate(double rate) {
        this.crossoverRate = rate;
    }

    @Override
    public double getCrossoverRate() {
        return crossoverRate;
    }
}
