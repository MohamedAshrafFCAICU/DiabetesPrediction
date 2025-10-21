package Problems;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class DiabetesDataset {
    private final List<DiabetesPatient> patients;
    private final String[] featureNames;

    public DiabetesDataset(List<DiabetesPatient> patients, String[] featureNames) {
        this.patients = new ArrayList<>(patients);
        this.featureNames = featureNames.clone();
    }

    public static DiabetesDataset loadFromCsv(String filepath) throws IOException {
        List<DiabetesPatient> patients = new ArrayList<>();
        String[] featureNames = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] values = line.split(",");

                if (isFirstLine) {
                    featureNames = new String[values.length - 1];
                    System.arraycopy(values, 0, featureNames, 0, values.length - 1);
                    isFirstLine = false;
                } else {
                    double[] features = new double[values.length - 1];
                    for (int i = 0; i < values.length - 1; i++) {
                        features[i] = Double.parseDouble(values[i].trim());
                    }

                    String labelStr = values[values.length - 1].trim();
                    int label;

                    try {
                        double labelDouble = Double.parseDouble(labelStr);
                        label = (int) Math.round(labelDouble);
                    } catch (NumberFormatException e) {
                        label = Integer.parseInt(labelStr);
                    }
                    patients.add(new DiabetesPatient(features, label));
                }
            }
        }

        if (featureNames == null || patients.isEmpty()) {
            throw new IOException("CSV file is empty or has invalid format");
        }

        return new DiabetesDataset(patients, featureNames);
    }


    public DatasetSplit split(double trainRatio) {
        if (trainRatio <= 0.0 || trainRatio >= 1.0) {
            throw new IllegalArgumentException("trainRatio must be between 0 and 1 (exclusive)");
        }

        List<DiabetesPatient> shuffled = new ArrayList<>(patients);
        Collections.shuffle(shuffled, new Random(42));

        int trainSize = (int) (patients.size() * trainRatio);

        if (trainSize == 0 || trainSize == patients.size()) {
            throw new IllegalArgumentException(
                    "trainRatio " + trainRatio + " results in empty train or test set"
            );
        }

        List<DiabetesPatient> trainSet = shuffled.subList(0, trainSize);
        List<DiabetesPatient> testSet = shuffled.subList(trainSize, shuffled.size());

        return new DatasetSplit(trainSet, testSet, featureNames);
    }

    public List<DiabetesPatient> getPatients() {
        return new ArrayList<>(patients);
    }

    public String[] getFeatureNames() {
        return featureNames.clone();
    }

    public int getSize() {
        return patients.size();
    }

    public int getFeatureCount() {
        return featureNames.length;
    }


    public static class DatasetSplit {
        private final List<DiabetesPatient> trainSet;
        private final List<DiabetesPatient> testSet;
        private final String[] featureNames;

        public DatasetSplit(List<DiabetesPatient> trainSet,
                            List<DiabetesPatient> testSet,
                            String[] featureNames) {
            this.trainSet = new ArrayList<>(trainSet);
            this.testSet = new ArrayList<>(testSet);
            this.featureNames = featureNames.clone();
        }

        public List<DiabetesPatient> getTrainSet() {
            return trainSet;
        }

        public List<DiabetesPatient> getTestSet() {
            return testSet;
        }

        public String[] getFeatureNames() {
            return featureNames;
        }

        public double[][] getTrainingFeatures() {
            return extractFeatures(trainSet);
        }

        public int[] getTrainingLabels() {
            return extractLabels(trainSet);
        }

        public double[][] getTestFeatures() {
            return extractFeatures(testSet);
        }

        public int[] getTestLabels() {
            return extractLabels(testSet);
        }

        private double[][] extractFeatures(List<DiabetesPatient> patients) {
            double[][] features = new double[patients.size()][];
            for (int i = 0; i < patients.size(); i++) {
                features[i] = patients.get(i).getFeatures();
            }
            return features;
        }

        private int[] extractLabels(List<DiabetesPatient> patients) {
            int[] labels = new int[patients.size()];
            for (int i = 0; i < patients.size(); i++) {
                labels[i] = patients.get(i).getLabel();
            }
            return labels;
        }
    }
}
