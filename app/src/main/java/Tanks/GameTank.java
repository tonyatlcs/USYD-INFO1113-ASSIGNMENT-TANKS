package Tanks;

import java.util.ArrayList;

import processing.core.*;

public class GameTank {

  PApplet parent;
  private float xcoord;
  private float ycoord;
  private int tankPower;
  boolean isMovingLeft;
  boolean isMovingRight;
  boolean isRotatingLeft;
  boolean isRotatingRight;
  float rotationAngle;
  private int speed;
  ArrayList<TankProjectile> tankProjectiles;
  float turretX1Pos;
  float turretY1Pos;
  float turretX2Pos;
  float turretY2Pos;
  float turretLength = 15;
  float projectilePower;
  float initalVelocity = 1;
  private ArrayList<Float> curvedTerrainHeight;
  private int totalProjectilesRendered = 0;
  private boolean craterCreated = false;
  private int craterXPos;
  private float craterYPos;

  GameTank(PApplet parent, ArrayList<Float> curvedTerrainHeight, float xcoord,
      float ycoord) {
    this.parent = parent;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    projectilePower = 1;
    this.tankPower = 50;
    isMovingLeft = false;
    isMovingRight = false;
    isRotatingLeft = false;
    isRotatingRight = false;
    rotationAngle = 0;
    this.speed = 1;
    tankProjectiles = new ArrayList<TankProjectile>();
    this.curvedTerrainHeight = curvedTerrainHeight;

  }

  public void drawTank(String color) {
    drawTankObject(color);

    // Draw projectile
    for (TankProjectile bullet : tankProjectiles) {
      bullet.updateProjectilePosition();
      float bulletXPos = bullet.getProjectileXPos();
      float bulletYPos = bullet.getProjectileYPos();

      // Check for collision
      if (bulletYPos + (8 / 2) >= bullet.getTerrainHeight(bulletXPos)) {

        if (!bullet.getIsExplosionTriggered()) {
          ProjectileExplosion explosion = new ProjectileExplosion(parent, bulletXPos, bulletYPos, 30);
          explosion.drawExplosion();
          bullet.setIsExplosionTriggered(true);
          bullet.setShowProjectile(false);
          createCrater((int) bulletXPos, 15);
          setCraterCreated(true);

        }

        // if position of tank is above terrain
        if (parent.height - curvedTerrainHeight.get((int) xcoord) > ycoord) {
          updateTankPosition((int) xcoord, parent.height - curvedTerrainHeight.get((int) xcoord), 60);
        }

      }

      if (bulletYPos + (8 / 2) < bullet.getTerrainHeight(bulletXPos) && bullet.getShowProjectile()) {
        bullet.drawProjectile();
      }
    }

  }

  /*
   * Getters
   */

  public int getTankPower() {
    return tankPower;
  }

  public int getSpeed() {
    return speed;
  }

  public ArrayList<TankProjectile> getTankProjectiles() {
    return tankProjectiles;
  }

  public float getXCoord() {
    return xcoord;
  }

  public float getYCoord() {
    return ycoord;
  }

  public ArrayList<Float> getCurvedTerrainHeight() {
    return curvedTerrainHeight;
  }

  public boolean getCraterCreated() {
    return craterCreated;
  }

  public int getTotalProjectilesRendered() {
    return totalProjectilesRendered;
  }

  public int getCraterXPos() {
    return craterXPos;
  }

  public float getCraterYPos() {
    return craterYPos;
  }

  /*
   * Setters
   */

  public void setTankPower(int tankPower) {
    this.tankPower = tankPower;
  }

  public void setTankProjectiles(ArrayList<TankProjectile> tankProjectiles) {
    this.tankProjectiles = tankProjectiles;
  }

  public void setCurvedTerrainHeight(ArrayList<Float> curvedTerrainHeight) {
    this.curvedTerrainHeight = curvedTerrainHeight;
  }

  public void setCraterCreated(boolean craterCreated) {
    this.craterCreated = craterCreated;
  }

  public void setTotalProjectilesRendered(int totalProjectilesRendered) {
    this.totalProjectilesRendered = totalProjectilesRendered;
  }

  public void setCraterXPos(int craterXPos) {
    this.craterXPos = craterXPos;
  }

  public void setCraterYPos(float craterYPos) {
    this.craterYPos = craterYPos;
  }

  /*
   * Tank physics functions
   */

  private void rotateTank() {
    turretX1Pos = xcoord - 0.75f;
    turretY1Pos = ycoord - 15;
    turretX2Pos = xcoord + 30;
    turretY2Pos = ycoord;

    turretX2Pos = turretX1Pos + turretLength * PApplet.cos(rotationAngle);
    turretY2Pos = turretY1Pos + turretLength * PApplet.sin(rotationAngle);
  }

  public void moveTank() {

    if (isMovingLeft) {
      xcoord = xcoord - speed;
      ycoord = parent.height - curvedTerrainHeight.get((int) xcoord);
    }

    if (isMovingRight) {
      xcoord = xcoord + speed;
      ycoord = parent.height - curvedTerrainHeight.get((int) xcoord);
    }
  }

  public float loopThroughProjectilePower(float power) {
    float result = 0;
    if (power >= 9) {
      result = 9;
    } else {
      result = power;
    }
    return result;
  }

  public void createCrater(int XPos, int blastRadius) {
    for (int i = 0; i < curvedTerrainHeight.size(); i++) {
      if (i >= XPos - blastRadius && i <= XPos + blastRadius) {
        float distance = PApplet.dist(XPos, 0, i, 0);
        float height = blastRadius + PApplet.sqrt(blastRadius * blastRadius - distance * distance);
        curvedTerrainHeight.set(i, curvedTerrainHeight.get(i) - height);
      }
    }
  }

  public void updateTankPosition(int xcoord, float ycoord, float speed) {

    float changeInY = 1.0f / speed;

    float distanceY = speed * changeInY;

    this.xcoord = xcoord;
    this.ycoord += distanceY;

  }

  public void drawTankObject(String color) {
    String[] colorArr = color.split(",");

    parent.noStroke();
    parent.fill(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
    parent.rect(xcoord - 10, ycoord - 10, 20, 6);
    parent.noStroke();
    parent.fill(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
    parent.rect(xcoord - 5, ycoord - 15, 10, 5);

    // Draw tank turret
    parent.stroke(0);
    parent.strokeWeight(2);
    rotateTank();
    parent.line(turretX1Pos, turretY1Pos, turretX2Pos, turretY2Pos);
  }

  /*
   * tank key press functions
   */
  public void keyPressed() {
    if (parent.keyCode == ' ') {
      System.out.println("Projectile power: " + initalVelocity * loopThroughProjectilePower(projectilePower));
      tankProjectiles.add(
          new TankProjectile(parent,
              turretX2Pos, turretY2Pos, initalVelocity * loopThroughProjectilePower(projectilePower), 3.6f,
              PApplet.degrees(-rotationAngle),
              0.05f, curvedTerrainHeight));
    }
  }
}
