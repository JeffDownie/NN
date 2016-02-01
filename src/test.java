import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class test {
  private static final int numGates = 5;
  static List<Gate> gates;

  public static void main(String[] args) {
    gates = Stream.generate(() -> new Gate(2)).limit(numGates).collect(Collectors.toList());
    List<DataPoint> data = Stream.of(
      new DataPoint(1.0,1.0,0.0),
      new DataPoint(0.0,1.0,1.0),
      new DataPoint(1.0,0.0,1.0),
      new DataPoint(0.0,0.0,0.0)
    ).collect(Collectors.toList());

    double priorCurrentOut = Double.MAX_VALUE;

    for (int i = 0; i < 100000; i++) {
      int toChoose = (int) (Math.floor(Math.random() * numGates));
      Gate oldGate = gates.get(toChoose);
      gates.set(toChoose, gates.get(toChoose).randomizedOne());
      double newOut = getCost(data);
      if(newOut >= priorCurrentOut) {
        gates.set(toChoose, oldGate);
      } else {
        priorCurrentOut = newOut;
        System.out.println("----------------------------");
        System.out.println("forRound: " + newOut);
        data.forEach(dataPoint -> {
          System.out.println("actual: " + getOutput(dataPoint));
          System.out.println("expected: " + dataPoint.out);
        });
      }
    }
    gates.forEach(System.out::println);
  }

  public static double getCost(List<DataPoint> data){
    return data.stream()
      .mapToDouble(dataPoint -> Math.pow(getOutput(dataPoint) - dataPoint.out, 2))
      .sum();
  }

  public static double getOutput(DataPoint dataPoint) {
    double outg1 = gates.get(0).getOutput(dataPoint.in1, dataPoint.in2);
    double outg2 = gates.get(1).getOutput(dataPoint.in1, dataPoint.in2);
    double outg3 = gates.get(2).getOutput(outg1, outg2);
    double outg4 = gates.get(3).getOutput(outg1, outg2);
    double outg5 = gates.get(4).getOutput(outg3, outg4);
    return outg5;
  }
}
class DataPoint {
  double in1, in2, out;
  DataPoint(double in1, double in2, double out) {
    this.in1 = in1;
    this.in2 = in2;
    this.out = out;
  }
}
class Gate {
  static final double maxDelta = 0.05;
  int size;
  double[] weights;
  double bias;

  Gate(int size) {
    this.size = size;
    bias = Math.random() - 0.5;
    weights = new double[size];
    for (int i = 0; i < size; i++) {
      weights[i] = Math.random() - 0.5;
    }
  }

  Gate(int size, double bias, double... weights) {
    if(weights.length != size) throw new IllegalArgumentException();
    this.size = size;
    this.bias = bias;
    this.weights = weights;
  }

  Gate randomizedOne() {
    int toChoose = (int) Math.floor(Math.random() * (1 + size));
    double change = (Math.random() - 0.5) * maxDelta;
    if(toChoose == size) {
      return new Gate(size, bias + change, weights);
    } else {
      double[] newWeights = new double[size];
      System.arraycopy(weights, 0, newWeights, 0, size);
      newWeights[toChoose] += change;
      return new Gate(size, bias, newWeights);
    }
  }

  double getOutput(double... inputs) {
    if(inputs.length != this.size) throw new IllegalArgumentException();
    double weightedInputSum = this.bias;
    for (int i = 0; i < inputs.length; i++) {
      weightedInputSum += inputs[i]*weights[i];
    }
    return 1.0 / (1.0 + Math.exp(-weightedInputSum));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.size; i++) {
      sb.append(weights[i]);
      sb.append(", ");
    }
    return sb.toString() + bias;
  }
}