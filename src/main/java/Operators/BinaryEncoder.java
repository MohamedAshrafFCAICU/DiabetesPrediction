package Operators;

import Core.BinaryGene;
import Core._Chromosome;
import Core._Gene;
import Core._Population;
import OperatorsContracts.IEncoder;
import Validation.IChromosomeValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class BinaryEncoder implements IEncoder<Boolean> {
    private final Random random;
    private final IChromosomeValidator<Boolean> validator;
    private final int minFeatures;
    private final int maxFeatures;

    public BinaryEncoder(IChromosomeValidator<Boolean> validator,
                         int minFeatures, int maxFeatures) {
        this.random = new Random();
        this.validator = validator;
        this.minFeatures = minFeatures;
        this.maxFeatures = maxFeatures;
    }

    @Override
    public _Population<Boolean> createInitialPopulation(int populationSize, int chromosomeLength) {
        List<_Chromosome<Boolean>> chromosomes = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            chromosomes.add(createValidChromosome(chromosomeLength));
        }

        return new _Population<>(chromosomes);
    }

    private _Chromosome<Boolean> createValidChromosome(int length) {

        int numFeatures = minFeatures + random.nextInt(maxFeatures - minFeatures + 1);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, random);

        List<_Gene<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add(new BinaryGene(false));
        }

        for (int i = 0; i < numFeatures; i++) {
            genes.get(indices.get(i)).setValue(true);
        }

        _Chromosome<Boolean> chromosome = new _Chromosome<>(genes);

        if (validator != null && !validator.isValid(chromosome)) {
            validator.repair(chromosome);
        }

        return chromosome;
    }

    @Override
    public _Chromosome<Boolean> encode(Object solution) {
        if (!(solution instanceof boolean[])) {
            throw new IllegalArgumentException("Solution must be boolean array");
        }
        boolean[] values = (boolean[]) solution;
        List<_Gene<Boolean>> genes = new ArrayList<>();

        for (boolean value : values) {
            genes.add(new BinaryGene(value));
        }

        return new _Chromosome<>(genes);
    }

    @Override
    public Object decode(_Chromosome<Boolean> chromosome) {
        List<Integer> selectedFeatures = new ArrayList<>();

        for (int i = 0; i < chromosome.getLength(); i++) {
            if (chromosome.getGene(i).getValue()) {
                selectedFeatures.add(i);
            }
        }

        return selectedFeatures;
    }
}
