package hu.adamtorok;

import java.util.ArrayList;

/**
 * Created by edems on 2017.11.16..
 */
public class FirstLayerNeuron extends Neuron {

    double y = 0;

    public FirstLayerNeuron(ArrayList<Weight> outputs, double y) {
        super(null, outputs);
        this.y = y;
    }

    @Override
    protected double getSum() {
        return 0;
    }

    @Override
    protected double getY() {
        return this.y;
    }
}
