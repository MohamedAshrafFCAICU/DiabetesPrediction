package OperatorsTests;

import Core._Chromosome;
import Core._Population;
import Operators.BinaryEncoder;
import Operators.IntegerEncoder;
import Operators.UniformCrossover;
import OperatorsContracts.ICrossover;
import OperatorsContracts.IEncoder;
import Validation.BinaryFeatureValidator;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UniformCrossoverTest
{
     /* TODO BY
                Anas Mahmoud
     */

    @Test
    public void testUniformCrossoverWithBinaryRepresentation(){

        IEncoder binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(1, 9), 1, 9);
        _Population chromosomes = binaryEncoder.createInitialPopulation(2, 10);

        ICrossover uniform = new UniformCrossover(new BinaryFeatureValidator(1, 9), .7);

        System.out.println("Parent 1: " + chromosomes.getChromosome(0).toString());
        System.out.println("Parent 2: " + chromosomes.getChromosome(1).toString());

        List<_Chromosome> offsprings =  uniform.crossover(chromosomes.getChromosome(0), chromosomes.getChromosome(1));

        System.out.println();

        System.out.println("Parent 1: " + chromosomes.getChromosome(0).toString());
        System.out.println("Parent 2: " + chromosomes.getChromosome(1).toString());
        System.out.println("Child 1: " + offsprings.get(0).toString());
        System.out.println("Child 2: " + offsprings.get(1).toString());

    }

    @Test
    public void testUniformCrossoverWithIntegerRepresentation(){

        IEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(1, 20, 10), 1, 20);
        _Population chromosomes = integerEncoder.createInitialPopulation(2, 10);

        ICrossover uniform = new UniformCrossover(new IntegerFeatureValidator(1, 20, 10), .7);

        System.out.println("Parent 1: " + chromosomes.getChromosome(0).toString());
        System.out.println("Parent 2: " + chromosomes.getChromosome(1).toString());

        List<_Chromosome> offsprings =  uniform.crossover(chromosomes.getChromosome(0), chromosomes.getChromosome(1));

        System.out.println();

        System.out.println("Parent 1: " + chromosomes.getChromosome(0).toString());
        System.out.println("Parent 2: " + chromosomes.getChromosome(1).toString());
        System.out.println("Child 1: " + offsprings.get(0).toString());
        System.out.println("Child 2: " + offsprings.get(1).toString());

    }
}