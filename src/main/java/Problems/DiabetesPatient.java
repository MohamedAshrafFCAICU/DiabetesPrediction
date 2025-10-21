package Problems;

public class DiabetesPatient {
    private final double[] features;
    private final int label;

    public DiabetesPatient(double[] features, int label) {
        this.features = features.clone();
        this.label = label;
    }

    public double[] getFeatures() {
        return features.clone();
    }

    public int getLabel() {
        return label;
    }

    public double getFeature(int index) {
        return features[index];
    }

    public int getFeatureCount() {
        return features.length;
    }
}
