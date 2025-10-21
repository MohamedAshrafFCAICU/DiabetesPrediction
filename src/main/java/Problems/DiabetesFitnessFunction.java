package Problems;

import Core._Chromosome;
import Core._Gene;
import ML.SimpleRandomForest;
import OperatorsContracts.IFitnessFunction;
import Validation.IChromosomeValidator;

import java.util.ArrayList;
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
    private final int totalFeatures;


    public DiabetesFitnessFunction(double[][] X_train, int[] y_train,
                                   double[][] X_test, int[] y_test,
                                   IChromosomeValidator<T> validator,
                                   double featurePenalty, SimpleRandomForest model,int totalFeatures) {
        this.X_train = X_train;
        this.y_train = y_train;
        this.X_test = X_test;
        this.y_test = y_test;
        this.validator = validator;
        this.featurePenalty = featurePenalty;
        this.model = model;
        this.totalFeatures = totalFeatures;
    }

    @Override
    public double calculate(_Chromosome<T> chromosome) {
        if (!validator.isValid(chromosome)) {
            return 0.0;
        }

        List<Integer> selectedFeatures = extractSelectedFeatures(chromosome);

        if (selectedFeatures.isEmpty() || selectedFeatures.size() < 5) {
            return 0.0;
        }

        try {
            double[][] X_train_selected = extractColumns(X_train, selectedFeatures);
            double[][] X_test_selected = extractColumns(X_test, selectedFeatures);

            model.train(X_train_selected, y_train);

            double accuracy = model.accuracy(X_test_selected, y_test);

            double featureRatio = (double) selectedFeatures.size() / totalFeatures;
            double penalty = featurePenalty * featureRatio;

            double fitness = accuracy - penalty;

            return Math.max(0.0, fitness);

        } catch (Exception e) {
            System.err.println("Error calculating fitness: " + e.getMessage());
            return 0.0;
        }

    }

    private List<Integer> extractSelectedFeatures(_Chromosome<T> chromosome) {

        List<Integer> selectedIndices = new ArrayList<>();
        List<_Gene<T>> genes = chromosome.getGenes();

        for (int i = 0; i < genes.size(); i++) {
            _Gene<T> gene = genes.get(i);
            T value = gene.getValue();

            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    selectedIndices.add(i);
                }
            } else if (value instanceof Integer) {
                if ((Integer) value == 1) {
                    selectedIndices.add(i);
                }
            } else if (value instanceof Double) {
                if ((Double) value == 1.0) {
                    selectedIndices.add(i);
                }
            } else {
                String valueStr = value.toString();
                if (valueStr.equals("1") || valueStr.equalsIgnoreCase("true")) {
                    selectedIndices.add(i);
                }
            }
        }

        return selectedIndices;
    }

    private double[][] extractColumns(double[][] data, List<Integer> columns) {
        if (data == null || data.length == 0 || columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Data and columns must not be null or empty");
        }

        int numRows = data.length;
        int numSelectedCols = columns.size();
        double[][] result = new double[numRows][numSelectedCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numSelectedCols; j++) {
                int colIndex = columns.get(j);
                if (colIndex < 0 || colIndex >= data[i].length) {
                    throw new IndexOutOfBoundsException(
                            "Column index " + colIndex + " out of bounds for data with " + data[i].length + " columns"
                    );
                }
                result[i][j] = data[i][colIndex];
            }
        }

        return result;
    }
}
