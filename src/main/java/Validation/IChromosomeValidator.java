package Validation;

import Core._Chromosome;

public interface IChromosomeValidator<T> {


    boolean isValid(_Chromosome<T> chromosome);

    ValidationResult validate(_Chromosome<T> chromosome);

    default boolean repair(_Chromosome<T> chromosome) {
        return false; // Default: no repair
    }
}
