package Tanks;

import processing.core.*;

public class ProjectileExplosion {
  private PApplet parent;
  private float impactXPos;
  private float impactYPos;
  private int blastRadius;
  int startTime;
  int animationTimeInterval;

  ProjectileExplosion(PApplet parent, float impactXPos, float impactYPos, int blastRadius) {
    this.parent = parent;
    this.impactXPos = impactXPos;
    this.impactYPos = impactYPos;
    this.startTime = parent.millis();
    this.animationTimeInterval = 1000;
    this.blastRadius = blastRadius;
  }

  /**
   * Getters
   */

  public float getImpactXPos() {
    return impactXPos;
  }

  public float getImpactYPos() {
    return impactYPos;
  }

  public void drawExplosion() {

    parent.noStroke();
    parent.fill(255, 0, 0);
    parent.ellipse(impactXPos, impactYPos, blastRadius, blastRadius);

    parent.noStroke();
    parent.fill(255, 165, 0);
    parent.ellipse(impactXPos, impactYPos, 15, 15);

    parent.noStroke();
    parent.fill(255, 255, 0);
    parent.ellipse(impactXPos, impactYPos, 6, 6);

  }
}
