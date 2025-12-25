package OperatorsTests;

import Core._Population;
import Operators.DoubleEncoder;
import Operators.UniformMutation;
import OperatorsContracts.IEncoder;
import OperatorsContracts.IMutator;
import Validation.DoubleFeatureValidator;
import org.junit.jupiter.api.Test;

public class UniformMutationTest
{
     /* TODO BY
               Mohamed Ashraf

               DONE
     */
    @Test
    public void testUniformMutation()
    {
        IEncoder doubleEncoder = new DoubleEncoder(new DoubleFeatureValidator(0.15, 0.8), 0.15, 0.8);

        _Population chromosomes = doubleEncoder.createInitialPopulation(1, 5);

        System.out.println("Chromosome before Mutation: " + chromosomes.getChromosome(0));

        IMutator uniform = new UniformMutation(0.1, 0.15, 0.8);

        uniform.mutate(chromosomes.getChromosome(0));

        System.out.println("Chromosome after Mutation: " + chromosomes.getChromosome(0));

    }
}
