package nnTest.network.api;

public interface SingleOutput extends Outputable {
  @Override
  default int getOutputSize(){
    return 1;
  }

  @Override
  default double[] getOutput(double[] input){
    return new double[]{getSingleOutput(input)};
  }

  double getSingleOutput(double[] input);
}
