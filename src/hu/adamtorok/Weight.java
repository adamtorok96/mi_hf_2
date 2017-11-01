package hu.adamtorok;

/**
 * Created by edems on 2017.11.01..
 */
public class Weight {

    Neuron input;
    Neuron output;
    float weight;

    Weight(Neuron input, Neuron output, float weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }
}
