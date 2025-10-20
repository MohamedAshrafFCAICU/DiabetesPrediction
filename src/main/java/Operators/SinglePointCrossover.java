package Operators;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.ICrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover<T> implements ICrossover<T> {
    private double crossoverRate;
    private final Random random;

    public SinglePointCrossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.random = new Random();
    }

    public SinglePointCrossover(double crossoverRate, long seed) {
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
    }

    @Override
    public List<_Chromosome<T>> crossover(_Chromosome<T> parent1, _Chromosome<T> parent2) {

        int length = parent1.getLength();

        // check crossover rate
        if (random.nextDouble() >= crossoverRate) {
            List<_Chromosome<T>> children = new ArrayList<>(2);
            children.add(parent1.copy());
            children.add(parent2.copy());
            return children;
        }
        List<_Gene<T>> genes1 = parent1.getGenes();
        List<_Gene<T>> genes2 = parent2.getGenes();

        int crossoverPoint = random.nextInt(length-1) + 1; // ensure at least one gene is swapped

        List<_Gene<T>> child1Genes = new ArrayList<>(length);
        List<_Gene<T>> child2Genes = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                child1Genes.add(genes1.get(i).copy());
                child2Genes.add(genes2.get(i).copy());
            } else {
                child1Genes.add(genes2.get(i).copy());
                child2Genes.add(genes1.get(i).copy());
            }
        }

        _Chromosome<T> child1 = new _Chromosome<>(child1Genes);
        _Chromosome<T> child2 = new _Chromosome<>(child2Genes);

        List<_Chromosome<T>> newGeneration = new ArrayList<>();

        newGeneration.add(child1);
        newGeneration.add(child2);

        return  newGeneration;
    }

    @Override
    public void setCrossoverRate(double rate) {
        this.crossoverRate = rate;
    }

    @Override
    public double getCrossoverRate() {
        return crossoverRate;
    }
}
