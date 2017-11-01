package hu.adamtorok;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();

        String inputs[] = line.split(",");

        int layerCounts[] = new int[inputs.length];

        // Parsing neuron counts
        for(int i = 0; i < inputs.length; i++) {
            layerCounts[i] = Integer.parseInt(inputs[i]);
        }

//        ArrayList<float[]> weightContainer = new ArrayList<>();
//
//        for(int i = 0; i < layerCounts.length - 1; i++) {
//            line = scanner.nextLine();
//
//            inputs = line.split(",");
//
//            float weights[] = new float[layerCounts[i] + 1];
//
//            System.out.println("size: " + weights.length);
//
//            for(int j = 0; j < weights.length; j++) {
//                weights[j] = Float.parseFloat(inputs[j]);
//                System.out.println(i +" -> " + j +": " + weights[j]);
//            }
//        }

        ArrayList<Neuron> neurons = new ArrayList<>();

        /**
         * Layers
         */
        for(int i = 0; i < layerCounts.length; i++) {
            System.out.println("Layer: " + i +" --> " + layerCounts[i]);

            // First neurons
            if( i == 0 ) {
                for(int j = 0; j < layerCounts[i]; j++) {
                    System.out.println("\tNeuron: " + j);

                    neurons.add(new Neuron(null, null));
                }
            } // last neurons
            else if( i == layerCounts.length - 1) {
                for(int j = 0; j < layerCounts[i]; j++) {
                    System.out.println("\tNeuron: " + j);

                    Neuron neuron = new Neuron(null, null);

                    ArrayList<Weight> weights = new ArrayList<>();

                    for(int k = 0; k < layerCounts[i - 1]; k++) {

                        int n = neurons.size() - layerCounts[i - 1] + k;

                        System.out.println("\tback: " + n);

                        weights.add(
                                new Weight(
                                        neurons.get(n),
                                        null,
                                        0
                                )
                        );
                    }

                    neuron.inputs = weights;
                    neurons.add(neuron);
                }
            } else {
                /**
                 * Neurons
                 */
                for(int j = 0; j < layerCounts[i]; j++) {
                    System.out.println("\tNeuron: " + j);

                    Neuron neuron = new Neuron(null, null);

                    ArrayList<Weight> weights = new ArrayList<>();

                    for(int k = 0; k < layerCounts[i - 1]; k++) {

                        int n = neurons.size() - layerCounts[i - 1] + k;

                        System.out.println("\tback: " + n);

                        weights.add(
                                new Weight(
                                        neurons.get(n),
                                        neuron,
                                        0
                                )
                        );
                    }

                    neuron.inputs = weights;
                    neurons.add(neuron);
                }
            }

        }
    }
}
