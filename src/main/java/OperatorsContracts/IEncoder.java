package OperatorsContracts;

import Core._Chromosome;
import Core._Population;

public interface IEncoder<T> {

    _Population<T> createInitialPopulation(int populationSize, int chromosomeLength);

    _Chromosome<T> encode(Object solution);

    Object decode(_Chromosome<T> chromosome);
}
