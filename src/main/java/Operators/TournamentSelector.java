package Operators;

import Core._Chromosome;
import Core._Population;
import OperatorsContracts.ISelector;

import java.util.ArrayList;
import java.util.List;
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
         if(population == null) {
             throw new IllegalArgumentException("Population cannot be null");
         }

         int populationSize = population.getSize();
         if(populationSize ==0) {
             throw new IllegalStateException("Population is empty");
         }

         int actualTournamentSize = Math.min(populationSize, tournamentSize);

         List<_Chromosome<T>> tournament = new ArrayList<>(actualTournamentSize);

         for(int i = 0; i < actualTournamentSize; i++) {
             int rand = random.nextInt(populationSize);
             tournament.add(population.getChromosome(rand));
         }

         return tournament.stream().max(_Chromosome::compareTo).orElseThrow(() -> new IllegalStateException("Tournament Selection Failed"));
    }

    public int getTournamentSize() {
        return tournamentSize;
    }
}
