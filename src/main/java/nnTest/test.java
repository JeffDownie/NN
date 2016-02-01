package nnTest;

import java.util.Arrays;
import java.util.stream.Stream;

public class test {
  public static void main(String[] args) {
    Network network = new Network(2, 2, 1);

    DataPoint[] data = Stream.of(
      new DataPoint(1.0, 1.0, 1.0),
      new DataPoint(0.5, 1.0, 0.0),
      new DataPoint(0.9, 0.0, 1.0),
      new DataPoint(0.0, 0.0, 0.0)
    ).toArray(DataPoint[]::new);

    double priorCurrentOut = getCost(data, network);

    for (int i = 0; i < 100000; i++) {
      Network newNet = network.randomisedOne();
      double newOut = getCost(data, newNet);
      if(newOut < priorCurrentOut) {
        priorCurrentOut = newOut;
        network = newNet;
        System.out.println("----------------------------");
        System.out.println("Total cost: " + newOut);
        for (DataPoint datapoint : data) {
          System.out.println(datapoint);
          System.out.println("Real output: " + Arrays.toString(network.getOutput(datapoint)));
        }
      }
    }
    System.out.println("Network:");
    System.out.println(network);
  }

  public static double getCost(DataPoint[] data, Network network) {
    return Stream.of(data).mapToDouble(network::getCost).sum();
  }
}

