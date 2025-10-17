package Operators;

import Core._Chromosome;
import Core._Population;
import OperatorsContracts.ISelector;

import java.util.Random;

public class RouletteWheelSelector<T> implements ISelector<T> {
    private final Random random;

    public RouletteWheelSelector() {
        this.random = new Random();
    }

    public RouletteWheelSelector(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public _Chromosome<T> select(_Population<T> population) {
        /*TODO
              by Mohamed Ashraf
        */

         return null;
    }
}