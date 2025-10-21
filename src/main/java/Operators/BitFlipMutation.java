package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.IMutator;

import java.util.Random;

public class BitFlipMutation implements IMutator<Boolean> {
    private double mutationRate;
    private final Random random;

    public BitFlipMutation(double mutationRate) {
        this.mutationRate = mutationRate;
        this.random = new Random();
    }

    public BitFlipMutation(double mutationRate, long seed) {
        this.mutationRate = mutationRate;
        this.random = new Random(seed);
    }

    @Override
    public void mutate(_Chromosome<Boolean> chromosome) {
          /*TODO
              by Ahmed Kamel
        */

        for (int i = 0; i < chromosome.getLength(); i++) {
            if (random.nextDouble() < mutationRate) {
                _Gene<Boolean> gene = chromosome.getGene(i);
                Boolean currentValue = gene.getValue();
                gene.setValue(!currentValue);
                chromosome.setGene(i, gene);
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
