package Tanks;

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class AppUtils {
  PApplet parent;

  public AppUtils(PApplet parent) {
    this.parent = parent;
  }

  /**
   * Read the level configuration from the config.json file
   * 
   * @param fileName
   * @return
   */
  public JSONArray readConfigLevelInfo(String fileName) {
    JSONObject config = parent.loadJSONObject(fileName);
    JSONArray levels = config.getJSONArray("levels");
    return levels;
  }

  /**
   * Read the player colours from the config.json file
   * 
   * @param fileName
   * @return
   */
  public JSONObject readPlayerColors(String fileName) {
    JSONObject config = parent.loadJSONObject(fileName);
    JSONObject playerColors = config.getJSONObject("player_colours");
    return playerColors;
  }

  /**
   * Read the level configuration for a specific level
   * 
   * @param fileName
   * @param level
   * @param resource
   * @return
   */
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
