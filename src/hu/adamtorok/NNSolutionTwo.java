package hu.adamtorok;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by edems on 2017.11.16..
 */
public class NNSolutionTwo {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();

        String inputs[] = line.split(",");

        int layerCounts[] = new int[inputs.length];

        // Parsing neuron counts
        for(int i = 0; i < inputs.length; i++) {
            layerCounts[i] = Integer.parseInt(inputs[i]);
        }

        // Reading weights
        ArrayList<double[]> weightContainer = new ArrayList<>();

        for(int i = 0; i < layerCounts.length - 1; i++) {
            for(int l = 0; l < layerCounts[i + 1]; l++) {

                line = scanner.nextLine();

                inputs = line.split(",");

                double weights[] = new double[layerCounts[i] + 1];

//                System.out.println("size: " + weights.length);

                for(int j = 0; j < weights.length; j++) {
                    weights[j] = Double.parseDouble(inputs[j]);
//                    System.out.println(i +" -> " + j +": " + weights[j]);
                }

                weightContainer.add(weights);
            }
        }

        // Reading inputs
        ArrayList<double[]> inputContainer = new ArrayList<>();

        line = scanner.nextLine();
        int nInputs = Integer.parseInt(line);

        for(int i = 0; i < nInputs; i++) {
            line = scanner.nextLine();

            inputs = line.split(",");

            double in[] = new double[inputs.length];

            for(int j = 0; j < inputs.length; j++) {
                in[j] = Double.parseDouble(inputs[j]);
            }

            inputContainer.add(in);
        }


        ArrayList<Neuron> neurons = new ArrayList<>();

        /**
         * Layers
         */
        for(int i = 0; i < layerCounts.length; i++) {
            //System.out.println("Layer: " + i +" --> " + layerCounts[i]);

            // First neurons
            if( i == 0 ) {
                for(int j = 0; j < layerCounts[i]; j++) {
                    //System.out.println("\tNeuron: " + j);

                    neurons.add(new FirstLayerNeuron(null, 0));
                }
            } // last neurons
           else {
                /**
                 * Neurons
                 */
                for(int j = 0; j < layerCounts[i]; j++) {
                    //System.out.println("\tNeuron: " + j);

                    Neuron neuron = new Neuron(null, null);
                    neuron.bias = weightContainer.get(neurons.size() - layerCounts[0])[weightContainer.get(neurons.size() - layerCounts[0]).length - 1];
                    neuron.hidden = i != layerCounts.length - 1;

                    ArrayList<Weight> weights = new ArrayList<>();

                    for(int k = 0; k < layerCounts[i - 1]; k++) {

                        int n = neurons.size() - layerCounts[i - 1] + k - j;

                        //System.out.println("\tback: " + n);

                        Weight weight = new Weight(
                                neurons.get(n),
                                neuron
                        );

                        weight.weight = weightContainer.get(neurons.size() - layerCounts[0])[k];

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

/*
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
*/

        System.out.println(nInputs);

        for(int i = 0; i < nInputs; i++) {

            for(int j = 0; j < inputContainer.get(i).length; j++) {
                FirstLayerNeuron fln = (FirstLayerNeuron) neurons.get(j);
                fln.y = inputContainer.get(i)[j];

//                System.out.println(inputContainer.get(i)[j] + " == " + neurons.get(j).getY());
            }

            int lastLayerNeurons = layerCounts[layerCounts.length - 1];

            for(int j = 0; j < lastLayerNeurons; j++) {
                Neuron lastNeuron = neurons.get(neurons.size() - lastLayerNeurons + j);

                System.out.print(lastNeuron.getY());

                if( j < lastLayerNeurons - 1 )
                    System.out.print(',');
            }


            if( i < nInputs - 1) {
                System.out.print('\n');
            }
        }
    }
}
