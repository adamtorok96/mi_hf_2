package hu.adamtorok;


/**
 * Created by edems on 2017.11.01..
 */
public class Weight {

    Neuron input;
    Neuron output;

    double weight;
    double dWeight;

    Weight(Neuron input, Neuron output) {
        this.input = input;
        this.output = output;
        this.weight = 0;

        if(input == null || output == null )
            throw new NullPointerException("weight");
    }
}
