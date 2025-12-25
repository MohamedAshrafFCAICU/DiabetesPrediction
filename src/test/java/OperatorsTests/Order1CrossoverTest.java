package OperatorsTests;

import Core._Chromosome;
import Core._Population;
import Operators.IntegerEncoder;
import Operators.Order1Crossover;
import OperatorsContracts.IEncoder;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Order1CrossoverTest {

    @Test
    public void testOrder1CrossoverWithIntegerGenes()
    {
        IEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(1,20,8),1,20);

        _Population<Integer> population = integerEncoder.createInitialPopulation(2,8);


        Order1Crossover<Integer> crossover = new Order1Crossover<>(.7,new IntegerFeatureValidator(1,20,5));

        List<_Chromosome<Integer>> offsprings = crossover.crossover(population.getChromosome(0), population.getChromosome(1));

        System.out.println("Parent 1: " + population.getChromosome(0));
        System.out.println("Parent 2: " + population.getChromosome(1));
        System.out.println("Child 1:  " + offsprings.get(0));
        System.out.println("Child 2:  " + offsprings.get(1));
        System.out.println();
    }


}