package Tanks;

import processing.core.*;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GameTerrain {

  PApplet parent;
  private HashMap<String, GamePlayer> gamePlayers = new HashMap<String, GamePlayer>();
  private GameBackground levelBackground;
  private float windAcceleration;
  private float windVelocityX;

  public GameTerrain(PApplet parent, GameBackground levelBackground, float windAcceleration, float windVelocityX) {
    this.parent = parent;
    this.levelBackground = levelBackground;
    this.windAcceleration = windAcceleration;
    this.windVelocityX = windVelocityX;
  }

  /**
   * Getters
   * 
   */
  public HashMap<String, GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  public float getWindAcceleration() {
    return windAcceleration;
  }

  public float getWindVelocityX() {
    return windVelocityX;
  }

  /**
   * This function saves all the required information that are required for a game
   * player
   * 
   * @param terrainHeights
   * @param terrainTanks
   * @param playerColors
   */
  public void setGamePlayers(ArrayList<Float> terrainHeights, ArrayList<Integer> terrainTanks,
      JSONObject playerColors) {
    ArrayList<String> tankNames = loadPlayerTanksInfo("level1Tanks.txt");
    ArrayList<Float> curvedTerrainheights = levelBackground.curvedLevelTerrain;
    int count = 0;

    for (int i = 0; i < terrainTanks.size(); i++) {

      if (terrainTanks.get(i) == 1) {
        // Give the necessary player information
        GameTank playerTank = new GameTank(parent,
            curvedTerrainheights, (i) * 32, windAcceleration, windVelocityX,
            parent.height - terrainHeights.get(i * 32));
        String playerName = "Player " + tankNames.get(count);
        String playerTextColor = playerColors.getString(tankNames.get(count));
        String playerColor = playerColors.getString(tankNames.get(count));
        GamePlayer player = new GamePlayer(playerName, playerTextColor, playerColor, playerTank, windAcceleration,
            windVelocityX);

        gamePlayers.put(tankNames.get(count), player);

        count = count + 1;
      }
    }
  }

  /**
   * This loads the terrain configuration from a txt file
   * 
   * @param fileName
   * @return
   */
  public ArrayList<Integer> loadTerrainObjects(String fileName) {
    String[] level1TerrainObject = parent.loadStrings(fileName);
    ArrayList<Integer> terrainHeights = new ArrayList<Integer>();
    for (int i = 0; i < level1TerrainObject.length; i++) {
      // split string with delimiter ","
      String[] lineArr = level1TerrainObject[i].split(",");
      terrainHeights.add(Integer.parseInt(lineArr[1]));
    }
    return terrainHeights;
  }

  /**
   * This loads the player tank configuration from a txt file
   * 
   * @param fileName
   * @return
   */
  public ArrayList<String> loadPlayerTanksInfo(String fileName) {
    String[] level1TerrainObject = parent.loadStrings(fileName);
    ArrayList<String> terrainTanks = new ArrayList<String>();
    for (int i = 0; i < level1TerrainObject.length; i++) {
      String[] lineArr = level1TerrainObject[i].split(",");
      // Only add if the third element is not empty
      if (!lineArr[2].equals("null")) {
        terrainTanks.add(lineArr[2]);
      }
    }

    return terrainTanks;
  }

  /**
   * This function calculates the number of players in the game
   * 
   * @param terrainTanks
   * @return
   */
  public int calculateGamePlayerNumbers(ArrayList<Integer> terrainTanks) {
    int count = 0;
    for (int i = 0; i < terrainTanks.size(); i++) {
      if (terrainTanks.get(i) == 1) {
        count = count + 1;
      }
    }
    return count;
  }

  /**
   * This function draws the terrain onto the game window
   * 
   * @param terrainPixelHeights
   */
  public void drawTerrain(ArrayList<Float> terrainPixelHeights) {
    // draw a rectangle for each pixel in the terrain
    for (int i = 0; i < terrainPixelHeights.size(); i++) {
      parent.stroke(255);
      parent.rect(i, parent.height - terrainPixelHeights.get(i), 1, terrainPixelHeights.get(i));
    }
  }

  /**
   * This function draws the trees onto the game window
   * 
   * @param terrainHeights
   * @param terrainTrees
   * @param file
   */
  public void drawTrees(ArrayList<Float> terrainHeights, ArrayList<Integer> terrainTrees, String file) {
    PImage tree = parent.loadImage(file);
    tree.resize(30, 30);

    for (int i = 0; i < terrainTrees.size(); i++) {
      if (terrainTrees.get(i) == 1) {
        parent.image(tree, ((i) * 32) - 16, parent.height - terrainHeights.get(i * 32) - 30);
      }
    }
  }

  /**
   * This function draws the tanks onto the game window
   * 
   * @param terrainHeights
   * @param terrainTanks
   * @param playerColors
   */
  public void drawTanks(ArrayList<Float> terrainHeights, ArrayList<Integer> terrainTanks, JSONObject playerColors) {
    ArrayList<String> tankNames = loadPlayerTanksInfo("level1Tanks.txt");

    int count = 0;

    for (int i = 0; i < terrainTanks.size(); i++) {

      if (terrainTanks.get(i) == 1) {
        GameTank playerTank = gamePlayers.get(tankNames.get(count)).getPlayerTank();
        String playerColor = gamePlayers.get(tankNames.get(count)).getPlayerTankColor();
        count = count + 1;
        playerTank.drawTank(playerColor);

      }
    }
  }

  /**
   * This function calculates the moving average of the terrain heights
   * 
   * @param terrainHeights
   * @return
   */
  public ArrayList<Float> calculateMovingAverageInt(ArrayList<Integer> terrainHeights) {
    ArrayList<Float> TerrainPixelHeight = new ArrayList<Float>();
    ArrayList<Float> smoothedTerrainPixelHeight = new ArrayList<Float>();
    MovingAverage movingAverage = new MovingAverage(32);

    for (int i = 0; i < terrainHeights.size(); i++) {
      for (int j = 0; j < 32; j++) {
        TerrainPixelHeight.add((float) terrainHeights.get(i) * 32);
      }
    }

    for (int i = 0; i < TerrainPixelHeight.size(); i++) {
      float result = movingAverage.next(TerrainPixelHeight.get(i));
      smoothedTerrainPixelHeight.add(result);
    }

    smoothedTerrainPixelHeight.subList(0, 31).clear();

    return smoothedTerrainPixelHeight;

  }

  /**
   * This function calculates the moving average of the terrain heights from an
   * array list of floating points
   * 
   * @param terrainPixelHeights
   * @return
   */
  public ArrayList<Float> calculateMovingAverageFloat(ArrayList<Float> terrainPixelHeights) {
    ArrayList<Float> smoothedTerrainPixelHeight = new ArrayList<Float>();
    MovingAverage movingAverage = new MovingAverage(32);

    for (int i = 0; i < terrainPixelHeights.size(); i++) {
      float result = movingAverage.next(terrainPixelHeights.get(i));
      smoothedTerrainPixelHeight.add(result);
    }

    return smoothedTerrainPixelHeight;

  }
}