package OperatorsTests;

import Core._Population;
import Operators.BinaryEncoder;
import Operators.DoubleEncoder;
import Operators.SwapMutation;
import OperatorsContracts.IEncoder;
import OperatorsContracts.IMutator;
import Validation.BinaryFeatureValidator;
import Validation.DoubleFeatureValidator;
import org.junit.jupiter.api.Test;

public class SwapMutationTest
{
     /* TODO BY
                 Anas Mahmoud
     */

    @Test
    public void testSwapMutationWithBinaryRepresentation()
    {
        IEncoder binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(3, 7), 3, 7);
        _Population chromosomes = binaryEncoder.createInitialPopulation(3, 10);

        IMutator swap = new SwapMutation(0.1);
        for (int i = 0; i < chromosomes.getSize(); i++)
        {
            System.out.println("The Chromosome before Mutation");
            System.out.println(chromosomes.getChromosome(i).toString());

            swap.mutate(chromosomes.getChromosome(i));

            System.out.println("The Chromosome after Mutation");
            System.out.println(chromosomes.getChromosome(i).toString());
        }

    }

    @Test
    public void testSwapMutationWithDoubleRepresentation()
    {
        IEncoder doubleEncoder = new DoubleEncoder(new DoubleFeatureValidator(0.12, 0.70), 0.12, 0.70);
        _Population chromosomes = doubleEncoder.createInitialPopulation(3, 10);

        IMutator swap = new SwapMutation(0.1);

        for (int i = 0; i < chromosomes.getSize(); i++)
        {
            System.out.println("The Chromosome before Mutation");
            System.out.println(chromosomes.getChromosome(i).toString());

            swap.mutate(chromosomes.getChromosome(i));

            System.out.println("The Chromosome after Mutation");
            System.out.println(chromosomes.getChromosome(i).toString());
        }

    }
}