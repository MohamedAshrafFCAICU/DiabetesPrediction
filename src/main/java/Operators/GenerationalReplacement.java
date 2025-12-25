package Operators;

import Core._Population;
import OperatorsContracts.IReplacementStrategy;

public class GenerationalReplacement<T> implements IReplacementStrategy<T> {

    @Override
    public _Population<T> replace(_Population<T> currentPopulation, _Population<T> offspring) {
          /*TODO
              by Ahmed Kamel
          */
        return offspring;
    }
}
