package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.IMutator;

import java.util.Random;

public class UniformMutation implements IMutator<Double> {
    private double mutationRate;
    private final Random random;
    private final double minValue;
    private final double maxValue;

    public UniformMutation(double mutationRate, double minValue, double maxValue) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = new Random();
    }

    public UniformMutation(double mutationRate, double minValue, double maxValue, long seed) {
        this.mutationRate = mutationRate;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = new Random(seed);
    }

    @Override
    public void mutate(_Chromosome<Double> chromosome) {
          /*TODO
              by Mohamed Ashraf

              DONE
          */

        for (_Gene gene : chromosome.getGenes()) {
            double r = random.nextDouble();
            if (r > mutationRate)
                continue;

            double x = (double)gene.getValue();
            double deltaLower = x - minValue;
            double deltaUpper = maxValue - x;

            double r1 = random.nextDouble();

            if(r1 <= 0.5)
            {
                double r2 = random.nextDouble() * deltaLower;
                gene.setValue(x - r2);
            }
            else
            {
                double r2 = random.nextDouble() * deltaUpper;
                gene.setValue(x + r2);
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
