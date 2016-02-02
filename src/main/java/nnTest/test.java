package nnTest;

import java.util.Arrays;
import java.util.stream.Stream;

public class test {
  public static void main(String[] args) throws Exception{
    Network network = new Network(4, 2, 3);

    DataPoint[] data = IrisParser.getData();

    double priorCurrentOut = getCost(data, network);

    for (int i = 0; i < 50000; i++) {
      Network newNet = network.randomisedOne();
      double newOut = getCost(data, newNet);
      if(newOut < priorCurrentOut) {
        priorCurrentOut = newOut;
        network = newNet;
        System.out.println("----------------------------");
        System.out.println("Total cost: " + newOut);
      }
    }
    System.out.println("Network:");
    System.out.println(network);
    for (DataPoint dataPoint : data) {
      System.out.println("----------------------------");
      System.out.println(dataPoint);
      System.out.println(Arrays.toString(network.getOutput(dataPoint)));
    }
  }

  public static double getCost(DataPoint[] data, Network network) {
    return Stream.of(data).mapToDouble(network::getCost).sum();
  }
}

