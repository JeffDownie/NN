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
    double nextPrintTime = System.currentTimeMillis() + 200;
    progressBar.printUpdatedValue(0);
    for (int i = 0; i < runs; i++) {
      if((i % 100 == 0) && (System.currentTimeMillis() > nextPrintTime)){
        nextPrintTime = System.currentTimeMillis() + 200;
        progressBar.printUpdatedValue(i);
      }
      Network newNetwork = bestNetwork.randomisedOne();
      double newOutputCost = getTotalCost(dataPoints, newNetwork);
      if(newOutputCost < bestOutputCost) {
        bestOutputCost = newOutputCost;
        bestNetwork = newNetwork;
      }
    }
    progressBar.complete();
    return bestNetwork;
  }

  public static double getTotalCost(DataPoint[] data, Network network) {
    return Stream.of(data).mapToDouble(network::getCost).sum();
  }
}
