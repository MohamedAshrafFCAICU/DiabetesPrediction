package Operators;

import Core._Population;
import OperatorsContracts.IReplacementStrategy;



public class SteadyStateReplacement<T> implements IReplacementStrategy<T> {

    @Override
    public _Population<T> replace(_Population<T> currentPopulation, _Population<T> offspring) {
        /*TODO
              by Mohamed Ashraf

              DONE
        */
        int targetSize = currentPopulation.getSize();

        _Population combinedPopulation = new _Population(currentPopulation.getSize() + offspring.getSize());

        combinedPopulation.addChromosomes(currentPopulation.getChromosomes());
        combinedPopulation.addChromosomes(offspring.getChromosomes());

        combinedPopulation.sortByFitness();

        _Population newPopulation = new _Population(targetSize);
        for (int i = 0; i < targetSize; i++) {
            newPopulation.addChromosome(combinedPopulation.getChromosome(i));
        }
       return newPopulation;

    }
}
