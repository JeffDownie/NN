package nnTest;

public class Main {
  public static void main(String[] args) throws Exception{
    DataPoint[] dataPoints = MinstParser.getData().limit(128).toArray(DataPoint[]::new);

    long time = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      Network optimized =  MinimizingRunner.improveNetwork(
        new Network(dataPoints[0].inputSize, 4, 3, dataPoints[0].outputSize), dataPoints, 10000);
      System.out.println(MinimizingRunner.getTotalCost(dataPoints, optimized));
    }
    System.out.println((System.currentTimeMillis() - time));
  }
}

