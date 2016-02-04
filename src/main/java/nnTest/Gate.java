package nnTest;

public class Gate implements Modifiable<Gate, Gate.GateDelta>{
  static final double maxDelta = 0.05;
  int size;
  double[] weights;
  double bias;

  Gate(int size) {
    this.size = size;
    bias = Math.random() - 0.5;
    weights = new double[size];
    for (int i = 0; i < size; i++) {
      weights[i] = Math.random() - 0.5;
    }
  }

  Gate(int size, double bias, double... weights) {
    if(weights.length != size) throw new IllegalArgumentException();
    this.size = size;
    this.bias = bias;
    this.weights = weights;
  }

  double getOutput(double... inputs) {
    if(inputs.length != this.size) throw new IllegalArgumentException("Input size does not agree with gate size.");
    double weightedInputSum = this.bias;
    for (int i = 0; i < this.size; i++) {
      weightedInputSum += inputs[i]*weights[i];
    }
    return 1.0 / (1.0 + Math.exp(-weightedInputSum));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.size; i++) {
      sb.append(weights[i]);
      sb.append(", ");
    }
    return sb.toString() + "bias: " + bias;
  }

  @Override
  public Gate applyDelta(final GateDelta delta) {
    if(delta.modifiedWeight == size) {
      return new Gate(size, bias + delta.delta, weights);
    }
    double[] newWeights = new double[size];
    System.arraycopy(weights, 0, newWeights, 0, size);
    newWeights[delta.modifiedWeight] += delta.delta;
    return new Gate(size, bias, newWeights);
  }

  @Override
  public GateDelta createRandomDelta() {
    return new GateDelta(this);
  }

  public static class GateDelta implements Delta<Gate, GateDelta>{
    private int modifiedWeight;
    private double delta;

    private GateDelta(Gate gate) {
      modifiedWeight = (int) Math.floor(Math.random() * (1 + gate.size));
      delta = (Math.random() - 0.5) * maxDelta;
    }
  }
}
