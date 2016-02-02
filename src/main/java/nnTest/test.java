package nnTest;

public class test {
  public static void main(String[] args) throws Exception{
    Network network = new Network(4, 3, 3, 3);

    DataPoint[] data = IrisParser.getData().toArray(DataPoint[]::new);

    network = MinimizingRunner.improveNetwork(network, data, 10000);

    System.out.println("----------------------------");
    System.out.println("Network:");
    System.out.println(network);
    System.out.println("Total cost: " + MinimizingRunner.getTotalCost(data, network));
  }
}

