package OperatorsTests;

import Core._Chromosome;
import Core._Population;
import Operators.IntegerEncoder;
import Operators.TournamentSelector;
import Validation.IntegerFeatureValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TournamentSelectorTest
{
    private IntegerEncoder encoder;
    private TournamentSelector<Integer> selector;
    private _Population<Integer> population;

    private int minValue =0;
    private int maxValue = 10;
    private int chromosomeLength = 5;
    private int populationSize = 5;

    @BeforeEach
    public void setup(){
        IntegerFeatureValidator validator = new IntegerFeatureValidator(minValue,maxValue,chromosomeLength);
        encoder = new IntegerEncoder(validator,minValue,maxValue);
        population = encoder.createInitialPopulation(populationSize,chromosomeLength);
        selector = new TournamentSelector<>(3,42L);

        double[] fitValues = {9.5,6.8,10.5,7.9,11.6};
        for (int i=0;i<population.getSize();i++){
            population.getChromosome(i).setFitness(fitValues[i]);
        }

    }

    @Test
    public void testSelect_ChromosomeFromPop(){
        _Chromosome<Integer> selected = selector.select(population);
        assertTrue(population.getChromosomes().contains(selected),"Selected chromosome must be in the population!");
    }

    @Test
    public void testSelect_NonNull(){
        _Chromosome<Integer> selected = selector.select(population);
        assertNotNull(selected,"Should return a non null chromosome");
    }

    @Test
    public void testSelect_ReturnsFromPopulation(){

        _Chromosome<Integer> selected = selector.select(population);
        assertTrue(population.getChromosomes().contains(selected),"Selected chromosome must be in the population!");

    }
    @Test
    public void testSelect_PreferBestChromosomes() {

        int count = 0;
        for (int i = 0; i < 50; i++) {
            _Chromosome<Integer> selected = selector.select(population);
            if (selected.getFitness() == 11.6) {
                count++;
            }
        }

        assertTrue(count > 0,
                "The fittest chromosome (fitness = 11.6) should be selected at least once");
    }


}

