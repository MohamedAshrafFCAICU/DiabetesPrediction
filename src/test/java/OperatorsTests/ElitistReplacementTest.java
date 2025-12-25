package OperatorsTests;

import Core.IntegerGene;
import Core._Chromosome;
import Core._Gene;
import Core._Population;
import Operators.BinaryEncoder;
import Operators.DoubleEncoder;
import Operators.ElitistReplacement;
import Operators.IntegerEncoder;
import OperatorsContracts.IReplacementStrategy;
import Validation.BinaryFeatureValidator;
import Validation.DoubleFeatureValidator;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class ElitistReplacementTest {


    BinaryEncoder binaryEncoder = new BinaryEncoder(new BinaryFeatureValidator(5,8),5,8);

    DoubleEncoder doubleEncoder = new DoubleEncoder(new DoubleFeatureValidator(5,8),5, 8);

    IntegerEncoder integerEncoder = new IntegerEncoder(new IntegerFeatureValidator(0,10,5),0,10);


    double[] currentPopFitness = {10.5, 8.3, 15.7, 6.2, 12.1};

    double[] offspringPopFitness = {9.8, 14.2, 7.5, 11.3, 13.6};

    @Test
    public void testElitistReplacement_SamePopulationSize_Binary() {

        _Population<Boolean> currentBinaryPop = binaryEncoder.createInitialPopulation(5,10);
        _Population<Boolean> offspringBinaryPop = binaryEncoder.createInitialPopulation(5,10);

        for (int i = 0; i < currentBinaryPop.getSize(); i++) {

            currentBinaryPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringBinaryPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        int eliteCount = 2;
        IReplacementStrategy<Boolean> replacement = new ElitistReplacement<>(eliteCount);

        _Population<Boolean> newPop = replacement.replace(currentBinaryPop, offspringBinaryPop);

        assertEquals(currentBinaryPop.getSize(), newPop.getSize(), "New population size should equal current population size");
        System.out.println(newPop.toString());
        System.out.println(newPop.getChromosomes().toString());

    }


    @Test
    public void testElitistReplacement_PreservesElites_Binary() {

        _Population<Boolean> currentBinaryPop = binaryEncoder.createInitialPopulation(5,10);
        _Population<Boolean> offspringBinaryPop = binaryEncoder.createInitialPopulation(5,10);

        for (int i = 0; i < currentBinaryPop.getSize(); i++) {
            currentBinaryPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringBinaryPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        int eliteCount = 2;
        IReplacementStrategy<Boolean> replacement = new ElitistReplacement<>(eliteCount);

        _Population<Boolean> newPop = replacement.replace(currentBinaryPop, offspringBinaryPop);
        currentBinaryPop.sortByFitness();
        double bestEliteFitness = currentBinaryPop.getChromosome(0).getFitness();
        double secondEliteFitness = currentBinaryPop.getChromosome(1).getFitness();
        double bestOffspringFitness = offspringBinaryPop.getChromosome(0).getFitness();
        double secondOffspringFitness = offspringBinaryPop.getChromosome(1).getFitness();

        List<Double> newFitnesses = newPop.getChromosomes().stream().map(_Chromosome::getFitness).toList();

        assertTrue(newFitnesses.contains(bestEliteFitness),"Best elite should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondEliteFitness), "Second best elite should be preserved in the new generation");

        assertTrue(newFitnesses.contains(bestOffspringFitness), "Best elite offspring should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondOffspringFitness), "Second best elite offspring should be preserved in the new generation");

        System.out.println("Current elites: " + currentBinaryPop.getChromosome(0).toString() + ", " + currentBinaryPop.getChromosome(1).toString());
        System.out.println("Offspring elites: " + offspringBinaryPop.getChromosome(0).toString() + ", " + offspringBinaryPop.getChromosome(1).toString());
        System.out.println();
    }


    @Test
    public void testElitistReplacement_InvalidEliteCount_Binary() {
        assertThrows(IllegalArgumentException.class, () -> new ElitistReplacement<Boolean>(-1),"Elite count cannot be negative");
    }

    @Test
    public void testElitistReplacement_EliteCountExceedsPopulation_Binary() {

        _Population<Boolean> currentBinaryPop = binaryEncoder.createInitialPopulation(5,10);
        _Population<Boolean> offspringBinaryPop = binaryEncoder.createInitialPopulation(5,10);


        IReplacementStrategy<Boolean> replacement = new ElitistReplacement<>(10);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentBinaryPop, offspringBinaryPop), "Elite count exceeding population size should throw exception");
    }

    @Test
    public void testElitistReplacement_NullInputs_Binary() {

        _Population<Boolean> currentBinaryPop = binaryEncoder.createInitialPopulation(5,10);
        _Population<Boolean> offspringBinaryPop = binaryEncoder.createInitialPopulation(5,10);

        IReplacementStrategy<Boolean> replacement = new ElitistReplacement<>(1);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(null, offspringBinaryPop),"Null current population should throw exception");

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentBinaryPop, null),"Null offspring population should throw exception");
    }

    @Test
    public void testElitistReplacement_SamePopulationSize_Double(){
        _Population<Double> currentPop = doubleEncoder.createInitialPopulation(5,10);
        _Population<Double> offspringPop = doubleEncoder.createInitialPopulation(5,10);

        int eliteCount = 2;

        IReplacementStrategy<Double> replacement = new ElitistReplacement<>(2);
        _Population<Double> newPop = replacement.replace(currentPop, offspringPop);

        assertEquals(currentPop.getSize(),newPop.getSize(),"New population size should equal current population size");
        System.out.println(newPop.toString());
        System.out.println(newPop.getChromosomes().toString());
        System.out.println();

    }

    @Test
    public void testElitistReplacement_PreservesElites_Double() {

        _Population<Double> currentPop = doubleEncoder.createInitialPopulation(5,10);
        _Population<Double> offspringPop = doubleEncoder.createInitialPopulation(5,10);

        for (int i = 0; i < currentPop.getSize(); i++) {
            currentPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        int eliteCount = 2;
        IReplacementStrategy<Double> replacement = new ElitistReplacement<>(eliteCount);

        _Population<Double> newPop = replacement.replace(currentPop, offspringPop);
        currentPop.sortByFitness();

        double bestEliteFitness = currentPop.getChromosome(0).getFitness();
        double secondEliteFitness = currentPop.getChromosome(1).getFitness();
        double bestOffspringFitness = offspringPop.getChromosome(0).getFitness();
        double secondOffspringFitness = offspringPop.getChromosomes().get(1).getFitness();

        List<Double> newFitnesses = newPop.getChromosomes().stream().map(_Chromosome::getFitness).toList();

        assertTrue(newFitnesses.contains(bestEliteFitness),"Best elite should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondEliteFitness), "Second best elite should be preserved in the new generation");

        assertTrue(newFitnesses.contains(bestOffspringFitness), "Best elite offspring should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondOffspringFitness), "Second elite offspring should be preserved in the new generation");

        System.out.println("Current elites: " + currentPop.getChromosome(0).toString() + ", " + currentPop.getChromosome(1).toString());
        System.out.println("Offspring elites: " + offspringPop.getChromosome(0).toString() + ", " + offspringPop.getChromosome(1).toString());
        System.out.println();

    }

    @Test
    public void testElitistReplacement_InvalidEliteCount_Double() {
        assertThrows(IllegalArgumentException.class, () -> new ElitistReplacement<Double>(-1),"Elite count cannot be negative");
    }

    @Test
    public void testElitistReplacement_EliteCountExceedsPopulation_Double() {

        _Population<Double> currentDoublePop = doubleEncoder.createInitialPopulation(5,10);
        _Population<Double> offspringDoublePop = doubleEncoder.createInitialPopulation(5,10);

        // Assign fitness values
        for (int i = 0; i < currentDoublePop.getSize(); i++) {
            currentDoublePop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringDoublePop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        IReplacementStrategy<Double> replacement = new ElitistReplacement<>(10);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentDoublePop, offspringDoublePop), "Elite count exceeding population size should throw exception");
    }

    @Test
    public void testElitistReplacement_NullInputs_Double() {

        _Population<Double> currentDoublePop = doubleEncoder.createInitialPopulation(5,10);
        _Population<Double> offspringDoublePop = doubleEncoder.createInitialPopulation(5,10);

        IReplacementStrategy<Double> replacement = new ElitistReplacement<>(1);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(null, offspringDoublePop),"Null current population should throw exception");

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentDoublePop, null),"Null offspring population should throw exception");
    }

    @Test
    public void testElitistReplacement_SamePopulationSize_Integer() {

        _Population<Integer> currentIntegerPop = integerEncoder.createInitialPopulation(5,10);
        _Population<Integer> offspringIntegerPop = integerEncoder.createInitialPopulation(5,10);

        // Assign fitness values
        for (int i = 0; i < currentIntegerPop.getSize(); i++) {
            currentIntegerPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringIntegerPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        int eliteCount = 2;
        IReplacementStrategy<Integer> replacement = new ElitistReplacement<>(eliteCount);

        _Population<Integer> newPop = replacement.replace(currentIntegerPop, offspringIntegerPop);

        assertEquals(currentIntegerPop.getSize(), newPop.getSize(), "New population size should equal current population size");
        System.out.println(newPop.toString());
        System.out.println(newPop.getChromosomes().toString());
        System.out.println();
    }

    @Test
    public void testElitistReplacement_PreservesElites_Integer() {

        _Population<Integer> currentIntegerPop = integerEncoder.createInitialPopulation(5,10);
        _Population<Integer> offspringIntegerPop = integerEncoder.createInitialPopulation(5,10);

        // Assign fitness values
        for (int i = 0; i < currentIntegerPop.getSize(); i++) {
            currentIntegerPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringIntegerPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        int eliteCount = 2;
        IReplacementStrategy<Integer> replacement = new ElitistReplacement<>(eliteCount);

        _Population<Integer> newPop = replacement.replace(currentIntegerPop, offspringIntegerPop);
        currentIntegerPop.sortByFitness();

        double bestEliteFitness = currentIntegerPop.getChromosome(0).getFitness();
        double secondEliteFitness = currentIntegerPop.getChromosome(1).getFitness();
        double bestOffspringFitness = offspringIntegerPop.getChromosome(0).getFitness();
        double secondOffspringFitness = offspringIntegerPop.getChromosome(1).getFitness();

        List<Double> newFitnesses = newPop.getChromosomes().stream().map(_Chromosome::getFitness).toList();

        assertTrue(newFitnesses.contains(bestEliteFitness), "Best elite should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondEliteFitness), "Second best elite should be preserved in the new generation");

        assertTrue(newFitnesses.contains(bestOffspringFitness), "Best elite offspring should be preserved in the new generation");
        assertTrue(newFitnesses.contains(secondOffspringFitness), "Second best elite offspring should be preserved in the new generation");

        System.out.println("Current elites: " + currentIntegerPop.getChromosome(0).toString() + ", " + currentIntegerPop.getChromosome(1).toString());
        System.out.println("Offspring elites: " + offspringIntegerPop.getChromosome(0).toString() + ", " + offspringIntegerPop.getChromosome(1).toString());
        System.out.println();
    }

    @Test
    public void testElitistReplacement_InvalidEliteCount_Integer() {
        assertThrows(IllegalArgumentException.class, () -> new ElitistReplacement<Integer>(-1),"Elite count cannot be negative");
    }

    @Test
    public void testElitistReplacement_EliteCountExceedsPopulation_Integer() {

        _Population<Integer> currentIntegerPop = integerEncoder.createInitialPopulation(5,10);
        _Population<Integer> offspringIntegerPop = integerEncoder.createInitialPopulation(5,10);

        // Assign fitness values
        for (int i = 0; i < currentIntegerPop.getSize(); i++) {
            currentIntegerPop.getChromosome(i).setFitness(currentPopFitness[i]);
            offspringIntegerPop.getChromosome(i).setFitness(offspringPopFitness[i]);
        }

        IReplacementStrategy<Integer> replacement = new ElitistReplacement<>(10);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentIntegerPop, offspringIntegerPop), "Elite count exceeding population size should throw exception");
    }

    @Test
    public void testElitistReplacement_NullInputs_Integer() {

        _Population<Integer> currentIntegerPop = integerEncoder.createInitialPopulation(5,10);
        _Population<Integer> offspringIntegerPop = integerEncoder.createInitialPopulation(5,10);

        IReplacementStrategy<Integer> replacement = new ElitistReplacement<>(1);

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(null, offspringIntegerPop),"Null current population should throw exception");

        assertThrows(IllegalArgumentException.class, () -> replacement.replace(currentIntegerPop, null),"Null offspring population should throw exception");
    }


    
}
