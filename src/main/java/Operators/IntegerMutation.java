package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.IMutator;
import Validation.IChromosomeValidator;

import java.util.*;

/**
 * Integer Mutation with Duplicate Prevention
 * Replaces a gene with an unused value
 */
public class IntegerMutation implements IMutator<Integer> {
    private double mutationRate;
    private final Random random;
    private final int minValue;
    private final int maxValue;
    private final IChromosomeValidator<Integer> validator;

    public IntegerMutation(double mutationRate, int minValue, int maxValue,
                           IChromosomeValidator<Integer> validator) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.validator = validator;
        this.random = new Random();
    }

    @Override
    public void mutate(_Chromosome<Integer> chromosome) {
        if (chromosome == null) {
            throw new IllegalArgumentException("Chromosome cannot be null");
        }

        boolean mutated = false;

        for (int i = 0; i < chromosome.getLength(); i++) {
            if (random.nextDouble() < mutationRate) {
                // ✅ Get currently used values
                Set<Integer> usedValues = getCurrentValues(chromosome);

                // ✅ Find available values
                List<Integer> availableValues = new ArrayList<>();
                for (int val = minValue; val < maxValue; val++) {
                    if (!usedValues.contains(val)) {
                        availableValues.add(val);
                    }
                }

                // ✅ Only mutate if there are available values
                if (!availableValues.isEmpty()) {
                    _Gene<Integer> gene = chromosome.getGene(i);

                    // Remove current value from used set
                    usedValues.remove(gene.getValue());

                    // Select random available value
                    int newValue = availableValues.get(random.nextInt(availableValues.size()));
                    gene.setValue(newValue);

                    mutated = true;
                }
            }
        }

        if (mutated) {
            chromosome.invalidateFitness();
        }

        // ✅ Validate and repair
        if (validator != null && !validator.isValid(chromosome)) {
            boolean repaired = validator.repair(chromosome);
            if (!repaired && !validator.isValid(chromosome)) {
                throw new IllegalStateException(
                        "Chromosome is invalid and could not be repaired: " +
                                validator.validate(chromosome)
                );
            }
        }
    }

    /**
     * Get all values currently in the chromosome
     */
    private Set<Integer> getCurrentValues(_Chromosome<Integer> chromosome) {
        Set<Integer> values = new HashSet<>();
        for (int i = 0; i < chromosome.getLength(); i++) {
            values.add(chromosome.getGene(i).getValue());
        }
        return values;
    }

    @Override
    public void setMutationRate(double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0 and 1");
        }
        this.mutationRate = rate;
    }

    @Override
    public double getMutationRate() {
        return mutationRate;
    }
}