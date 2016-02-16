package nnTest.network.api;

public interface GradientModifiable<M extends GradientModifiable<M, D>, D extends Delta<M, D>> extends Modifiable<M, D>, Outputable{
  D getBestDelta(double[] input, double[] expectedOutput);
}
