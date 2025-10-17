package Validation;

import Core._Chromosome;

import java.util.HashSet;
import java.util.Set;

/**
 * Validator for integer representation: ensures no duplicates and valid range
 */
public class IntegerFeatureValidator implements IChromosomeValidator<Integer> {

    private final int minValue;
    private final int maxValue;
    private final int expectedLength;

    public IntegerFeatureValidator(int minValue, int maxValue, int expectedLength) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.expectedLength = expectedLength;
    }

    @Override
    public boolean isValid(_Chromosome<Integer> chromosome) {
        if (chromosome.getLength() != expectedLength) {
            return false;
        }

        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < chromosome.getLength(); i++) {
            Integer value = chromosome.getGene(i).getValue();

            // Check range
            if (value < minValue || value > maxValue) {
                return false;
            }

            // Check duplicates
            if (seen.contains(value)) {
                return false;
            }
            seen.add(value);
        }

        return true;
    }

    @Override
    public ValidationResult validate(_Chromosome<Integer> chromosome) {
        ValidationResult result = new ValidationResult(true);

        if (chromosome.getLength() != expectedLength) {
            result.addViolation("Invalid length: " + chromosome.getLength()
                    + ", expected: " + expectedLength);
            return new ValidationResult(false, result.getViolations());
        }

        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < chromosome.getLength(); i++) {
            Integer value = chromosome.getGene(i).getValue();

            if (value < minValue || value > maxValue) {
                result.addViolation("Value out of range at position " + i
                        + ": " + value);
            }

            if (seen.contains(value)) {
                result.addViolation("Duplicate value: " + value);
            }
            seen.add(value);
        }

        return result.getViolations().isEmpty() ?
                ValidationResult.valid() :
                new ValidationResult(false, result.getViolations());
    }
}
