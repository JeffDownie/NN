package nnTest;

import nnTest.network.impl.Gate;
import nnTest.parser.DataPoint;
import nnTest.parser.IrisParser;
import nnTest.runners.MinimizingRunner;

public class Main {
  public static void main(String[] args) throws Exception{
    DataPoint[] dataPoints = IrisParser.getData().toArray(DataPoint[]::new);
//    MinimizingRunner<Network, Network.NetworkDelta> runner = new MinimizingRunner<>();

    /*long time = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      Network optimized =  runner.minimizeCost(
        new Network(dataPoints[0].inputSize, 3, dataPoints[0].outputSize), dataPoints, 100000);
      System.out.println(optimized.getTotalCost(dataPoints));
    }*/

    Gate opt = new Gate(1);
    System.out.println(opt);
    DataPoint[] points = new DataPoint[]{new DataPoint(0.5, 0.5), new DataPoint(1.0, 1.0), new DataPoint(0.0, 0.0)};
    opt = new MinimizingRunner<Gate, Gate.GateDelta>().minimizeCost(opt, points, 10000);
    System.out.println(opt.getTotalCost(points));
    System.out.println(opt);
//    System.out.println((System.currentTimeMillis() - time));
  }
}

