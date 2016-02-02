package nnTest;

public class Gate {
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

  Gate randomizedOne() {
    int toChoose = (int) Math.floor(Math.random() * (1 + size));
    double change = (Math.random() - 0.5) * maxDelta;
    if(toChoose == size) {
      return new Gate(size, bias + change, weights);
    } else {
      double[] newWeights = new double[size];
      System.arraycopy(weights, 0, newWeights, 0, size);
      newWeights[toChoose] += change;
      return new Gate(size, bias, newWeights);
    }
  }

  double getOutput(double... inputs) {
    if(inputs.length != this.size) throw new IllegalArgumentException("Input size does");
    double weightedInputSum = this.bias;
    for (int i = 0; i < inputs.length; i++) {
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
}
