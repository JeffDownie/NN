package nnTest;

import nnTest.network.impl.Layer;
import nnTest.parser.DataPoint;

public class Main {
  public static void main(String[] args) throws Exception{
    Layer opt = new Layer(1, 1);
    System.out.println(opt);
    DataPoint[] points = new DataPoint[1];
    points[0] = new DataPoint(new double[]{0.5}, new double[]{0.5});
    System.out.println(opt.getTotalCost(points));
    for (int i = 0; i < 20; i++) {
      Layer.LayerDelta delta = opt.getBestDelta(points[0].inputs, points[0].outputs);
      opt = opt.applyDelta(delta);
    }
    System.out.println(opt);
    System.out.println(opt.getTotalCost(points));
  }
}

