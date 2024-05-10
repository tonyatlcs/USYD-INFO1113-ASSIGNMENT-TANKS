package Tanks;

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class AppUtils {
  PApplet parent;

  public AppUtils(PApplet parent) {
    this.parent = parent;
  }

  public JSONArray readConfigLevelInfo(String fileName) {
    JSONObject config = parent.loadJSONObject(fileName);
    JSONArray levels = config.getJSONArray("levels");
    return levels;
  }

  public JSONObject readPlayerColors(String fileName) {
    JSONObject config = parent.loadJSONObject(fileName);
    JSONObject playerColors = config.getJSONObject("player_colours");
    return playerColors;
  }

  public String readLevelConfig(String fileName, int level, String resource) {
    JSONArray config = readConfigLevelInfo(fileName);

    for (int i = 0; i < config.size() - 1; i++) {
      JSONObject levelsConfig = config.getJSONObject(i);
      if (i + 1 == level) {
        return levelsConfig.getString(resource);
      }
    }
    return " ";
  }

}
