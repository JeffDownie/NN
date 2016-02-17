package nnTest.network.impl;

import nnTest.network.api.Delta;
import nnTest.network.api.GradientModifiable;
import nnTest.network.api.RandomModifiable;
import nnTest.network.api.SingleOutput;

import java.util.Arrays;

public class Gate implements RandomModifiable<Gate, Gate.GateDelta>, SingleOutput, GradientModifiable<Gate, Gate.GateDelta> {
  static final double maxDelta = 0.05;
  int inputSize;
  double[] weights;
  double bias;

  public Gate(int inputSize) {
    this.inputSize = inputSize;
    bias = Math.random() - 0.5;
    weights = new double[inputSize];
    for (int i = 0; i < inputSize; i++) {
      weights[i] = Math.random() - 0.5;
    }
  }

  private Gate(int inputSize, double bias, double... weights) {
    if(weights.length != inputSize) throw new IllegalArgumentException();
    this.inputSize = inputSize;
    this.bias = bias;
    this.weights = weights;
  }

  @Override
  public double getSingleOutput(double[] inputs) {
    if(inputs.length != this.inputSize) throw new IllegalArgumentException("Input size does not agree with gate size.");
    double weightedInputSum = this.bias;
    for (int i = 0; i < this.inputSize; i++) {
      weightedInputSum += inputs[i]*weights[i];
    }
    return sigmoid(weightedInputSum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.inputSize; i++) {
      sb.append(weights[i]);
      sb.append(", ");
    }
    return sb.toString() + "bias: " + bias;
  }

  @Override
  public Gate applyDelta(final GateDelta delta) {
    double[] newWeights = new double[inputSize];
    System.arraycopy(weights, 0, newWeights, 0, inputSize);
    for (int i = 0; i < delta.deltas.length; i++) {
      newWeights[i] += delta.deltas[i];
    }
    return new Gate(inputSize, bias + delta.biasDelta, newWeights);
  }

  @Override
  public GateDelta createRandomDelta() {
    return new GateDelta(this);
  }

  @Override
  public int getInputSize() {
    return this.inputSize;
  }

  @Override
  public GateDelta getBestDelta(final double[] input, final double[] expectedOutput) {
    if(expectedOutput.length != 1) throw new IllegalArgumentException("ExpectedOutput size is not 1, instead is " + expectedOutput.length);
    double output = this.getSingleOutput(input);
    double outputDifference = expectedOutput[0] - output;

    double biasDelta = sigmoidGradient(this.bias) * outputDifference;
    double[] weightDeltas = new double[this.inputSize];
    for (int i = 0; i < this.inputSize; i++) {
      weightDeltas[i] = sigmoidGradient(this.weights[i]) * outputDifference;
    }

    return new GateDelta(biasDelta, weightDeltas);
  }

  private static double sigmoidGradient(double input) {
    double val = sigmoid(input);
    return val * (1 - val);
  }

  private static double sigmoid(double input) {
    return 1.0 / (1.0 + Math.exp(-input));
  }

  public static class GateDelta implements Delta<Gate, GateDelta> {
    private double biasDelta;
    private double[] deltas;

    public GateDelta(int size) {
      this(0.0, new double[size]);
    }

    private GateDelta(Gate gate) {
      int modifiedWeight = (int) Math.floor(Math.random() * (1 + gate.inputSize));
      double delta = (Math.random() - 0.5) * maxDelta;

      if(modifiedWeight == gate.inputSize) {
        this.biasDelta = delta;
        this.deltas = new double[gate.inputSize];
        for (int i = 0; i < deltas.length; i++) {
          deltas[i] = 0.0;
        }
      } else {
        this.biasDelta = 0.0;
        this.deltas = new double[gate.inputSize];
        for (int i = 0; i < deltas.length; i++) {
          deltas[i] = 0.0;
        }
        this.deltas[modifiedWeight] = delta;
      }
    }

    private GateDelta(double biasDelta, double[] weightDeltas) {
      this.biasDelta = biasDelta;
      this.deltas = weightDeltas;
    }

    @Override
    public String toString() {
      return "{" + biasDelta + "," + Arrays.toString(deltas) + "}";
    }

    @Override
    public GateDelta scale(final double scalar) {
      double[] newWeightDeltas = new double[deltas.length];
      System.arraycopy(deltas, 0, newWeightDeltas, 0, deltas.length);
      for (int i = 0; i < deltas.length; i++) {
        newWeightDeltas[i] *= scalar;
      }
      return new GateDelta(biasDelta * scalar, newWeightDeltas);
    }

    @Override
    public GateDelta add(final GateDelta addTo) {
      if(addTo.deltas.length != this.deltas.length) throw new IllegalArgumentException("Gates are not of same size:" + this + "," + addTo);
      double[] newWeightDeltas = new double[deltas.length];
      System.arraycopy(deltas, 0, newWeightDeltas, 0, deltas.length);
      for (int i = 0; i < deltas.length; i++) {
        newWeightDeltas[i] += addTo.deltas[i];
      }
      return new GateDelta(biasDelta * addTo.biasDelta, newWeightDeltas);
    }
  }
}
