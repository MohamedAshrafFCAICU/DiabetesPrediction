package Core;

public class BinaryGene implements _Gene<Boolean> {
    private Boolean value;

    public BinaryGene(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public _Gene<Boolean> copy() {
        return new BinaryGene(this.value);
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BinaryGene)) return false;
        BinaryGene other = (BinaryGene) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}