package Problems;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DiabetesDataset {
    private final List<DiabetesPatient> patients;
    private final String[] featureNames;

    public DiabetesDataset(List<DiabetesPatient> patients, String[] featureNames) {
        this.patients = new ArrayList<>(patients);
        this.featureNames = featureNames.clone();
    }

    public static DiabetesDataset loadFromJson(String filepath) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(new FileReader(filepath), JsonObject.class);

        JsonArray featuresArray = jsonObject.getAsJsonArray("feature_names");
        String[] featureNames = new String[featuresArray.size()];
        for (int i = 0; i < featuresArray.size(); i++) {
            featureNames[i] = featuresArray.get(i).getAsString();
        }

        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        List<DiabetesPatient> patients = new ArrayList<>();

        for (JsonElement element : dataArray) {
            JsonObject patientObj = element.getAsJsonObject();
            JsonArray featuresArr = patientObj.getAsJsonArray("features");

            double[] features = new double[featuresArr.size()];
            for (int i = 0; i < featuresArr.size(); i++) {
                features[i] = featuresArr.get(i).getAsDouble();
            }

            int label = patientObj.get("label").getAsInt();
            patients.add(new DiabetesPatient(features, label));
        }

        return new DiabetesDataset(patients, featureNames);
    }

    public DatasetSplit split(double trainRatio) {
        List<DiabetesPatient> shuffled = new ArrayList<>(patients);
        Collections.shuffle(shuffled);

        int trainSize = (int) (patients.size() * trainRatio);

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
