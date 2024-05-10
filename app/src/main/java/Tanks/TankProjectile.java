package Tanks;

import java.util.ArrayList;

import processing.core.*;

public class TankProjectile {
  PApplet parent;
  private float projectileXPos, projectileYPos;
  private float projectileXVel, projectileYVel;

  private float gravityConstant;
  private float timeStepConstant;
  private float projectilePower;

  private float intialVelocity;
  private float launchAngle;
  private boolean showProjectile;
  private ArrayList<Float> terrainHeights;

  TankProjectile(
      PApplet parent,
      float projectileXPos,
      float projectileYPos,
      float intialVelocity,
      float gravityConstant,
      float launchAngle,
      float timeStepConstant,
      ArrayList<Float> terrainHeights) {
    this.parent = parent;
    this.projectileXPos = projectileXPos;
    this.projectileYPos = projectileYPos;
    this.intialVelocity = intialVelocity;
    this.gravityConstant = gravityConstant;
    this.launchAngle = PApplet.radians(launchAngle);
    this.timeStepConstant = timeStepConstant;
    this.terrainHeights = terrainHeights;
    this.projectileXVel = intialVelocity * (float) PApplet.cos(PApplet.radians(launchAngle));
    this.projectileYVel = -intialVelocity * (float) PApplet.sin(PApplet.radians(launchAngle));

  }

  /*
   * Getters
   */
  public float getProjectileXPos() {
    return projectileXPos;
  }

  public float getProjectileYPos() {
    return projectileYPos;
  }

  public float getProjectileXVel() {
    return projectileXVel;
  }

  public float getProjectileYVel() {
    return projectileYVel;
  }

  public float getGravityConstant() {
    return gravityConstant;
  }

  public float getTimeStepConstant() {
    return timeStepConstant;
  }

  public float getIntialVelocity() {
    return intialVelocity;
  }

  public float getLaunchAngle() {
    return launchAngle;
  }

  public float getProjectilePower() {
    return projectilePower;
  }

  public boolean getShowProjectile() {
    return showProjectile;
  }
  /*
   * Setters
   */

  public void setShowProjectile(boolean showProjectile) {
    this.showProjectile = showProjectile;
  }

  public void updateProjectilePosition() {
    projectileXPos += projectileXVel;
    projectileYPos += projectileYVel;
    projectileYVel += (gravityConstant * timeStepConstant);
  }

  public void drawProjectile() {
    parent.noStroke();
    parent.ellipse(projectileXPos, projectileYPos, 8, 8);
  }

  public float getTerrainHeight(float xPos) {
    int index = PApplet.constrain(PApplet.round(xPos), 0, parent.width - 1);
    return parent.height - terrainHeights.get(index);
  }

  public float[] checkProjectileExplotion() {
    return new float[2];
  }
}
