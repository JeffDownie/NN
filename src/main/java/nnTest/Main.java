package nnTest;

import nnTest.network.impl.Layer;
import nnTest.parser.DataPoint;
import nnTest.runners.GradientMinimizingBatchRunner;

public class Main {
  public static void main(String[] args) throws Exception{
    Layer opt = new Layer(2, 2);
    System.out.println(opt);
    DataPoint[] points = new DataPoint[1];
    points[0] = new DataPoint(new double[]{0.5, 0.5}, 0.5, 0.5);

    GradientMinimizingBatchRunner<Layer, Layer.LayerDelta> runner = new GradientMinimizingBatchRunner<>();

    opt = runner.minimizeCost(opt, points, 1000);

    System.out.println(opt);
    System.out.println(opt.getTotalCost(points));
  }
}

