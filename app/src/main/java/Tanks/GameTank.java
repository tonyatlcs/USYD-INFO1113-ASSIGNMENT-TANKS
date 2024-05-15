package Tanks;

import java.util.ArrayList;

import processing.core.*;

public class GameTank {

  PApplet parent;
  private float xcoord;
  private float ycoord;
  private int tankHealth;
  boolean isMovingLeft;
  boolean isMovingRight;
  boolean isRotatingLeft;
  boolean isRotatingRight;
  float rotationAngle;
  private int speed;
  TankProjectile tankProjectile;
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
  private float craterXPos;
  private float craterYPos;
  float windAcceleration;
  float windVelocityX;
  String windDirection;
  private int blastRadius;

  GameTank(PApplet parent, ArrayList<Float> curvedTerrainHeight, float xcoord, float windAcceleration,
      float windVelocityX,
      float ycoord) {
    this.parent = parent;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    projectilePower = 1;
    isMovingLeft = false;
    isMovingRight = false;
    isRotatingLeft = false;
    isRotatingRight = false;
    rotationAngle = 0;
    this.speed = 1;
    this.curvedTerrainHeight = curvedTerrainHeight;
    this.windAcceleration = windAcceleration;
    this.windVelocityX = windVelocityX;
    this.tankProjectile = new TankProjectile(parent, 0, 0, 0, -10, -10, 0, 0, curvedTerrainHeight);
    this.tankHealth = 100;

  }

  public void drawTank(String color) {

    drawTankObject(color);

    // Draw projectile

    tankProjectile.updateProjectilePosition();
    float bulletXPos = tankProjectile.getProjectileXPos();
    float bulletYPos = tankProjectile.getProjectileYPos();

    // Check for collision
    if (bulletYPos + (8 / 2) >= tankProjectile.getTerrainHeight(bulletXPos)) {
      if (!tankProjectile.getIsExplosionTriggered()) {
        ProjectileExplosion explosion = new ProjectileExplosion(parent, bulletXPos, bulletYPos, 30);

        explosion.drawExplosion();
        tankProjectile.setIsExplosionTriggered(true);
        tankProjectile.setShowProjectile(false);
        createCrater((int) bulletXPos, 15);
        setCraterXPos(tankProjectile.getProjectileXPos());
        setCraterYPos(tankProjectile.getProjectileYPos());
        setCraterCreated(true);
      }

      if (parent.height - curvedTerrainHeight.get((int) xcoord) > ycoord) {
        updateTankPosition((int) xcoord, parent.height - curvedTerrainHeight.get((int) xcoord), 60);
      }
    }

    if (bulletYPos + (8 / 2) < tankProjectile.getTerrainHeight(bulletXPos) && tankProjectile.getShowProjectile()) {
      tankProjectile.drawProjectile();
    }
  }

  /*
   * Getters
   */

  public int getSpeed() {
    return speed;
  }

  public TankProjectile getTankProjectile() {
    return tankProjectile;
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

  public float getCraterXPos() {
    return craterXPos;
  }

  public float getCraterYPos() {
    return craterYPos;
  }

  public float getWindAcceleration() {
    return windAcceleration;
  }

  public int getBlastRadius() {
    return blastRadius;
  }

  public int getTankHealth() {
    return tankHealth;
  }

  /*
   * Setters
   */

  public void setTankProjectiles(TankProjectile tankProjectile) {
    this.tankProjectile = tankProjectile;
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

  public void setCraterXPos(float craterXPos) {
    this.craterXPos = craterXPos;
  }

  public void setCraterYPos(float craterYPos) {
    this.craterYPos = craterYPos;
  }

  public void setBlastRadius(int blastRadius) {
    this.blastRadius = blastRadius;
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
    setBlastRadius(blastRadius);
    for (int i = 0; i < curvedTerrainHeight.size(); i++) {
      if (i >= XPos - blastRadius && i <= XPos + blastRadius) {
        float distance = PApplet.dist(XPos, 0, i, 0);
        float height = blastRadius + PApplet.sqrt(blastRadius * blastRadius - distance * distance);
        curvedTerrainHeight.set(i, curvedTerrainHeight.get(i) - height);
      }
    }
  }

  public void updateTankPosition(int xcoord, float ycoord, float speed) {
    PImage parachute = parent.loadImage("parachute.png");
    float changeInY = 1.0f / speed;
    float distanceY = speed * changeInY;

    this.xcoord = xcoord;
    this.ycoord += distanceY;

    float imageX = this.xcoord - parachute.width / 2;
    float imageY = this.ycoord - parachute.height - 10;

    parent.image(parachute, imageX, imageY);
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

  public void resetWindComponent() {
    windAcceleration = (int) parent.random(-5, 5) * 0.03f;
  }

  /*
   * tank key press functions
   */
  public void keyPressed() {
    if (parent.keyCode == ' ') {
      resetWindComponent();

      System.out.println("Projectile power: " + initalVelocity * loopThroughProjectilePower(projectilePower));
      tankProjectile = new TankProjectile(parent,
          windAcceleration,
          turretX2Pos, turretY2Pos, initalVelocity * loopThroughProjectilePower(projectilePower), 3.6f,
          PApplet.degrees(-rotationAngle),
          0.05f, curvedTerrainHeight);
    }
  }

  public int applyHealthDamage(float blastXPos, float blastYPos, float radius) {
    float distance = PApplet.dist(
        blastXPos,
        blastYPos,
        xcoord,
        ycoord);
    System.out.println(blastXPos);
    System.out.println(blastYPos);
    System.out.println(xcoord);
    System.out.println(ycoord);
    if (PApplet.abs(distance - radius) < 15f) {
      tankHealth = tankHealth - 30;
      System.out.println(tankHealth);

      return tankHealth;
    } else if (PApplet.abs(distance - radius) < 10f) {
      tankHealth = tankHealth - 40;
      System.out.println(tankHealth);

      return tankHealth;
    } else if (PApplet.abs(distance - radius) < 20f) {
      tankHealth = tankHealth - 20;
      System.out.println(tankHealth);
      return tankHealth;
    } else {
      return tankHealth - 0;
    }

  }
}
