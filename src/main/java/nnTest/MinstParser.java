package nnTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MinstParser {
  public static Stream<DataPoint> getData() {
    try {
      return Files.lines(Paths.get("src/main/resources/mnist_test.csv"))
        .map(s -> {
          String[] line = s.split(",");
          double[] inputs = new double[784];
          for (int i = 0; i < inputs.length; i++) {
            inputs[i] = Double.parseDouble(line[i+1]) / 256.0;
          }
          double[] outputs = new double[]{0,0,0,0,0,0,0,0,0,0};
          outputs[Integer.parseInt(line[0])] = 1.0;
          return new DataPoint(outputs, inputs);
        });
    } catch (IOException e) {
      e.printStackTrace();
      return Stream.empty();
    }
  }

  public static void printMinstDataPoint(DataPoint dataPoint) {
    for (int i = 0; i < 28; i++) {
      for (int j = 0; j < 28; j++) {
        System.out.print(dataPoint.inputs[i*28+j] > 0.5 ? (dataPoint.inputs[i*28+j] > 0.75 ? "8":"0") : ".");
      }
      System.out.println();
    }
  }
}
