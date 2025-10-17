package OperatorsContracts;

import Core._Chromosome;

public interface IMutator<T> {

    void mutate(_Chromosome<T> chromosome);

    void setMutationRate(double rate);

    double getMutationRate();
}