package hu.adamtorok;

import java.util.ArrayList;

/**
 * Created by edems on 2017.11.01..
 */
public class Neuron {

    private static int N = 0;

    ArrayList<Weight> inputs;
    ArrayList<Weight> outputs;

    double bias, y;
    boolean hasY = false;

    boolean hidden = false;

    private int n = 0;

    public Neuron(ArrayList<Weight> inputs, ArrayList<Weight> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.bias = 0;

        this.n = N++;
    }

    private double activating(double x) {
        return hidden
                ? (0 > x ? 0 : x)
                : x
        ;
    }

    protected double getY() {
        if( hasY )
            return y;

        y = bias;

//        System.out.println("size: "+ inputs.size());
//        for (Weight input : inputs) {
//            System.out.println("\tIN(" + n + " = " + input.output.n + "): " + input.input.n +" w: " + input.weight);
//        }

        for (Weight input : inputs) {
            y += input.weight * input.input.getY();
        }

        hasY = true;

        return this.y = activating(y);
    }
}
