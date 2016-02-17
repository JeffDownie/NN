package nnTest.runners;

import nnTest.network.api.Delta;
import nnTest.network.api.GradientModifiable;
import nnTest.network.api.Outputable;
import nnTest.parser.DataPoint;
import nnTest.utils.ProgressBar;

public class GradientMinimizingBatchRunner<T extends Outputable & GradientModifiable<T, D>, D extends Delta<T, D>> {
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

      D delta = bestFunction.getEmptyDelta();
      for (int j = 0; j < dataPoints.length; j++) {
        delta = delta.add(bestFunction.getBestDelta(dataPoints[j].inputs, dataPoints[j].outputs));
      }
      delta = delta.scale(1.0 / dataPoints.length);

      bestFunction = bestFunction.applyDelta(delta);
      bestOutputCost = bestFunction.getTotalCost(dataPoints);
    }
    progressBar.complete();
    return bestFunction;
  }
}