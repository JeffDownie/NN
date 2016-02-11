package nnTest.parser;

import java.util.Arrays;

public class DataPoint {
  public final double[] inputs, outputs;
  public final int inputSize, outputSize;

  DataPoint(double output, double... inputs){
    this(new double[] {output}, inputs);
  }

  DataPoint(double[] outputs, double... inputs) {
    this.inputs = inputs;
    this.outputs = outputs;
    this.inputSize = inputs.length;
    this.outputSize = outputs.length;
  }

  @Override
  public String toString() {
    return "Inputs: " + Arrays.toString(inputs) + " Outputs: " + Arrays.toString(outputs);
  }
}
