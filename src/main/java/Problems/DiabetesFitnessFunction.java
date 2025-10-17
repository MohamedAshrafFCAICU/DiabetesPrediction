package Problems;

import Core._Chromosome;
import ML.SimpleRandomForest;
import OperatorsContracts.IFitnessFunction;
import Validation.IChromosomeValidator;

import java.util.List;

                /*
                        TODO BY AHMED KAMEL

                */


public class DiabetesFitnessFunction<T> implements IFitnessFunction<T> {

    private final double[][] X_train;
    private final int[] y_train;
    private final double[][] X_test;
    private final int[] y_test;
    private final IChromosomeValidator<T> validator;
    private final SimpleRandomForest model;
    private final double featurePenalty;

    public DiabetesFitnessFunction(double[][] X_train, int[] y_train,
                                   double[][] X_test, int[] y_test,
                                   IChromosomeValidator<T> validator,
                                   double featurePenalty, SimpleRandomForest model) {
        this.X_train = X_train;
        this.y_train = y_train;
        this.X_test = X_test;
        this.y_test = y_test;
        this.validator = validator;
        this.featurePenalty = featurePenalty;
        this.model = model;
    }

    @Override
    public double calculate(_Chromosome<T> chromosome) {
            return 0.0;
    }

    private List<Integer> extractSelectedFeatures(_Chromosome<T> chromosome) {
            return null;
    }

    private double[][] extractColumns(double[][] data, List<Integer> columns) {
            return null;
    }
}
