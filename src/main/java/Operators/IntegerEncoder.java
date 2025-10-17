package Operators;

import Core._Chromosome;
import Core._Gene;
import Core.IntegerGene;
import Core._Population;
import OperatorsContracts.IEncoder;
import Validation.IChromosomeValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class IntegerEncoder implements IEncoder<Integer> {
    private final Random random;
    private final IChromosomeValidator<Integer> validator;
    private final int minValue;
    private final int maxValue;

    public IntegerEncoder(IChromosomeValidator<Integer> validator,
                          int minValue, int maxValue) {
        this.random = new Random();
        this.validator = validator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public _Population<Integer> createInitialPopulation(int _PopulationSize, int _ChromosomeLength) {
        List<_Chromosome<Integer>> _Chromosomes = new ArrayList<>();

        for (int i = 0; i < _PopulationSize; i++) {
            _Chromosomes.add(createValidChromosome(_ChromosomeLength));
        }

        return new _Population<>(_Chromosomes);
    }

    private _Chromosome<Integer> createValidChromosome(int length) {
        List<Integer> features = new ArrayList<>();
        for (int i = minValue; i <= maxValue; i++) {
            features.add(i);
        }
        Collections.shuffle(features, random);

        List<_Gene<Integer>> _Genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            _Genes.add(new IntegerGene(features.get(i)));
        }

        _Chromosome<Integer> _Chromosome = new _Chromosome<>(_Genes);

        if (validator != null && !validator.isValid(_Chromosome)) {
            validator.repair(_Chromosome);
        }

        return _Chromosome;
    }

    @Override
    public _Chromosome<Integer> encode(Object solution) {
        if (!(solution instanceof int[])) {
            throw new IllegalArgumentException("Solution must be int array");
        }

        int[] values = (int[]) solution;
        List<_Gene<Integer>> _Genes = new ArrayList<>();

        for (int value : values) {
            _Genes.add(new IntegerGene(value));
        }

        return new _Chromosome<>(_Genes);
    }

    @Override
    public Object decode(_Chromosome<Integer> _Chromosome) {
        List<Integer> selectedFeatures = new ArrayList<>();

        for (int i = 0; i < _Chromosome.getLength(); i++) {
            selectedFeatures.add(_Chromosome.getGene(i).getValue());
        }

        return selectedFeatures;
    }
}
