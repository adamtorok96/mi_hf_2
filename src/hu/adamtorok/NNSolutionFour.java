package hu.adamtorok;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by edems on 2017.11.17..
 */
public class NNSolutionFour {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        // reading learning params
        String line = scanner.nextLine();

        String inputs[] = line.split(",");

        int E = Integer.parseInt(inputs[0]);
        double braveness = Double.parseDouble(inputs[1]);
        double R = Double.parseDouble(inputs[2]);


        // reading arch
        line = scanner.nextLine();

        inputs = line.split(",");

        int layerCounts[] = new int[inputs.length];

        // Parsing neuron counts
        for(int i = 0; i < inputs.length; i++) {
            layerCounts[i] = Integer.parseInt(inputs[i]);
        }

        int N = layerCounts[0];
        int M = layerCounts[layerCounts.length - 1];

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
        int nInputs = Integer.parseInt(line); // S

        for(int i = 0; i < nInputs; i++) {
            line = scanner.nextLine();

            inputs = line.split(",");

            double in[] = new double[inputs.length];

            for(int j = 0; j < inputs.length; j++) {
                in[j] = Double.parseDouble(inputs[j]);
            }

            inputContainer.add(in);
        }

        int St = (int)Math.floor(nInputs * R);

//        System.out.println("nLearningInputs: " + St);
//        System.out.println("nValidationInputs: " + (nInputs - St));
//        System.out.println("inputs: " + nInputs);


        // build network
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




        for(int e = 0; e < E; e++) {


            for(int s = 0; s < St; s++) {
                // set inputs
                for(int j = 0; j < N; j++) {
                    FirstLayerNeuron fln = (FirstLayerNeuron) neurons.get(j);
                    fln.y = inputContainer.get(s)[j];
                }

//            double err = 0.0;
                int inps = 0;
                for(int j = neurons.size() - layerCounts[layerCounts.length - 1]; j < neurons.size(); j++) {
                    double Y = neurons.get(j).getY();
                    double shouldBe = inputContainer.get(s)[N + inps++];
                    double error = shouldBe - Y;//                err = (error * braveness * 2);

//            System.out.println("Y: " + Y +" --> "+ shouldBe);
//            System.out.println("Error: " + error);
//            System.out.println("error * mu * 2 = " + err);
//
//            System.out.println();

                    neurons.get(j).delta = error;
                }

                // deriv
                for(int l = layerCounts.length - 2; l >= 0; l--) {

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

                        double su = neuron.getSum();

                        neuron.delta = sum * (su > 0 ? 1 : 0);
                    }
                }

                for(int i = layerCounts[0]; i < neurons.size(); i++) {
                    Neuron neuron = neurons.get(i);

                    for(Weight w : neuron.inputs) {
                        w.dWeight = neuron.delta * w.input.getY();
                    }

                    neuron.dBias = neuron.delta;
                }

            /*
            // print der
            System.out.println("der:");
            for(int j = layerCounts[0]; j < neurons.size(); j++) {
                Neuron neuron = neurons.get(j);

                for(int k = 0; k < neuron.inputs.size(); k++) {
                    System.out.print(neuron.inputs.get(k).dWeight);
                    System.out.print(',');
                }

                System.out.print(neuron.dBias);

                if( j < neurons.size() )
                    System.out.print('\n');
            }
*/

                // correcting
                for(Neuron neuron : neurons) {
                    if( neuron.inputs != null ) {
                        for(Weight weight : neuron.inputs) {
//                        weight.weight += weight.dWeight * err;
                            weight.weight += 2 * braveness * weight.dWeight;
                        }

//                    neuron.bias += neuron.dBias * err;
                        neuron.bias += 2 * braveness * neuron.dBias;
                    }
                }

/*
            System.out.println("weights:");

            for(int i = layerCounts[0]; i < neurons.size(); i++) {
                Neuron neuron = neurons.get(i);
                int size = neuron.inputs.size();

                for(int j = 0; j < size; j++) {
                    System.out.print(neuron.inputs.get(j).weight);
                    System.out.print(',');
                }

                System.out.print(neurons.get(i).bias);

                if( i < neurons.size() ) {
                    System.out.print('\n');
                }
            }


            System.out.println();*/
            }

            double meanSquareError = 0.0;

            // validation
            for(int v = St; v < nInputs; v++) {
//            System.out.println("v: " + v);

                for(int j = 0; j < N; j++) {
                    FirstLayerNeuron fln = (FirstLayerNeuron) neurons.get(j);
                    fln.y = inputContainer.get(v)[j];
                }

                int inps = 0;
                double mse = 0.0;

                for(int j = neurons.size() - layerCounts[layerCounts.length - 1]; j < neurons.size(); j++) {
                    double Y = neurons.get(j).getY();
                    double shouldBe = inputContainer.get(v)[N + inps++];
                    double error = shouldBe - Y;

                    mse += Math.pow(error, 2);
                }

                mse /= layerCounts[layerCounts.length - 1];

                meanSquareError += mse;
            }

            meanSquareError /= ((nInputs - St));

            System.out.println(meanSquareError);
        }

//        System.out.println("MSE: " + meanSquareError);


        // print arch
        for(int i = 0; i < layerCounts.length; i++) {
            System.out.print(layerCounts[i]);

            if( i < layerCounts.length - 1 ) {
                System.out.print(',');
            }
        }

        System.out.print('\n');

        // Print weights
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
