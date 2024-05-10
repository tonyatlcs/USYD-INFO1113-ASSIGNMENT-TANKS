package Tanks;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import processing.data.JSONObject;

public class GameBackground extends PApplet {
  PImage background_image;
  AppUtils appUtils;
  String level1BackgroundImageFile;
  String level1TreeFile;
  JSONObject level1PlayerColors;
  ArrayList<Integer> level1Terrain;
  ArrayList<Integer> level1Trees;
  ArrayList<Integer> level1Tanks;
  ArrayList<Float> smoothedLevel1Terrain;
  ArrayList<Float> curvedLevel1Terrain;
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
    level1BackgroundImageFile = appUtils.readLevelConfig("config.json", 1, "background");
    level1TreeFile = appUtils.readLevelConfig("config.json", 1, "trees");
    level1PlayerColors = appUtils.readPlayerColors("config.json");

    // Load necessary player information
    level1Terrain = level1.loadTerrainObjects("level1Terrain.txt");
    level1Trees = level1.loadTerrainObjects("level1Tree.txt");
    level1Tanks = level1.loadTerrainObjects("level1Tanks.txt");

    // Calculate smoothed terrain
    smoothedLevel1Terrain = level1.calculateMovingAverageInt(level1Terrain);
    curvedLevel1Terrain = level1.calculateMovingAverageFloat(smoothedLevel1Terrain);

    // Set up game players
    level1.setGamePlayers(curvedLevel1Terrain, level1Tanks, level1PlayerColors);
    currentPlayer = level1.getGamePlayers().get("A");
    System.out.println(currentPlayer.getPlayerName());

  }

  public void settings() {
    size(864, 640);
  }

  public void draw() {
    // Background
    background_image = loadImage(level1BackgroundImageFile);
    image(background_image, 0, 0);

    // Render Terrain
    level1.drawTerrain(curvedLevel1Terrain);

    // Render Trees
    level1.drawTrees(curvedLevel1Terrain, level1Trees, level1TreeFile);

    // Render Tanks
    level1.drawTanks(curvedLevel1Terrain, level1Tanks, level1PlayerColors);

    currentPlayer.getPlayerTank().setTerrainHeights(curvedLevel1Terrain);

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
