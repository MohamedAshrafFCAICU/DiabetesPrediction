package Operators;

import Core._Chromosome;
import Core._Population;
import OperatorsContracts.ISelector;

import java.util.Random;

public class TournamentSelector<T> implements ISelector<T> {
    private final int tournamentSize;
    private final Random random;

    public TournamentSelector(int tournamentSize) {
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException("Tournament size must be positive");
        }
        this.tournamentSize = tournamentSize;
        this.random = new Random();
    }

    public TournamentSelector(int tournamentSize, long seed) {
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException("Tournament size must be positive");
        }
        this.tournamentSize = tournamentSize;
        this.random = new Random(seed);
    }

    @Override
    public _Chromosome<T> select(_Population<T> population) {
         /*TODO
              by Husam Abozide
        */

        return  null;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }
}
