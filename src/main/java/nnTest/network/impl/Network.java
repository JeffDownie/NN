package nnTest.network.impl;

import nnTest.network.api.Delta;
import nnTest.network.api.Outputable;
import nnTest.network.api.RandomModifiable;

public class Network implements RandomModifiable<Network, Network.NetworkDelta>, Outputable {
  Layer[] layers;
  int inputSize;
  int outputSize;

  private Network(int inputSize, int outputSize, Layer[] layers){
    this.layers = layers;
    this.inputSize = inputSize;
    this.outputSize = outputSize;
  }

  Network(int... layerSizes) {
    if(layerSizes.length < 2) throw new IllegalArgumentException("Number of layers too small.");
    this.inputSize = layerSizes[0];
    this.outputSize = layerSizes[layerSizes.length - 1];
    int numberOfLayers = layerSizes.length - 1;
    layers = new Layer[numberOfLayers];
    for (int layerNumber = 0; layerNumber < numberOfLayers; layerNumber++) {
      int layerInputSize = layerSizes[layerNumber];
      int layerOutputSize = layerSizes[layerNumber + 1];
      layers[layerNumber] = new Layer(layerInputSize, layerOutputSize);
    }
  }

  @Override
  public double[] getOutput(double[] inputs) {
    double[] priorOutput = inputs;
    for (Layer layer : layers){
      priorOutput = layer.getOutput(priorOutput);
    }
    return priorOutput;
  }

  @Override
  public Network applyDelta(final NetworkDelta delta) {
    Layer newLayer = layers[delta.changedLayer].applyDelta(delta.layerDelta);
    Layer[] newLayers = new Layer[layers.length];
    System.arraycopy(layers, 0, newLayers, 0, layers.length);
    newLayers[delta.changedLayer] = newLayer;
    return new Network(inputSize, outputSize, newLayers);
  }

  @Override
  public NetworkDelta createRandomDelta() {
    return new NetworkDelta(this);
  }

  @Override
  public int getInputSize() {
    return inputSize;
  }

  @Override
  public int getOutputSize() {
    return outputSize;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < layers.length; i++) {
      sb.append("Layer ").append(i).append("\n").append(layers[i].toString()).append("\n");
    }
    return sb.toString();
  }

  public static class NetworkDelta implements Delta<Network, NetworkDelta> {
    private int changedLayer;
    private Layer.LayerDelta layerDelta;

    private NetworkDelta(Network network){
      changedLayer = (int) Math.floor(Math.random() * (network.layers.length));
      layerDelta = network.layers[changedLayer].createRandomDelta();
    }
  }
}
