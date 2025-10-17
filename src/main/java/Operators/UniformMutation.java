package Operators;

import Core._Chromosome;
import OperatorsContracts.IMutator;

import java.util.Random;

public class UniformMutation implements IMutator<Double> {
    private double mutationRate;
    private final Random random;
    private final double minValue;
    private final double maxValue;

    public UniformMutation(double mutationRate, double minValue, double maxValue) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = new Random();
    }

    public UniformMutation(double mutationRate, double minValue, double maxValue, long seed) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = new Random(seed);
    }

    @Override
    public void mutate(_Chromosome<Double> chromosome) {
          /*TODO
              by Mohamed Ashraf
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
