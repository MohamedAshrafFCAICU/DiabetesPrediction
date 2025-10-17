package Validation;

import Core._Chromosome;

public class DoubleFeatureValidator implements IChromosomeValidator<Double> {

    private final double minValue;
    private final double maxValue;

    public DoubleFeatureValidator(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean isValid(_Chromosome<Double> chromosome) {
        for (int i = 0; i < chromosome.getLength(); i++) {
            Double value = chromosome.getGene(i).getValue();
            if (value < minValue || value > maxValue || value.isNaN() || value.isInfinite()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ValidationResult validate(_Chromosome<Double> chromosome) {
        ValidationResult result = new ValidationResult(true);

        for (int i = 0; i < chromosome.getLength(); i++) {
            Double value = chromosome.getGene(i).getValue();

            if (value.isNaN()) {
                result.addViolation("NaN value at position " + i);
            } else if (value.isInfinite()) {
                result.addViolation("Infinite value at position " + i);
            } else if (value < minValue || value > maxValue) {
                result.addViolation("Value out of range at position " + i
                        + ": " + value + " not in [" + minValue + ", " + maxValue + "]");
            }
        }

        return result.getViolations().isEmpty() ?
                ValidationResult.valid() :
                new ValidationResult(false, result.getViolations());
    }

    @Override
    public boolean repair(_Chromosome<Double> chromosome) {
        boolean repaired = false;
        for (int i = 0; i < chromosome.getLength(); i++) {
            Double value = chromosome.getGene(i).getValue();

            if (value.isNaN() || value.isInfinite()) {
                chromosome.getGene(i).setValue((minValue + maxValue) / 2.0);
                repaired = true;
            } else if (value < minValue) {
                chromosome.getGene(i).setValue(minValue);
                repaired = true;
            } else if (value > maxValue) {
                chromosome.getGene(i).setValue(maxValue);
                repaired = true;
            }
        }

        if (repaired) {
            chromosome.invalidateFitness();
        }

        return repaired;
    }
}
