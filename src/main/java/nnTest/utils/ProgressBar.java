package nnTest.utils;

public class ProgressBar {
  private static final int DEFAULT_ASCII_BAR_SIZE = 50;

  private long maxValue;
  private long valuesPerProgress;
  private boolean complete = false;

  public ProgressBar(long maxValue, int asciiBarSize) {
    if(maxValue < asciiBarSize) {
      asciiBarSize = (int) maxValue;
    }
    this.maxValue = maxValue;
    this.valuesPerProgress = maxValue / asciiBarSize;
  }

  public ProgressBar(long maxValue) {
    this(maxValue, DEFAULT_ASCII_BAR_SIZE);
  }

  public void printUpdatedValue(long value){
    if(value > maxValue) throw new IllegalArgumentException("Value: " + value + " is greater than the maximum value of: " + maxValue);
    if(complete) throw new IllegalStateException("Cannot update completed ProgressBar!");
    StringBuilder sb = new StringBuilder();
    sb.append("\r[");
    for (long j = 0; j < value/valuesPerProgress; j++) {
      sb.append("=");
    }
    sb.append(">");
    for (long j = value/valuesPerProgress; j < maxValue/valuesPerProgress; j++) {
      sb.append(" ");
    }
    sb.append("] ");
    sb.append((int)(value * 100.0 / maxValue + 0.5));
    sb.append("%");
    System.out.print(sb.toString());
  }

  public void complete(){
    if(complete) throw new IllegalStateException("Cannot complete ProgressBar more than once!");
    printUpdatedValue(maxValue);
    complete = true;
    System.out.println();
  }
}
