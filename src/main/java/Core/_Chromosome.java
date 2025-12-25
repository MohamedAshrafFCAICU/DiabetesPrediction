package Core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _Chromosome<T> implements Comparable<_Chromosome<T>> {
    private final List<_Gene<T>> _Genes;
    private double fitness;
    private boolean fitnessCalculated;

    public _Chromosome(List<_Gene<T>> _Genes) {
        this._Genes = new ArrayList<>(_Genes);
        this.fitness = 0.0;
        this.fitnessCalculated = false;
    }

    public List<_Gene<T>> getGenes() {
        return _Genes;
    }

    public _Gene<T> getGene(int index) {
        if (index < 0 || index >= _Genes.size()) {
            throw new IndexOutOfBoundsException("_Gene index out of bounds: " + index);
        }
        return _Genes.get(index);
    }

    public void setGene(int index, _Gene<T> _Gene) {
        if (index < 0 || index >= _Genes.size()) {
            throw new IndexOutOfBoundsException("_Gene index out of bounds: " + index);
        }
        _Genes.set(index, _Gene);
        invalidateFitness();
    }

    public int getLength() {
        return _Genes.size();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        this.fitnessCalculated = true;
    }

    public boolean isFitnessCalculated() {
        return fitnessCalculated;
    }

    public void invalidateFitness() {
        this.fitnessCalculated = false;
    }

    public _Chromosome<T> copy() {
        List<_Gene<T>> copied_Genes = _Genes.stream()
                .map(_Gene::copy)
                .collect(Collectors.toList());
        _Chromosome<T> copy = new _Chromosome<>(copied_Genes);
        if (this.fitnessCalculated) {
            copy.setFitness(this.fitness);
        }
        return copy;
    }

    @Override
    public int compareTo(_Chromosome<T> other) {
        return Double.compare(this.fitness, other.fitness);
    }

    @Override
    public String toString() {
        return String.format("_Chromosome{fitness=%.2f, _Genes=%s}",
                fitness,
                _Genes.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof _Chromosome)) return false;
        _Chromosome<?> other = (_Chromosome<?>) obj;
        return _Genes.equals(other._Genes);
    }

    @Override
    public int hashCode() {
        return _Genes.hashCode();
    }
}