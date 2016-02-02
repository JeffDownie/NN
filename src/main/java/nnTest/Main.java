package nnTest;

public class Main {
  public static void main(String[] args) throws Exception{
    DataPoint[] dataPoints = MinstParser.getData().limit(50).toArray(DataPoint[]::new);

    /*MinstParser.printMinstDataPoint(dataPoints[0]);*/
    for (int i = 0; i < 1; i++) {
      Network optimized =  MinimizingRunner.improveNetwork(
            new Network(dataPoints[0].inputSize, 4, 3, dataPoints[0].outputSize), dataPoints, 50000);
      System.out.println(MinimizingRunner.getAccuracyClassifierData(dataPoints, optimized));
      MinstParser.printMinstDataPoint(MinimizingRunner.improveDataPoint(optimized, new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, dataPoints[0].inputs, 10000000));
    }
  }
}

