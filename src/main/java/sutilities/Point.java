/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class Point extends Point2D implements Poolable, Serializable {
  
  /**
   * The serial version UID for this class.
   */
  private static final long serialVersionUID = -7019116493816101649L;
  
  
  /**
   * The x coordinate of this point, default is 0.
   */
  public float x;
  /**
   * The y coordinate of this point, default is 0.
   */
  public float y;
  
  
  /**
   * Constructs an empty point.
   */
  public Point() {
  }
  
  
  /**
   * Constructs a point with the specified coordinates.
   * @param x The x coordinate
   * @param y The y coordinate
   */
  public Point(float x, float y)
  {
    this.x = x;
    this.y = y;
  }
  

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public void setLocation(double x, double y) {
    this.x = (float)x;
    this.y = (float)y;
  }
  
  
  /**
   * Translates this point by the specified amount.
   * @param dx The x amount
   * @param dy The y amount
   */
  public void translate(float dx, float dy)
  {
    x += dx;
    y += dy;
  }
  
  
  @Override
  public String toString()
  {
    return "[x=" + x + ",y=" + y + "]";
  }
}
