package nnTest.network.impl;

import nnTest.network.api.Delta;
import nnTest.network.api.GradientModifiable;
import nnTest.network.api.Outputable;
import nnTest.network.api.RandomModifiable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.stream.Stream;

public class Network implements RandomModifiable<Network, Network.NetworkDelta>, Outputable, GradientModifiable<Network, Network.NetworkDelta> {
  Layer[] layers;
  int inputSize;
  int outputSize;

  private Network(int inputSize, int outputSize, Layer[] layers){
    this.layers = layers;
    this.inputSize = inputSize;
    this.outputSize = outputSize;
  }

  public Network(int... layerSizes) {
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

  private double[][] getAllLayerOutputs(double[] inputs) {
    double[][] layerOutputs = new double[layers.length + 1][];
    layerOutputs[0] = inputs;
    for (int i = 0; i < layers.length; i++) {
      layerOutputs[i+1] = layers[i].getOutput(layerOutputs[i]);
    }
    return layerOutputs;
  }

  @Override
  public Network applyDelta(final NetworkDelta delta) {
    Layer[] newLayers = new Layer[layers.length];
    System.arraycopy(layers, 0, newLayers, 0, layers.length);
    for (int i = 0; i < layers.length; i++) {
      newLayers[i] = newLayers[i].applyDelta(delta.layerDeltas[i]);
    }
    return new Network(inputSize, outputSize, newLayers);
  }

  @Override
  public NetworkDelta getEmptyDelta() {
    return new NetworkDelta(layers);
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
  public NetworkDelta getBestDelta(final double[] input, final double[] expectedOutput) {
    double[][] layerOutputs = getAllLayerOutputs(input);
    Layer.LayerDelta[] layerDeltas = new Layer.LayerDelta[layers.length];
    layerOutputs[layers.length] = expectedOutput;
    for (int i = layers.length - 1; i >= 0; i--) {
      layerDeltas[i] = layers[i].getBestDelta(layerOutputs[i], layerOutputs[i+1]);
      layerOutputs[i] = new double[layerOutputs[i].length];
      for (int j = 0; j < layerDeltas[i].gateDeltas.length; j++) {
        for (int k = 0; k < layerDeltas[i].gateDeltas[j].deltas.length; k++) {
          layerOutputs[i][k] += layerDeltas[i].gateDeltas[j].deltas[k];
        }
      }
    }
    return new NetworkDelta(layerDeltas);
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
    private Layer.LayerDelta[] layerDeltas;

    private NetworkDelta(Network network){
      int changedLayer = (int) Math.floor(Math.random() * (network.layers.length));
      layerDeltas = new Layer.LayerDelta[network.layers.length];
      for (int i = 0; i < network.layers.length; i++) {
        layerDeltas[i] = network.layers[i].getEmptyDelta();
      }
      Layer.LayerDelta layerDelta = network.layers[changedLayer].createRandomDelta();
      layerDeltas[changedLayer] = layerDelta;
    }

    private NetworkDelta(final Layer[] layers) {
      layerDeltas = new Layer.LayerDelta[layers.length];
      for (int i = 0; i < layers.length; i++) {
        layerDeltas[i] = layers[i].getEmptyDelta();
      }
    }

    private NetworkDelta(final Layer.LayerDelta[] layerDeltas) {
      this.layerDeltas = layerDeltas;
    }

    @Override
    public NetworkDelta scale(final double scalar) {
      Layer.LayerDelta[] newDeltas = new Layer.LayerDelta[layerDeltas.length];
      System.arraycopy(layerDeltas, 0, newDeltas, 0, layerDeltas.length);
      for (int i = 0; i < layerDeltas.length; i++) {
        newDeltas[i] = newDeltas[i].scale(scalar);
      }
      return new NetworkDelta(newDeltas);
    }

    @Override
    public NetworkDelta add(final NetworkDelta addTo) {
      Layer.LayerDelta[] newDeltas = new Layer.LayerDelta[layerDeltas.length];
      System.arraycopy(layerDeltas, 0, newDeltas, 0, layerDeltas.length);
      for (int i = 0; i < layerDeltas.length; i++) {
        newDeltas[i] = newDeltas[i].add(addTo.layerDeltas[i]);
      }
      return new NetworkDelta(newDeltas);
    }
  }
}
