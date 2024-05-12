package Tanks;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import processing.data.JSONObject;

public class GameBackground extends PApplet {
  PImage background_image;
  AppUtils appUtils;
  String levelBackgroundImageFile;
  String levelTreeFile;
  JSONObject levelPlayerColors;
  ArrayList<Integer> levelTerrain;
  ArrayList<Integer> levelTrees;
  ArrayList<Integer> levelTanks;
  ArrayList<Float> smoothedLevelTerrain;
  ArrayList<Float> curvedLevelTerrain;
  HashMap<String, GamePlayer> gamePlayers;
  GameTerrain level1;
  GamePlayer currentPlayer;
  String currentPlayerName;
  int currentPlayerNameIndex = 0;
  float tankTurrentAngle = 0f;
  ArrayList<TankProjectile> tankProjectiles;

  public void setup() {
    appUtils = new AppUtils(this);
    level1 = new GameTerrain(this, this);

    // Load level 1 background image, tree file, and player colors
    levelBackgroundImageFile = appUtils.readLevelConfig("config.json", 1, "background");
    levelTreeFile = appUtils.readLevelConfig("config.json", 1, "trees");
    levelPlayerColors = appUtils.readPlayerColors("config.json");

    // Load necessary player information
    levelTerrain = level1.loadTerrainObjects("level1Terrain.txt");
    levelTrees = level1.loadTerrainObjects("level1Tree.txt");
    levelTanks = level1.loadTerrainObjects("level1Tanks.txt");

    // Calculate smoothed terrain
    smoothedLevelTerrain = level1.calculateMovingAverageInt(levelTerrain);
    curvedLevelTerrain = level1.calculateMovingAverageFloat(smoothedLevelTerrain);

    // Set up game players
    level1.setGamePlayers(curvedLevelTerrain, levelTanks, levelPlayerColors);
    currentPlayer = level1.getGamePlayers().get("A");
    System.out.println(currentPlayer.getPlayerName());

  }

  public void settings() {
    size(864, 640);
  }

  public void draw() {
    // Background
    background_image = loadImage(levelBackgroundImageFile);
    image(background_image, 0, 0);

    // Render Terrain
    level1.drawTerrain(curvedLevelTerrain);

    // Render Trees
    level1.drawTrees(curvedLevelTerrain, levelTrees, levelTreeFile);

    // Render Tanks
    level1.drawTanks(curvedLevelTerrain, levelTanks, levelPlayerColors);

  }

  public void keyPressed() {
    String[] playersNames = level1.getGamePlayers().keySet().toArray(new String[0]);
    if (key == CODED) {
      if (keyCode == RIGHT) {
        currentPlayer.getPlayerTank().isMovingRight = true;
        currentPlayer.getPlayerTank().moveTank();
      }

      if (keyCode == LEFT) {
        currentPlayer.getPlayerTank().isMovingLeft = true;
        currentPlayer.getPlayerTank().moveTank();
      }

      if (keyCode == DOWN && currentPlayer.getPlayerTank().rotationAngle < 0) {
        System.out.println("DOWN");
        currentPlayer.getPlayerTank().isRotatingRight = true;
        currentPlayer.getPlayerTank().rotationAngle += radians(5);
      }

      if (keyCode == UP && currentPlayer.getPlayerTank().rotationAngle > -PI) {
        System.out.println("UP");
        currentPlayer.getPlayerTank().isRotatingRight = true;
        currentPlayer.getPlayerTank().rotationAngle -= radians(5);
      }
    }

    if (key == 'w') {
      currentPlayer.getPlayerTank().projectilePower += 1;
    }

    if (key == ' ') {
      currentPlayer.getPlayerTank().keyPressed();
      switchPlayer(playersNames);
    }
  }

  public void keyReleased() {
    if (key == CODED) {
      if (keyCode == RIGHT) {
        currentPlayer.getPlayerTank().isMovingRight = false;
      }

      if (keyCode == LEFT) {
        currentPlayer.getPlayerTank().isMovingLeft = false;
      }
    }
  }

  public void switchPlayer(String[] playersNames) {
    currentPlayerNameIndex = (currentPlayerNameIndex + 1) % playersNames.length;
    currentPlayerName = playersNames[currentPlayerNameIndex];
    currentPlayer = level1.getGamePlayers().get(currentPlayerName);
  }

}
