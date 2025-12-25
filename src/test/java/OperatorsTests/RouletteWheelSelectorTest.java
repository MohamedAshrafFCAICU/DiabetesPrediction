package OperatorsTests;

import Core._Chromosome;
import Core._Population;
import Operators.BinaryEncoder;
import Operators.DoubleEncoder;
import Operators.IntegerEncoder;
import Operators.RouletteWheelSelector;
import OperatorsContracts.IEncoder;
import OperatorsContracts.ISelector;
import Validation.BinaryFeatureValidator;
import Validation.DoubleFeatureValidator;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.Test;

public class RouletteWheelSelectorTest
{
     /* TODO BY
                Mohamed Ashraf

                DONE
     */
    @Test
    public void testRouletteWheelSelectorWithBinaryEncodedChromosomes()
    {
        IEncoder binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(5, 10), 5, 10);

        _Population chromosomes = binaryEncoder.createInitialPopulation(3, 20);

        double[] fitness = {20, 35.6, 88.5};

        for (int i = 0; i < chromosomes.getSize(); i++)
            chromosomes.getChromosome(i).setFitness(fitness[i]);

        ISelector rouletteWheelSelector = new RouletteWheelSelector();

        _Chromosome selectedChromosome = rouletteWheelSelector.select(chromosomes);

        System.out.println( "Chromosome: " + selectedChromosome.toString());

          /*  Tested and works correctly */
    }

    @Test
    public void testRouletteWheelSelectorWithIntegerEncodedChromosomes()
    {
        IEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(1, 20, 10), 1, 20);

        _Population chromosomes = integerEncoder.createInitialPopulation(3, 10);

        double[] fitness = {15.35, 81.3, 20};

        for (int i = 0; i < chromosomes.getSize(); i++)
            chromosomes.getChromosome(i).setFitness(fitness[i]);

        ISelector rouletteWheelSelector = new RouletteWheelSelector();

        _Chromosome selectedChromosome = rouletteWheelSelector.select(chromosomes);

        System.out.println( "Chromosome: " + selectedChromosome.toString());

        /*  Tested and works correctly */
    }

    @Test
    public void testRouletteWheelSelectorWithDoubleEncodedChromosomes()
    {
        IEncoder doubleEncoder = new DoubleEncoder(new DoubleFeatureValidator(0.15, 0.75), 0.15, 0.75);

        _Population chromosomes = doubleEncoder.createInitialPopulation(3, 20);

        double[] fitness = {75.34, 19.74, 3};

        for (int i = 0; i < chromosomes.getSize(); i++)
            chromosomes.getChromosome(i).setFitness(fitness[i]);

        ISelector rouletteWheelSelector = new RouletteWheelSelector();

        _Chromosome selectedChromosome = rouletteWheelSelector.select(chromosomes);

        System.out.println( "Chromosome: " + selectedChromosome.toString());

        /*  Tested and works correctly */
    }
}
