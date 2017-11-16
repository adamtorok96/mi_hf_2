package hu.adamtorok;

import java.util.ArrayList;

/**
 * Created by edems on 2017.11.01..
 */
public class Neuron {

    ArrayList<Weight> inputs;
    ArrayList<Weight> outputs;

    double bias;

    public Neuron(ArrayList<Weight> inputs, ArrayList<Weight> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.bias = 0;
    }
}
