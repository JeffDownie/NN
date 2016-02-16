package nnTest;

import nnTest.network.impl.Gate;
import nnTest.parser.DataPoint;
import nnTest.parser.IrisParser;
import nnTest.runners.RandomMinimizingRunner;

import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) throws Exception{
    Gate opt = new Gate(1);
    System.out.println(opt);
    DataPoint[] points = new DataPoint[3];
    points[0] = new DataPoint(0.5, 0.5);
    points[1] = new DataPoint(0.7, 0.7);
    points[2] = new DataPoint(0.9, 0.9);
    System.out.println(opt.getTotalCost(points));
    for (int i = 0; i < 200000; i++) {
      Gate.GateDelta delta = opt.getBestDelta(points[0].inputs, points[0].outputs);
      opt = opt.applyDelta(delta);
      delta = opt.getBestDelta(points[1].inputs, points[1].outputs);
      opt = opt.applyDelta(delta);
      delta = opt.getBestDelta(points[2].inputs, points[2].outputs);
      opt = opt.applyDelta(delta);
    }
    System.out.println(opt);
    System.out.println(opt.getTotalCost(points));
    System.out.println(opt.getSingleOutput(new double[] {0.5}));
    System.out.println(opt.getSingleOutput(new double[] {0.7}));
    System.out.println(opt.getSingleOutput(new double[] {0.9}));
  }
}

