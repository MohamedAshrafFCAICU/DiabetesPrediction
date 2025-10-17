package Operators;

import Core._Chromosome;
import OperatorsContracts.IMutator;

import java.util.Random;

public class SwapMutation<T> implements IMutator<T> {
    private double mutationRate;
    private final Random random;

    public SwapMutation(double mutationRate) {
        this.mutationRate = mutationRate;
        this.random = new Random();
    }

    public SwapMutation(double mutationRate, long seed) {
        this.mutationRate = mutationRate;
        this.random = new Random(seed);
    }

    @Override
    public void mutate(_Chromosome<T> chromosome) {
          /*TODO
              by Anas Mahmoud
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
