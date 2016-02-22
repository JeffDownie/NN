package nnTest.runners;

import nnTest.network.api.Delta;
import nnTest.network.api.Outputable;
import nnTest.network.api.RandomModifiable;
import nnTest.parser.DataPoint;
import nnTest.utils.ProgressBar;

public class RandomMinimizingRunner<T extends Outputable & RandomModifiable<T, D>, D extends Delta<T, D>> {
  public T minimizeCost(T initialFunction, DataPoint[] dataPoints, int runs){
    ProgressBar progressBar = new ProgressBar(runs);
    T bestFunction = initialFunction;
    double bestOutputCost = bestFunction.getTotalCost(dataPoints);
    double nextPrintTime = System.currentTimeMillis() - 1;
    progressBar.printUpdatedValue(0);

    for (int i = 0; i < runs; i++) {
      if((System.currentTimeMillis() > nextPrintTime)){
        nextPrintTime = System.currentTimeMillis() + 200;
        progressBar.printUpdatedValue(i);
        System.out.print(" Cost: " + bestOutputCost);
      }

      T newObject = bestFunction.applyDelta(bestFunction.createRandomDelta());
      double newOutputCost = newObject.getTotalCost(dataPoints);
      if(newOutputCost < bestOutputCost) {
        bestOutputCost = newOutputCost;
        bestFunction = newObject;
      }
    }
    progressBar.complete();
    return bestFunction;
  }
}
