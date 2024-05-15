package Tanks;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Moving average class to calculate the moving average of a data stream.
 */
public class MovingAverage {
  private Queue<Float> dataStream;
  private int windowSize;
  private Float sum;

  public MovingAverage(int size) {
    dataStream = new LinkedList<Float>();
    windowSize = size;
    sum = 0.0f;
  }

  /**
   * This function calculates the average of a predetermined window size and moves
   * the window by one.
   * 
   * @param value
   * @return
   */
  public float next(float value) {
    if (dataStream.size() == windowSize) {
      sum = sum - dataStream.remove();
    }

    dataStream.add(value);
    sum = sum + value;

    return sum / dataStream.size();
  }
}
