package Operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Core._Chromosome;
import Core._Gene;
import OperatorsContracts.ICrossover;
import Validation.IChromosomeValidator;

public class Order1Crossover<T> implements ICrossover<T> {
    private double crossoverRate;
    private final Random random;
    private final IChromosomeValidator<T> validator;

    public Order1Crossover(double crossoverRate, IChromosomeValidator<T> validator) {
        this.crossoverRate = crossoverRate;
        this.random = new Random();
        this.validator = validator;
    }

    public Order1Crossover(double crossoverRate, long seed, IChromosomeValidator<T> validator) {
        this.crossoverRate = crossoverRate;
        this.random = new Random(seed);
        this.validator = validator;
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

        List<_Chromosome<T>> newGeneration = new ArrayList<>(2);

        boolean validChildren = false;
        int maxAttempts = 2000; // prevent infinite loop
        int attempts = 0;

        while (!validChildren && attempts < maxAttempts) {
            attempts++;

            // Create list of genes for children initialized to null
            List<_Gene<T>> child1Genes = new ArrayList<>(length);
            List<_Gene<T>> child2Genes = new ArrayList<>(length);

            for (int i = 0; i < length; i++) // to prevent overwrite the data
            {
                child1Genes.add(null);
                child2Genes.add(null);
            }

            // get start and end points
            int point1 = random.nextInt(length);
            int point2 = random.nextInt(length);
            int start = Math.min(point1, point2);
            int end = Math.max(point1, point2);

            // copy the segment from parents to children
            for (int i = start; i <= end; i++) {
                child1Genes.set(i, parent1.getGene(i).copy());
                child2Genes.set(i, parent2.getGene(i).copy());
            }

            // fill remaining genes for first child
            int currentIndex1 = (end + 1) % length;
            for (int i = 0; i < length; i++) {
                _Gene<T> candidateFromP2 = parent2.getGene((end + 1 + i) % length);
                boolean exists = false;
                for (_Gene<T> gene : child1Genes) {
                    if (gene != null && gene.getValue().equals(candidateFromP2.getValue())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    child1Genes.set(currentIndex1, candidateFromP2.copy());
                    currentIndex1 = (currentIndex1 + 1) % length;
                }
            }

            // fill remaining genes for second child
            int currentIndex2 = (end + 1) % length;
            for (int i = 0; i < length; i++) {
                _Gene<T> candidateFromP1 = parent1.getGene((end + 1 + i) % length);
                boolean exists = false;
                for (_Gene<T> gene : child2Genes) {
                    if (gene != null && gene.getValue().equals(candidateFromP1.getValue())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    child2Genes.set(currentIndex2, candidateFromP1.copy());
                    currentIndex2 = (currentIndex2 + 1) % length;
                }
            }

            _Chromosome<T> child1 = new _Chromosome<>(child1Genes);
            _Chromosome<T> child2 = new _Chromosome<>(child2Genes);

            try {
                validator.validate(child1);
                validator.validate(child2);
                validChildren = true;
                newGeneration.clear();
                newGeneration.add(child1);
                newGeneration.add(child2);
            } catch (Exception e) {
                // Validation failed — retry
                validChildren = false;
            }
        }

        if (!validChildren) {
            // fallback: return parents if we failed too many times
            newGeneration.clear();
            newGeneration.add(parent1.copy());
            newGeneration.add(parent2.copy());
        }

        return newGeneration;
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