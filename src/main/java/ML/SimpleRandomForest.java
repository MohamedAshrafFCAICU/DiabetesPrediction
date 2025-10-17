package ML;

import smile.classification.RandomForest;
import smile.data.formula.Formula;


                    /*
                        TODO BY AHMED KAMEL
                    */

public class SimpleRandomForest {
    private RandomForest model;
    private int numTrees = 100;

    public SimpleRandomForest() {
    }

    public SimpleRandomForest(int numTrees) {
        this.numTrees = numTrees;
    }


    public void train(double[][] features, int[] labels) {

    }


    public int[] predict(double[][] features) {
       return null;
    }


    public double accuracy(double[][] features, int[] actualLabels) {
        return 0.0;
    }

    private smile.data.DataFrame createDataFrame(double[][] features, int[] labels) {
        return null;
    }
}
