package OperatorsContracts;

import Core._Chromosome;
import Core._Population;

import java.util.List;
import java.util.stream.Stream;

public interface ISelector<T> {


    _Chromosome<T> select(_Population<T> population);

    default List<_Chromosome<T>> selectMultiple(_Population<T> population, int count) {
        return Stream.generate(() -> select(population))
                .limit(count)
                .toList();
    }
}
