package nnTest;

public class Network implements Modifiable<Network, Network.NetworkDelta>{
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
    double[] outputs = new double[layer.length];
    for (int i = 0; i < layer.length; i++) {
      outputs[i] = layer[i].getOutput(inputs);
    }
    return outputs;
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

  @Override
  public Network applyDelta(final NetworkDelta delta) {
    Gate newGate = gates[delta.changedLayer][delta.changedGate].applyDelta(delta.gateDelta);
    Gate[] newGateLayer = new Gate[gates[delta.changedLayer].length];
    System.arraycopy(gates[delta.changedLayer], 0, newGateLayer, 0, newGateLayer.length);
    newGateLayer[delta.changedGate] = newGate;
    Gate[][] newGates = new Gate[gates.length][];
    System.arraycopy(gates, 0, newGates, 0, gates.length);
    newGates[delta.changedLayer] = newGateLayer;
    return new Network(newGates);
  }

  @Override
  public NetworkDelta createRandomDelta() {
    return new NetworkDelta(this);
  }

  public static class NetworkDelta implements Delta<Network, NetworkDelta>{
    private int changedLayer;
    private int changedGate;
    private Gate.GateDelta gateDelta;

    private NetworkDelta(Network network){
      changedLayer = (int) Math.floor(Math.random() * (network.gates.length));
      changedGate = (int) Math.floor(Math.random() * (network.gates[changedLayer].length));
      gateDelta = network.gates[changedLayer][changedGate].createRandomDelta();
    }
  }
}
