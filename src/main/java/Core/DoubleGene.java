package Core;

public class DoubleGene implements _Gene<Double> {
    private Double value;

    public DoubleGene(Double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public _Gene<Double> copy() {
        return new DoubleGene(this.value);
    }

    @Override
    public String toString() {
        return String.format("%.4f", value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DoubleGene)) return false;
        DoubleGene other = (DoubleGene) obj;
        return Math.abs(value - other.value) < 1e-10;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
