package nnTest.network.impl;

import nnTest.network.api.Delta;
import nnTest.network.api.GradientModifiable;
import nnTest.network.api.Outputable;
import nnTest.network.api.RandomModifiable;

public class Layer implements RandomModifiable<Layer, Layer.LayerDelta>, Outputable, GradientModifiable<Layer, Layer.LayerDelta> {
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
    Gate[] newGates = new Gate[gates.length];
    System.arraycopy(gates, 0, newGates, 0, gates.length);
    for (int i = 0; i < gates.length; i++) {
      newGates[i] = newGates[i].applyDelta(delta.gateDeltas[i]);
    }
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
  public LayerDelta getBestDelta(final double[] input, final double[] expectedOutput) {
    Gate.GateDelta[] gateDeltas = new Gate.GateDelta[gates.length];
    for (int i = 0; i < gates.length; i++) {
      gateDeltas[i] = gates[i].getBestDelta(input, new double[] {expectedOutput[i]});
    }
    return new LayerDelta(gateDeltas);
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
    private Gate.GateDelta[] gateDeltas;

    private LayerDelta(Layer layer) {
      int modifiedGate = (int) Math.floor(Math.random() * (layer.gates.length));
      Gate.GateDelta[] gateDeltas = new Gate.GateDelta[layer.gates.length];

      for (int i = 0; i < layer.gates.length; i++) {
        gateDeltas[i] = new Gate.GateDelta(layer.gates[modifiedGate].getInputSize());
      }
      gateDeltas[modifiedGate] = layer.gates[modifiedGate].createRandomDelta();
      this.gateDeltas = gateDeltas;
    }

    private LayerDelta(Gate.GateDelta[] deltas) {
      this.gateDeltas = deltas;
    }

    @Override
    public LayerDelta scale(final double scalar) {
      Gate.GateDelta[] newDeltas = new Gate.GateDelta[gateDeltas.length];
      System.arraycopy(gateDeltas, 0, newDeltas, 0, gateDeltas.length);
      for (int i = 0; i < gateDeltas.length; i++) {
        newDeltas[i] = newDeltas[i].scale(scalar);
      }
      return new LayerDelta(newDeltas);
    }

    @Override
    public LayerDelta add(final LayerDelta addTo) {
      Gate.GateDelta[] newDeltas = new Gate.GateDelta[gateDeltas.length];
      System.arraycopy(gateDeltas, 0, newDeltas, 0, gateDeltas.length);
      for (int i = 0; i < gateDeltas.length; i++) {
        newDeltas[i] = newDeltas[i].add(addTo.gateDeltas[i]);
      }
      return new LayerDelta(newDeltas);
    }
  }
}
