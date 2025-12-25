package Validation;

import Core._Chromosome;

public class BinaryFeatureValidator implements IChromosomeValidator<Boolean> {

    private final int minFeatures;
    private final int maxFeatures;

    public BinaryFeatureValidator(int minFeatures, int maxFeatures) {
        this.minFeatures = minFeatures;
        this.maxFeatures = maxFeatures;
    }

    @Override
    public boolean isValid(_Chromosome<Boolean> chromosome) {
        int count = countSelectedFeatures(chromosome);
        return count >= minFeatures && count <= maxFeatures;
    }

    @Override
    public ValidationResult validate(_Chromosome<Boolean> chromosome) {
        int count = countSelectedFeatures(chromosome);

        if (count < minFeatures) {
            return ValidationResult.invalid(
                    "Too few features: " + count + " < " + minFeatures);
        }

        if (count > maxFeatures) {
            return ValidationResult.invalid(
                    "Too many features: " + count + " > " + maxFeatures);
        }

        return ValidationResult.valid();
    }

    @Override
    public boolean repair(_Chromosome<Boolean> chromosome) {
        int count = countSelectedFeatures(chromosome);

        if (count > maxFeatures) {
            // Remove excess features randomly
            int toRemove = count - maxFeatures;
            removeRandomFeatures(chromosome, toRemove);
            chromosome.invalidateFitness();
            return true;
        }

        if (count < minFeatures) {
            // Add missing features randomly
            int toAdd = minFeatures - count;
            addRandomFeatures(chromosome, toAdd);
            chromosome.invalidateFitness();
            return true;
        }

        return false;
    }

    private int countSelectedFeatures(_Chromosome<Boolean> chromosome) {
        int count = 0;
        for (int i = 0; i < chromosome.getLength(); i++) {
            if (chromosome.getGene(i).getValue()) {
                count++;
            }
        }
        return count;
    }

    private void removeRandomFeatures(_Chromosome<Boolean> chromosome, int count) {
        int removed = 0;
        for (int i = 0; i < chromosome.getLength() && removed < count; i++) {
            if (chromosome.getGene(i).getValue()) {
                chromosome.getGene(i).setValue(false);
                removed++;
            }
        }
    }

    private void addRandomFeatures(_Chromosome<Boolean> chromosome, int count) {
        int added = 0;
        for (int i = 0; i < chromosome.getLength() && added < count; i++) {
            if (!chromosome.getGene(i).getValue()) {
                chromosome.getGene(i).setValue(true);
                added++;
            }
        }
    }
}
