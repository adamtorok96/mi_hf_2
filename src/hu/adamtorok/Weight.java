package hu.adamtorok;

import org.apache.commons.math3.distribution.NormalDistribution;


/**
 * Created by edems on 2017.11.01..
 */
public class Weight {

    Neuron input;
    Neuron output;
    double weight;

    Weight(Neuron input, Neuron output) {
        this.input = input;
        this.output = output;

        NormalDistribution nd = new NormalDistribution(0, 0.1);

        this.weight = nd.sample();
    }
}
