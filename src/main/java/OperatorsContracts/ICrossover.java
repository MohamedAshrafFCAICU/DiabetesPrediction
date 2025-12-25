package OperatorsContracts;

import Core._Chromosome;

import java.util.List;

public interface ICrossover<T> {


    List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2);

    void setCrossoverRate(double rate);

    double getCrossoverRate();
}
