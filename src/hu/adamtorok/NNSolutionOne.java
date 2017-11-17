package hu.adamtorok;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.Scanner;

public class NNSolutionOne {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();

        String inputs[] = line.split(",");

        int layerCounts[] = new int[inputs.length];

        // Parsing neuron counts
        for(int i = 0; i < inputs.length; i++) {
            layerCounts[i] = Integer.parseInt(inputs[i]);
        }

        ArrayList<Neuron> neurons = new ArrayList<>();

        NormalDistribution nd = new NormalDistribution(0, 0.1);

        /**
         * Layers
         */
        for(int i = 0; i < layerCounts.length; i++) {
            //System.out.println("Layer: " + i +" --> " + layerCounts[i]);

            // First neurons
            if( i == 0 ) {
                for(int j = 0; j < layerCounts[i]; j++) {
                    //System.out.println("\tNeuron: " + j);

                    neurons.add(new Neuron(null, null));
                }
            } // last neurons
            /*
            else if( i == layerCounts.length - 1) {
                for(int j = 0; j < layerCounts[i]; j++) {
                    //System.out.println("\tNeuron: " + j);

                    Neuron neuron = new Neuron(null, null);

                    ArrayList<Weight> weights = new ArrayList<>();

                    for(int k = 0; k < layerCounts[i - 1]; k++) {

                        int n = neurons.size() - layerCounts[i - 1] + k;

                        //System.out.println("\tback: " + n);

                        Weight weight = new Weight(
                                neurons.get(n),
                                null
                        );

                        weights.add(weight);

                        if( neurons.get(n).outputs == null )
                            neurons.get(n).outputs = new ArrayList<>();

                        neurons.get(n).outputs.add(weight);
                    }

                    neuron.inputs = weights;
                    neurons.add(neuron);
                }
            } */
            else {
                /**
                 * Neurons
                 */
                for(int j = 0; j < layerCounts[i]; j++) {
                    //System.out.println("\tNeuron: " + j);

                    Neuron neuron = new Neuron(null, null);

                    ArrayList<Weight> weights = new ArrayList<>();

                    for(int k = 0; k < layerCounts[i - 1]; k++) {

                        int n = neurons.size() - layerCounts[i - 1] + k;

                        //System.out.println("\tback: " + n);

                        Weight weight = new Weight(
                                neurons.get(n),
                                neuron
                        );

                        weight.weight = nd.sample();

                        weights.add(weight);

                        if( neurons.get(n).outputs == null )
                            neurons.get(n).outputs = new ArrayList<>();

                        neurons.get(n).outputs.add(weight);
                    }

                    neuron.inputs = weights;
                    neurons.add(neuron);
                }
            }

        }

        for(int i = 0; i < layerCounts.length; i++) {
            System.out.print(layerCounts[i]);

            if( i < layerCounts.length - 1 ) {
                System.out.print(',');
            }
        }

        System.out.print('\n');

        for(int i = layerCounts[0]; i < neurons.size(); i++) {
            Neuron neuron = neurons.get(i);
            int size = neuron.inputs.size();

            for(int j = 0; j < size; j++) {
                System.out.print(neuron.inputs.get(j).weight);
                System.out.print(',');
            }

            System.out.print(neurons.get(i).bias);

            if( i < neurons.size() - 1 ) {
                System.out.print('\n');
            }
        }
    }


}
