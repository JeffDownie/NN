package nnTest;

import nnTest.network.impl.Gate;
import nnTest.parser.DataPoint;
import nnTest.parser.IrisParser;
import nnTest.runners.RandomMinimizingRunner;

import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) throws Exception{
    DataPoint[] dataPoints = IrisParser.getData().toArray(DataPoint[]::new);
//    RandomMinimizingRunner<Network, Network.NetworkDelta> runner = new RandomMinimizingRunner<>();

    /*long time = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      Network optimized =  runner.minimizeCost(
        new Network(dataPoints[0].inputSize, 3, dataPoints[0].outputSize), dataPoints, 100000);
      System.out.println(optimized.getTotalCost(dataPoints));
    }*/

    Gate opt = new Gate(1);
    System.out.println(opt);
    DataPoint[] points = new DataPoint[10];
    for (int i = 0; i < 10; i++) {
      points[i] = new DataPoint(i / 10.0, i / 10.0);
    }
    opt = new RandomMinimizingRunner<Gate, Gate.GateDelta>().minimizeCost(opt, points, 100000);
    Stream.of(points).forEach(System.out::println);
    System.out.println(opt.getTotalCost(points));
    System.out.println(opt);
//    System.out.println((System.currentTimeMillis() - time));
  }
}

