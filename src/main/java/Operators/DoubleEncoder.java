package Operators;

import Core.DoubleGene;
import Core._Chromosome;
import Core._Gene;
import Core._Population;
import OperatorsContracts.IEncoder;
import Validation.IChromosomeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoubleEncoder implements IEncoder<Double> {
    private final Random random;
    private final IChromosomeValidator<Double> validator;
    private final double minValue;
    private final double maxValue;

    public DoubleEncoder(IChromosomeValidator<Double> validator,
                         double minValue, double maxValue) {
        this.random = new Random();
        this.validator = validator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public _Population<Double> createInitialPopulation(int populationSize, int chromosomeLength) {
        List<_Chromosome<Double>> chromosomes = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            chromosomes.add(createValidChromosome(chromosomeLength));
        }

        return new _Population<>(chromosomes);
    }

    private _Chromosome<Double> createValidChromosome(int length) {
        List<_Gene<Double>> genes = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            double value = minValue + random.nextDouble() * (maxValue - minValue);
            genes.add(new DoubleGene(value));
        }

        _Chromosome<Double> chromosome = new _Chromosome<>(genes);

        if (validator != null && !validator.isValid(chromosome)) {
            validator.repair(chromosome);
        }

        return chromosome;
    }

    @Override
    public _Chromosome<Double> encode(Object solution) {
        if (!(solution instanceof double[])) {
            throw new IllegalArgumentException("Solution must be double array");
        }

        double[] values = (double[]) solution;
        List<_Gene<Double>> genes = new ArrayList<>();

        for (double value : values) {
            genes.add(new DoubleGene(value));
        }

        return new _Chromosome<>(genes);
    }

    @Override
    public Object decode(_Chromosome<Double> chromosome) {
        List<Integer> selectedFeatures = new ArrayList<>();

        for (int i = 0; i < chromosome.getLength(); i++) {
            if (chromosome.getGene(i).getValue() >= 0.5) {
                selectedFeatures.add(i);
            }
        }

        return selectedFeatures;
    }
}
