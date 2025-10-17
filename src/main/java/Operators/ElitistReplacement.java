package Operators;

import Core._Population;
import OperatorsContracts.IReplacementStrategy;


public class ElitistReplacement<T> implements IReplacementStrategy<T> {
    private final int eliteCount;

    public ElitistReplacement(int eliteCount) {
        if (eliteCount < 0) {
            throw new IllegalArgumentException("Elite count cannot be negative");
        }
        this.eliteCount = eliteCount;
    }

    @Override
    public _Population<T> replace(_Population<T> currentPopulation, _Population<T> offspring) {

          /*TODO
              by Husam Abozide
          */

        return  null;
    }

    public int getEliteCount() {
        return eliteCount;
    }
}
