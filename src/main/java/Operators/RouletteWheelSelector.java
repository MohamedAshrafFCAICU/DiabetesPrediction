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

              DONE
        */

        if(population.getChromosomes().isEmpty())
            throw new IllegalArgumentException("Cannot select from empty population");

        double totalFitness = population.getTotalFitness();

        if(totalFitness == 0.0)
        {
            return population.getChromosomes().get(
                    random.nextInt(population.getChromosomes().size())
            );
        }


        double randomVal = random.nextDouble();
        double cumulativePercentage = 0.0;

        for (_Chromosome<T> chromosome : population.getChromosomes()) {

            double chromosomePercentage = chromosome.getFitness() / totalFitness;
            cumulativePercentage += chromosomePercentage;

            if(randomVal <= cumulativePercentage)
                return chromosome;

        }

        return population.getChromosomes().get(
                population.getChromosomes().size() - 1
        );
    }
}