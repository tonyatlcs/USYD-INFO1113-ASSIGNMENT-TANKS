package Tanks;

import processing.core.PApplet;

public class App extends PApplet {

  public static void main(String[] args) {
    GameBackground background = new GameBackground();
    PApplet.runSketch(new String[] { "Tanks" }, background);
  }
}
