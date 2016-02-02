package nnTest;

import java.util.stream.Stream;

public class Network {
  Gate[][] gates;

  private Network(Gate[][] gates){
    this.gates = gates;
  }

  Network(int... layerSizes) {
    if(layerSizes.length < 2) throw new IllegalArgumentException("Number of layers too small.");
    int numberOfLayers = layerSizes.length - 1;
    gates = new Gate[numberOfLayers][];
    for (int layerNumber = 0; layerNumber < numberOfLayers; layerNumber++) {
      int layerInputSize = layerSizes[layerNumber];
      int layerOutputSize = layerSizes[layerNumber + 1];
      gates[layerNumber] = new Gate[layerOutputSize];
      for (int gateNumber = 0; gateNumber < layerOutputSize; gateNumber++) {
        gates[layerNumber][gateNumber] = new Gate(layerInputSize);
      }
    }
  }

  Network randomisedOne() {
    int toChooseLayer = (int) Math.floor(Math.random() * (gates.length));
    int toChooseSpecific = (int) Math.floor(Math.random() * (gates[toChooseLayer].length));
    Gate newGate = gates[toChooseLayer][toChooseSpecific].randomizedOne();
    Gate[] newGateLayer = new Gate[gates[toChooseLayer].length];
    System.arraycopy(gates[toChooseLayer], 0, newGateLayer, 0, newGateLayer.length);
    newGateLayer[toChooseSpecific] = newGate;
    Gate[][] newGates = new Gate[gates.length][];
    System.arraycopy(gates, 0, newGates, 0, gates.length);
    newGates[toChooseLayer] = newGateLayer;
    return new Network(newGates);
  }

  public double getCost(DataPoint dataPoint) {
    double[] output = getOutput(dataPoint);
    double total = 0.0;
    for (int i = 0; i < dataPoint.outputSize; i++) {
      total += Math.pow(dataPoint.outputs[i] - output[i], 2);
    }
    return total;
  }

  public double[] getOutput(DataPoint dataPoint) {
    double[] priorOutput = dataPoint.inputs;
    for (Gate[] gateLayer : gates){
      priorOutput = getLayerOutput(gateLayer, priorOutput);
    }
    return priorOutput;
  }

  private static double[] getLayerOutput(Gate[] layer, double[] inputs){
    return Stream.of(layer).mapToDouble(gate -> gate.getOutput(inputs)).toArray();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < gates.length; i++) {
      sb.append("Layer " + i + "\n");
      for (int j = 0; j < gates[i].length; j++) {
        sb.append(gates[i][j].toString());
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
