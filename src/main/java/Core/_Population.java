package Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class _Population<T> {
    private List<_Chromosome<T>> chromosomes;

    public _Population(List<_Chromosome<T>> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
    }

    public _Population(int size) {
        this.chromosomes = new ArrayList<>(size);
    }

    public void addChromosome(_Chromosome<T> chromosome) {
        chromosomes.add(chromosome);
    }

    public void addChromosomes(List<_Chromosome<T>> chromosomes) {
        this.chromosomes.addAll(chromosomes);
    }

    public _Chromosome<T> getChromosome(int index) {
        if (index < 0 || index >= chromosomes.size()) {
            throw new IndexOutOfBoundsException("Chromosome index out of bounds: " + index);
        }
        return chromosomes.get(index);
    }

    public List<_Chromosome<T>> getChromosomes() {
        return new ArrayList<>(chromosomes);
    }

    public void setChromosomes(List<_Chromosome<T>> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
    }

    public int getSize() {
        return chromosomes.size();
    }

    public _Chromosome<T> getBestChromosome() {
        if (chromosomes.isEmpty()) {
            throw new IllegalStateException("_Population is empty");
        }
        return Collections.max(chromosomes);
    }

    public _Chromosome<T> getWorstChromosome() {
        if (chromosomes.isEmpty()) {
            throw new IllegalStateException("_Population is empty");
        }
        return Collections.min(chromosomes);
    }

    public double getTotalFitness() {
        if (chromosomes.isEmpty()) {
            return 0.0;
        }
        return chromosomes.stream()
                .mapToDouble(_Chromosome::getFitness)
                .sum();
    }

    public double getAverageFitness() {
        if (chromosomes.isEmpty()) {
            return 0.0;
        }
        return chromosomes.stream()
                .mapToDouble(_Chromosome::getFitness)
                .average()
                .orElse(0.0);
    }

    public double getBestFitness() {
        if (chromosomes.isEmpty()) {
            return 0.0;
        }
        return getBestChromosome().getFitness();
    }

    public void sortByFitness() {
        chromosomes.sort(Collections.reverseOrder());
    }

    @Override
    public String toString() {
        return String.format("_Population{size=%d, avgFitness=%.2f, bestFitness=%.2f}",
                chromosomes.size(),
                getAverageFitness(),
                chromosomes.isEmpty() ? 0.0 : getBestFitness());
    }
}
