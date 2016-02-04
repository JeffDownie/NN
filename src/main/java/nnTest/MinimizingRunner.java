package nnTest;

import java.util.stream.Stream;

/**
 * Created by JeffDownie on 02/02/2016.
 */
public class MinimizingRunner {
  public static Network improveNetwork(Network inputNet, DataPoint[] dataPoints, int runs) {
    ProgressBar progressBar = new ProgressBar(runs);
    Network bestNetwork = inputNet;
    double bestOutputCost = getTotalCost(dataPoints, bestNetwork);
    double nextPrintTime = System.currentTimeMillis() - 1;
    progressBar.printUpdatedValue(0);

    for (int i = 0; i < runs; i++) {
      if((System.currentTimeMillis() > nextPrintTime)){
        nextPrintTime = System.currentTimeMillis() + 200;
        progressBar.printUpdatedValue(i);
        System.out.print(" Cost: " + bestOutputCost);
      }

      Network.NetworkDelta delta = bestNetwork.createRandomDelta();
      Network newNetwork = bestNetwork.applyDelta(delta);
      double newOutputCost = getTotalCost(dataPoints, newNetwork);
      if(newOutputCost < bestOutputCost) {
        bestOutputCost = newOutputCost;
        bestNetwork = newNetwork;
      }
    }
    progressBar.complete();
    return bestNetwork;
  }

  public static DataPoint improveDataPoint(Network inputNet, double[] requiredOutput, double[] inputs, int runs) {
    ProgressBar progressBar = new ProgressBar(runs);
    double[] bestInputs = inputs;
    DataPoint bestDataPoint = new DataPoint(requiredOutput, bestInputs);
    double bestOutputCost = getTotalCost(new DataPoint[] {bestDataPoint}, inputNet);
    double nextPrintTime = System.currentTimeMillis() + 200;
    progressBar.printUpdatedValue(0);
    for (int i = 0; i < runs; i++) {
      if((System.currentTimeMillis() > nextPrintTime)){
        nextPrintTime = System.currentTimeMillis() + 200;
        progressBar.printUpdatedValue(i);
        System.out.print(" Cost: " + bestOutputCost);
      }

      double[] newInputs = new double[bestInputs.length];
      System.arraycopy(bestInputs, 0, newInputs, 0, bestInputs.length);
      int toChoose = (int) Math.floor(Math.random() * (bestInputs.length));
      newInputs[toChoose] += (Math.random() - 0.5) * 0.05;

      DataPoint newDataPoint = new DataPoint(requiredOutput, newInputs);
      double newOutputCost = getTotalCost(new DataPoint[] {newDataPoint}, inputNet);
      if(newOutputCost < bestOutputCost) {
        bestOutputCost = newOutputCost;
        bestInputs = newInputs;
        bestDataPoint = newDataPoint;
      }
    }
    progressBar.complete();
    return bestDataPoint;
  }

  public static double getTotalCost(DataPoint[] data, Network network) {
    return Stream.of(data).parallel().mapToDouble(network::getCost).sum();
  }

  public static double getAccuracyClassifierData(DataPoint[] data, Network network) {
    return Stream.of(data).mapToInt(dataPoint -> {
      double[] output = network.getOutput(dataPoint);
      int maxIndex = 0;
      double maxValue = Double.MIN_VALUE;
      for (int i = 0; i < dataPoint.outputSize; i++) {
        if(output[i] > maxValue){
          maxIndex = i;
          maxValue = output[i];
        }
      }
      return dataPoint.outputs[maxIndex] > 0.5 ? 1 : 0;
    }).average().getAsDouble();
  }
}
