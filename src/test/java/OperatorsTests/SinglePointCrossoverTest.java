package OperatorsTests;

import Core.*;
import Operators.BinaryEncoder;
import Operators.DoubleEncoder;
import Operators.IntegerEncoder;
import Operators.SinglePointCrossover;

import OperatorsContracts.IEncoder;
import Validation.BinaryFeatureValidator;
import Validation.DoubleFeatureValidator;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class SinglePointCrossoverTest {

    @Test
    public void testSinglePointCrossoverWithBinaryGenes() {

        IEncoder<Boolean> binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(3,7),3,7);

        _Population<Boolean> population = binaryEncoder.createInitialPopulation(2,10);


        SinglePointCrossover<Boolean> crossover = new SinglePointCrossover<>(.7);

        List<_Chromosome<Boolean>> offsprings = crossover.crossover(population.getChromosome(0), population.getChromosome(1));

        System.out.println("Parent 1: " + population.getChromosome(0));
        System.out.println("Parent 2: " + population.getChromosome(1));
        System.out.println("Child 1:  " + offsprings.get(0));
        System.out.println("Child 2:  " + offsprings.get(1));
        System.out.println();
    }

    @Test
    public void testSinglePointCrossoverWithIntegerGenes() {
        IEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(1,20,10),1,20);

        _Population<Integer> population = integerEncoder.createInitialPopulation(2,10);


        SinglePointCrossover<Integer> crossover = new SinglePointCrossover<>(.7);

        List<_Chromosome<Integer>> offsprings = crossover.crossover(population.getChromosome(0), population.getChromosome(1));

        System.out.println("Parent 1: " + population.getChromosome(0));
        System.out.println("Parent 2: " + population.getChromosome(1));
        System.out.println("Child 1:  " + offsprings.get(0));
        System.out.println("Child 2:  " + offsprings.get(1));
        System.out.println();
    }

    @Test
    public void testSinglePointCrossoverWithDoubleGenes() {
        IEncoder doubleEncoder = new DoubleEncoder(new DoubleFeatureValidator(0.0,0.999999),0.0,0.999999);

        _Population<Double> population = doubleEncoder.createInitialPopulation(2,10);


        SinglePointCrossover<Double> crossover = new SinglePointCrossover<>(.7);

        List<_Chromosome<Double>> offsprings = crossover.crossover(population.getChromosome(0), population.getChromosome(1));

        System.out.println("Parent 1: " + population.getChromosome(0));
        System.out.println("Parent 2: " + population.getChromosome(1));
        System.out.println("Child 1:  " + offsprings.get(0));
        System.out.println("Child 2:  " + offsprings.get(1));
        System.out.println();
    }
}
