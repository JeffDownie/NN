package nnTest;

public class Gate implements Modifiable<Gate, Gate.GateDelta>, SingleOutput{
  static final double maxDelta = 0.05;
  int inputSize;
  double[] weights;
  double bias;

  Gate(int inputSize) {
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
    return 1.0 / (1.0 + Math.exp(-weightedInputSum));
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
    if(delta.modifiedWeight == inputSize) {
      return new Gate(inputSize, bias + delta.delta, weights);
    }
    double[] newWeights = new double[inputSize];
    System.arraycopy(weights, 0, newWeights, 0, inputSize);
    newWeights[delta.modifiedWeight] += delta.delta;
    return new Gate(inputSize, bias, newWeights);
  }

  @Override
  public GateDelta createRandomDelta() {
    return new GateDelta(this);
  }

  @Override
  public int getInputSize() {
    return this.inputSize;
  }

  public static class GateDelta implements Delta<Gate, GateDelta>{
    private int modifiedWeight;
    private double delta;

    private GateDelta(Gate gate) {
      modifiedWeight = (int) Math.floor(Math.random() * (1 + gate.inputSize));
      delta = (Math.random() - 0.5) * maxDelta;
    }
  }
}
