package Operators;

import Core._Chromosome;
import OperatorsContracts.ICrossover;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover<T> implements ICrossover<T> {
    private double crossoverRate;
    private final Random random;

    public SinglePointCrossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.random = new Random();
    }

    public SinglePointCrossover(double crossoverRate, long seed) {
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
    }

    @Override
    public List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2) {
          /*TODO
              by Omar Hatem
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
