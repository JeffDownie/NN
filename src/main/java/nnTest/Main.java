package nnTest;

import nnTest.network.impl.Network;
import nnTest.parser.DataPoint;
import nnTest.parser.IrisParser;
import nnTest.runners.GradientMinimizingBatchRunner;

public class Main {
  public static void main(String[] args) throws Exception{
    DataPoint[] points = IrisParser.getData().toArray(DataPoint[]::new);
    Network opt = new Network(4, 1, 3);

    GradientMinimizingBatchRunner<Network, Network.NetworkDelta> runner = new GradientMinimizingBatchRunner<>();

    opt = runner.minimizeCost(opt, points, 10000);

    System.out.println(opt);
    System.out.println(opt.getTotalCost(points));
  }
}

