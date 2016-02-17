package nnTest.network.impl;

import nnTest.network.api.Delta;
import nnTest.network.api.Outputable;
import nnTest.network.api.RandomModifiable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Layer implements RandomModifiable<Layer, Layer.LayerDelta>, Outputable {
  private Gate[] gates;
  private int inputSize;

  public Layer(int inputSize, int layerSize) {
    if(layerSize < 1) throw new IllegalArgumentException("Layer size must be at least one.");
    this.gates = new Gate[layerSize];
    for (int i = 0; i < layerSize; i++) {
      this.gates[i] = new Gate(inputSize);
    }
    this.inputSize = inputSize;
  }

  private Layer(int inputSize, Gate[] gates) {
    this.inputSize = inputSize;
    this.gates = gates;
  }

  @Override
  public double[] getOutput(double[] input) {
    if(input.length != this.inputSize) throw new IllegalArgumentException("Input size is not the same as the layer's input size.");
    double[] outputs = new double[gates.length];
    for (int i = 0; i < gates.length; i++) {
      outputs[i] = gates[i].getSingleOutput(input);
    }
    return outputs;
  }

  @Override
  public Layer applyDelta(final LayerDelta delta) {
    Gate newGate = gates[delta.modifiedGate].applyDelta(delta.gateDelta);
    Gate[] newGates = new Gate[gates.length];
    System.arraycopy(gates, 0, newGates, 0, gates.length);
    newGates[delta.modifiedGate] = newGate;
    return new Layer(inputSize, newGates);
  }

  @Override
  public LayerDelta createRandomDelta() {
    return new LayerDelta(this);
  }

  @Override
  public int getInputSize() {
    return this.inputSize;
  }

  @Override
  public int getOutputSize() {
    return this.gates.length;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(Gate gate : gates) {
      sb.append(gate.toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  public static class LayerDelta implements Delta<Layer, LayerDelta> {
    private int modifiedGate;
    private Gate.GateDelta gateDelta;

    private LayerDelta(Layer layer) {
      modifiedGate = (int) Math.floor(Math.random() * (layer.gates.length));
      gateDelta = layer.gates[modifiedGate].createRandomDelta();
    }

    @Override
    public LayerDelta scale(final double scalar) {
      throw new NotImplementedException();
    }

    @Override
    public LayerDelta add(final LayerDelta addTo) {
      throw new NotImplementedException();
    }
  }
}
