package hu.adamtorok;

/**
 * Created by edems on 2017.11.01..
 */
public class Weight {

    Neuron input;
    Neuron output;
    float weight;

    Weight(Neuron input, Neuron output) {
        this.input = input;
        this.output = output;
        this.weight = normal();
    }

    public static float normal()
    {
        double z = Math.sqrt(0.1);

        return (float)(
                (1.0f / (z * Math.sqrt(2 * Math.PI))) *
                        Math.exp(
                                (-1) * (
                                        Math.pow(0.5 - Math.random(), 2) / (2 * Math.pow(z, 2))
                                )
                        )
        );
    }
}
