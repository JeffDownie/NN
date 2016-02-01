package nnTest;

import java.util.stream.Stream;

public class Network {
  Gate[][] gates;

  private Network(Gate[][] gates){
    this.gates = gates;
  }

  Network(int... layerSizes) {
    gates = new Gate[layerSizes.length - 1][];
    for (int i = 1; i < layerSizes.length; i++) {
      gates[i - 1] = new Gate[layerSizes[i]];
      for (int j = 0; j < layerSizes[i]; j++) {
        gates[i - 1][j] = new Gate(layerSizes[i - 1]);
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
    for (int i = 0; i < dataPoint.outputs.length; i++) {
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
