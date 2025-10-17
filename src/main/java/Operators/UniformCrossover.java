package Operators;

import Core._Chromosome;
import OperatorsContracts.ICrossover;

import java.util.List;
import java.util.Random;


public class UniformCrossover<T> implements ICrossover<T> {
    private double crossoverRate;
    private final Random random;

    public UniformCrossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.random = new Random();
    }

    public UniformCrossover(double crossoverRate, long seed) {
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
    }

    @Override
    public List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2) {
        /*TODO
              by Anas Mahmoud
        */

        return  null;
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
