package hu.adamtorok;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by edems on 2017.11.16..
 */
public class NNSolutionThree {

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


        neurons.get(neurons.size() - 1).delta = 1.0;

        for(int j = 0; j < inputContainer.get(0).length; j++) {
            FirstLayerNeuron fln = (FirstLayerNeuron) neurons.get(j);
            fln.y = inputContainer.get(0)[j];
        }

        for(int l = layerCounts.length - 2; l >= 0; l--) {
//            System.out.println("layer: " + l);

            int offset = 0;

            for(int i = 0; i < l; i++) {
                offset += layerCounts[i];
            }

            for(int i = 0; i < layerCounts[l]; i++) {
                Neuron neuron = neurons.get(offset + i);

                double sum = 0;

                for(Weight w : neuron.outputs) {
                    sum += w.output.delta * w.weight;
                }

                double s = neuron.getSum();

//                System.out.println("n: "+ (s > 0 ? 1 : 0) +"->" + s);

                neuron.delta = sum * (s > 0 ? 1 : 0);
            }
        }

        for(int i = layerCounts[0]; i < neurons.size(); i++) {
            Neuron neuron = neurons.get(i);

            for(Weight w : neuron.inputs) {
                w.dWeight = neuron.delta * w.input.getY();
            }

            neuron.dBias = neuron.delta;
//            System.out.println(neuron.dBias);
        }

        for(int i = 0; i < layerCounts.length; i++) {
            System.out.print(layerCounts[i]);

            if( i < layerCounts.length - 1 ) {
                System.out.print(',');
            }
        }

        System.out.print('\n');

        for(int i = 0; i < nInputs; i++) {

            for(int j = 0; j < inputContainer.get(i).length; j++) {
                FirstLayerNeuron fln = (FirstLayerNeuron) neurons.get(j);
                fln.y = inputContainer.get(i)[j];
            }

            neurons.get(neurons.size() - 1).getY();

            for(int j = layerCounts[0]; j < neurons.size(); j++) {
                Neuron neuron = neurons.get(j);

                for(int k = 0; k < neuron.inputs.size(); k++) {
                    System.out.print(neuron.inputs.get(k).dWeight);
                    System.out.print(',');
                }

                System.out.print(neuron.dBias);

                if( j < neurons.size()  - 1 )
                    System.out.print('\n');
            }


            if( i < nInputs - 1) {
                System.out.print('\n');
            }
        }
    }
}
