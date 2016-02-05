package nnTest;

import java.util.stream.Stream;

public interface Outputable {
  double[] getOutput(double[] input);
  int getInputSize();
  int getOutputSize();

  default double getCost(double[] input, double[] expectedOutput){
    if(input.length != getInputSize()) throw new IllegalArgumentException("Input is of size " + input.length + ", not of expected size " + getInputSize());
    if(expectedOutput.length != getOutputSize()) throw new IllegalArgumentException("ExpectedOutput is of size " + expectedOutput.length + ", not of expected size " + getOutputSize());
    double[] output = getOutput(input);
    if(output.length != getOutputSize()) throw new IllegalArgumentException("Output is of size " + output.length + ", not of expected size " + getOutputSize());
    double total = 0.0;
    for (int i = 0; i < getOutputSize(); i++) {
      total += Math.pow(expectedOutput[i] - output[i], 2);
    }
    return total;
  }

  default double getCost(DataPoint dataPoint) {
    return this.getCost(dataPoint.inputs, dataPoint.outputs);
  }

  default double getTotalCost(DataPoint[] dataPoints) {
    return Stream.of(dataPoints).parallel().mapToDouble(this::getCost).sum();
  }
}

