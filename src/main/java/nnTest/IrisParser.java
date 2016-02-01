package nnTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.DoubleStream;

public class IrisParser {
  public static DataPoint[] getData() {
    try {
      return Files.lines(Paths.get("src/main/resources/iris.csv")).map(s -> {
        String[] line = s.split(",");
        double[] inputs = new double[4];
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = Double.parseDouble(line[i]);
        }
        double[] outputs = "Iris-setosa".equals(line[4]) ? DoubleStream.of(1.0,0.0,0.0).toArray() :
          ("Iris-versicolor".equals(line[4]) ? DoubleStream.of(0.0,1.0,0.0).toArray() : DoubleStream.of(0.0,0.0,1.0).toArray());
        return new DataPoint(outputs, inputs);
      }).toArray(DataPoint[]::new);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
