package Core;

public class IntegerGene implements _Gene<Integer> {
    private Integer value;

    public IntegerGene(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public _Gene<Integer> copy() {
        return new IntegerGene(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IntegerGene)) return false;
        IntegerGene other = (IntegerGene) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
