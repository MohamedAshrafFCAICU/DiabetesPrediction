package OperatorsTests;

import Core._Chromosome;
import Operators.IntegerEncoder;
import Operators.IntegerMutation;
import Validation.IChromosomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerMutationTest
{
     private IntegerMutation mutation;
     private IChromosomeValidator<Integer> validator;
     private IntegerEncoder integerEncoder;


    private int minValue =1;
     private int maxValue = 10;
     private int chromosomeLength = 5;

     private _Chromosome<Integer> createChromosome(){
         return integerEncoder.createInitialPopulation(1,chromosomeLength).getChromosome(0);

     }

     @BeforeEach
    public void setUp(){
         mutation = new IntegerMutation(0.5,minValue,maxValue,validator);
         integerEncoder = new IntegerEncoder(validator,minValue,maxValue);
     }

     @Test
    public void testMutation_GenesChange(){
         mutation.setMutationRate(1.0);
         _Chromosome<Integer> chromosome = createChromosome();

         var values = chromosome.getGenes().stream().map(g->g.getValue()).toList();
         mutation.mutate(chromosome);
         var newValues = chromosome.getGenes().stream().map(g->g.getValue()).toList();

         assertNotEquals(values, newValues,"At least one gene should be mutated");
     }

     @Test
    public void testMutation_NonGenesChange_RateIsZero(){

         mutation.setMutationRate(0.0);
         _Chromosome<Integer> chromosome = createChromosome();

         var values = chromosome.getGenes().stream().map(g->g.getValue()).toList();
         mutation.mutate(chromosome);
         var newValues = chromosome.getGenes().stream().map(g->g.getValue()).toList();

         assertEquals(values,newValues, "Genes should remain unchanged!");
     }

     @Test
    public void testMutation_ValuesInRange(){
         mutation.setMutationRate(1.0);
         _Chromosome<Integer> chromosome = createChromosome();
         mutation.mutate(chromosome);

         for(var gene:chromosome.getGenes()){
             int val = gene.getValue();
             assertTrue(val >= minValue && val < maxValue,"All Genes should remain within the range!");
         }
     }





}
