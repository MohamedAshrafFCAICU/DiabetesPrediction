package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.IMutator;
import Validation.IChromosomeValidator;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import java.util.*;


public class IntegerMutation implements IMutator<Integer> {
    private double mutationRate;
    private final Random random;
    private final int minValue;
    private final int maxValue;
    private final IChromosomeValidator<Integer> validator;

    public IntegerMutation(double mutationRate, int minValue, int maxValue,
                           IChromosomeValidator<Integer> validator) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.validator = validator;
        this.random = new Random();
    }

        @Override
        public void mutate(_Chromosome<Integer> chromosome) {

            if(chromosome == null){
                throw new IllegalArgumentException("Chromosome cannot be null");
            }

            for (int i=0; i<chromosome.getLength();i++){
                if(random.nextDouble() < mutationRate){
                    _Gene<Integer> gene = chromosome.getGene(i);
                    int newValue;
                    do{
                        newValue = random.nextInt(maxValue - minValue) + minValue;
                    } while (chromosome.getLength() > 1 && newValue == gene.getValue());
                    gene.setValue(newValue);
                    chromosome.invalidateFitness();
                }
            }
            if(validator!=null && !validator.isValid(chromosome)){
                boolean repair = validator.repair(chromosome);
                if(!repair && !validator.isValid(chromosome)){
                    throw new IllegalStateException("Chromosome is invalid and could not be repaired!");
                }
            }
        }

    @Override
    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    @Override
    public double getMutationRate() {
        return mutationRate;
    }
}
