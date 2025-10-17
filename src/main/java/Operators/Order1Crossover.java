package Operators;

import Core._Chromosome;
import OperatorsContracts.ICrossover;
import Validation.IChromosomeValidator;

import java.util.List;
import java.util.Random;

public class Order1Crossover<T> implements ICrossover<T> {
    private double crossoverRate;
    private final Random random;
    private final IChromosomeValidator<T> validator;

    public Order1Crossover(double crossoverRate, IChromosomeValidator<T> validator) {
        this.crossoverRate = crossoverRate;
        this.random = new Random();
        this.validator = validator;
    }

    public Order1Crossover(double crossoverRate, long seed, IChromosomeValidator<T> validator) {
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
        this.validator = validator;
    }

    @Override
    public List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2){

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