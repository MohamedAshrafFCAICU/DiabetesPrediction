package Operators;

import Core._Chromosome;
import Core._Population;
import OperatorsContracts.IReplacementStrategy;

import java.util.ArrayList;
import java.util.List;


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

          if(currentPopulation == null || offspring == null) {
              throw new IllegalArgumentException("Population and offspring cannot be null");
          }
          int populationSize=currentPopulation.getSize();

          if (populationSize < eliteCount) {
              throw new IllegalArgumentException("Population size cannot be less than elite count");
          }

          currentPopulation.sortByFitness();
          offspring.sortByFitness();

          List<_Chromosome<T>> elites = new ArrayList<>();
          for(int i=0; i<eliteCount; i++) {
              elites.add(currentPopulation.getChromosome(i));
          }

          List<_Chromosome<T>> newEliteGen = new ArrayList<>(elites);
          int remainingSize = populationSize - eliteCount;

          for(int i=0; i<remainingSize && i < offspring.getSize(); i++) {
              newEliteGen.add(offspring.getChromosome(i));
          }
          return new _Population<>(newEliteGen);
    }

    public int getEliteCount() {
        return eliteCount;
    }
}
