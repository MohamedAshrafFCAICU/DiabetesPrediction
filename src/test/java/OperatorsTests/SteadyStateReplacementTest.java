package OperatorsTests;

import Core._Chromosome;
import Core._Population;
import Operators.BinaryEncoder;
import Operators.IntegerEncoder;
import Operators.SteadyStateReplacement;
import OperatorsContracts.IEncoder;
import OperatorsContracts.IReplacementStrategy;
import Validation.BinaryFeatureValidator;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.Test;

public class SteadyStateReplacementTest {
     /* TODO BY
                 Mohamed Ashraf

                 DONE
     */

    @Test
    public void testSteadyStateReplacementWithBinaryRepresentation()
    {
        IEncoder binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(1 , 5), 1 , 5);

        _Population currentPopulation = binaryEncoder.createInitialPopulation(5, 5);

        double[] fitnessOfCurrentPopulation = {20, 15 , 63.5, 10, 55};

        _Population offspring = binaryEncoder.createInitialPopulation(5, 5);

        double[] fitnessOfOffspring = {88, 12 , 62, 19, 30};

        for (int i = 0; i < 5; i++)
        {
            _Chromosome chromosomeOfCurrentPopulation = currentPopulation.getChromosome(i);
            chromosomeOfCurrentPopulation.setFitness(fitnessOfCurrentPopulation[i]);

            _Chromosome chromosomeOfOffspring = offspring.getChromosome(i);
            chromosomeOfOffspring.setFitness(fitnessOfOffspring[i]);
        }

        System.out.println("Before Replacement");

        System.out.println("The current population is: ");
        for (int i = 0; i < currentPopulation.getSize(); i++)
            System.out.println(currentPopulation.getChromosome(i).toString());

        System.out.println("The offspring is: ");
        for (int i = 0; i < offspring.getSize(); i++)
            System.out.println(offspring.getChromosome(i).toString());

        IReplacementStrategy steadyState = new SteadyStateReplacement();

        _Population newPopulation =  steadyState.replace(currentPopulation, offspring);

        System.out.println("The new population is: ");
        for (int i = 0; i < newPopulation.getSize(); i++)
            System.out.println(newPopulation.getChromosome(i).toString());
    }

    @Test
    public void testSteadyStateReplacementWithIntegerRepresentation()
    {
        IEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(1 , 5, 3), 1 , 5);

        _Population currentPopulation = integerEncoder.createInitialPopulation(5, 3);

        double[] fitnessOfCurrentPopulation = {20, 15 , 63.5, 10, 55};

        _Population offspring = integerEncoder.createInitialPopulation(5, 3);

        double[] fitnessOfOffspring = {88, 12 , 62, 19, 30};

        for (int i = 0; i < 5; i++)
        {
            _Chromosome chromosomeOfCurrentPopulation = currentPopulation.getChromosome(i);
            chromosomeOfCurrentPopulation.setFitness(fitnessOfCurrentPopulation[i]);

            _Chromosome chromosomeOfOffspring = offspring.getChromosome(i);
            chromosomeOfOffspring.setFitness(fitnessOfOffspring[i]);
        }

        System.out.println("Before Replacement");

        System.out.println("The current population is: ");
        for (int i = 0; i < currentPopulation.getSize(); i++)
            System.out.println(currentPopulation.getChromosome(i).toString());

        System.out.println("The offspring is: ");
        for (int i = 0; i < offspring.getSize(); i++)
            System.out.println(offspring.getChromosome(i).toString());

        IReplacementStrategy steadyState = new SteadyStateReplacement();

        _Population newPopulation =  steadyState.replace(currentPopulation, offspring);

        System.out.println("The new population is: ");
        for (int i = 0; i < newPopulation.getSize(); i++)
            System.out.println(newPopulation.getChromosome(i).toString());
    }
}
