package Operators;

import Core._Chromosome;
import OperatorsContracts.IMutator;
import Validation.IChromosomeValidator;

import java.util.*;


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
         /*TODO
              by Husam Abozid
        */
    }

    @Override
    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    @Override
    public double getMutationRate() {
        return mutationRate;
    }
}
