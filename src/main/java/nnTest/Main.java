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
    DataPoint[] points = new DataPoint[1];
    points[0] = new DataPoint(0.5, 0.5);
    System.out.println(opt.getTotalCost(points));
    Gate.GateDelta delta = opt.getBestDelta(points[0].inputs, points[0].outputs);
    opt = opt.applyDelta(delta);
    System.out.println(opt);
    System.out.println(opt.getTotalCost(points));
  }
}

