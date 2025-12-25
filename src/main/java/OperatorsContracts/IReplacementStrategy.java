package OperatorsContracts;

import Core._Population;

public interface IReplacementStrategy<T> {

    _Population<T> replace(_Population<T> currentPopulation, _Population<T> offspring);
}