package nnTest;

import java.util.stream.Stream;

public class test {
  public static void main(String[] args) throws Exception{
    Network network = new Network(4, 2, 2, 3);

    DataPoint[] data = IrisParser.getData();

    double priorCurrentOut = getCost(data, network);

    for (int i = 0; i < 100000; i++) {
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
  }

  public static double getCost(DataPoint[] data, Network network) {
    return Stream.of(data).mapToDouble(network::getCost).sum();
  }
}

