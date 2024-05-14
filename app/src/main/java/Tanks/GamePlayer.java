package Tanks;

public class GamePlayer {
  private String playerName;
  private String playerTextColor;
  private String playerTankColor;
  private int playerTankFuel;
  private int playerTankHealth;
  private int playerTankScore;
  private GameTank playerTank;
  private float windAcceleration;
  private float windVelocityX;

  public GamePlayer(String playerName, String playerTextColor, String playerTankColor, GameTank playerTank,
      float windAcceleration, float windVelocityX) {
    this.playerName = playerName;
    this.playerTextColor = playerTextColor;
    this.playerTankColor = playerTankColor;
    this.playerTank = playerTank;
    this.playerTankFuel = 250;
    this.playerTankHealth = 100;
    this.playerTankScore = 0;
    this.windAcceleration = windAcceleration;
    this.windVelocityX = windVelocityX;

  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getPlayerTextColor() {
    return playerTextColor;
  }

  public void setPlayerTextColor(String playerTextColor) {
    this.playerTextColor = playerTextColor;
  }

  public String getPlayerTankColor() {
    return playerTankColor;
  }

  public void setPlayerTankColor(String playerTankColor) {
    this.playerTankColor = playerTankColor;
  }

  public int getPlayerTankFuel() {
    return playerTankFuel;
  }

  public void setPlayerTankFuel(int playerTankFuel) {
    this.playerTankFuel = playerTankFuel;
  }

  public int getPlayerTankHealth() {
    if (playerTankHealth < 0) {
      playerTankHealth = 0;
    }
    return playerTankHealth;
  }

  public float getWindAcceleration() {
    return windAcceleration;
  }

  public float getWindVelocityX() {
    return windVelocityX;
  }

  public void setPlayerTankHealth(int playerTankHealth) {
    this.playerTankHealth = playerTankHealth;
  }

  public int getPlayerTankScore() {
    return playerTankScore;
  }

  public void setPlayerTankScore(int playerTankScore) {
    this.playerTankScore = playerTankScore;
  }

  public GameTank getPlayerTank() {
    return playerTank;
  }

  public void setPlayerTank(GameTank playerTank) {
    this.playerTank = playerTank;
  }
}
