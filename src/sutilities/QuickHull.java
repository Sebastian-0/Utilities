/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public final class QuickHull
{
  private QuickHull()
  {
  }
  
  
  public static List<Point> compute(Point[] points)
  {
    List<Point> pruned = prune(points);
    
    List<Point> hull = new ArrayList<Point>();
    
    if (pruned.size() > 2)
    {
      Point minX = null;
      Point maxX = null;
      
      double min = Double.MAX_VALUE;
      double max = -Double.MAX_VALUE;
      
      for (Point p : pruned)
      {
        if (p.getX() < min)
        {
          minX = p;
          min = p.getX();
        }
        if (p.getX() > max)
        {
          maxX = p;
          max = p.getX();
        }
      }
      
      pruned.remove(minX);
      pruned.remove(maxX);
      hull.add(minX);
      hull.add(maxX);
      
      List<Point> left  = new ArrayList<Point>();
      List<Point> right = new ArrayList<Point>();
      
      for (Point p : pruned)
      {
        if (getPosition(minX, maxX, p) == -1)
          left.add(p);
        else
          right.add(p);
      }
      
      computeHull(minX, maxX, right, hull);
      computeHull(maxX, minX, left, hull);
    }
    else
    {
      for (Point p : pruned)
        hull.add(p);
    }
    
    return hull;
  }
  
  
  private static void computeHull(Point A, Point B, List<Point> set, List<Point> hull)
  {
    if (!set.isEmpty())
    {
      int insertIndex = hull.indexOf(B);
      if (set.size() == 1)
      {
        hull.add(insertIndex, set.get(0));
        set.remove(0);
      }
      else
      {
        double distance = Integer.MIN_VALUE;
        int index = 0;
        
        for (int i = 0; i < set.size(); i++)
        {
          Point p = set.get(i);
          
          double dst = getApproximateDistance(A, B, p);
          
          if (dst > distance)
          {
            distance = dst;
            index = i;
          }
        }
        
        Point furthest = set.get(index);
        
        hull.add(insertIndex, furthest);
        set.remove(index);

        List<Point> left  = new ArrayList<Point>();
        List<Point> right = new ArrayList<Point>();
        
        for (Point p : set)
        {
          if (getPosition(A, furthest, p) == 1)
            left.add(p);
          else if (getPosition(furthest, B, p) == 1)
            right.add(p);
        }
        
        computeHull(A, furthest, left, hull);
        computeHull(furthest, B, right, hull);
      }
    }
  }
  
  
  private static List<Point> prune(Point[] src)
  {
    List<Point> result = new LinkedList<Point>();
    
    Point topLeft     = src[0];
    Point topRight    = src[0];
    Point bottomLeft  = src[0];
    Point bottomRight = src[0];
    
    result.add(src[0]);
    
    double boundTopY    = topLeft.getY();
    double boundBottomY = topLeft.getY();
    double boundLeftX   = topLeft.getX();
    double boundRightX  = topLeft.getX();
    
    for (int i = 1; i < src.length; i++)
    {
      Point p = src[i];
      
      if (p.getX() + p.getY() > topRight.getX() + topRight.getY())
      {
        topRight = p;
        
        boundTopY   = Math.min(topRight.getY(), topLeft.getY());
        boundRightX = Math.min(topRight.getX(), bottomRight.getX());
      }
      if (p.getX() - p.getY() < topLeft.getX() - topLeft.getY())
      {
        topLeft = p;
        
        boundTopY  = Math.min(topRight.getY(), topLeft.getY());
        boundLeftX = Math.max(bottomLeft.getX(), topLeft.getX());
      }
      if (p.getX() - p.getY() > bottomRight.getX() - bottomRight.getY())
      {
        bottomRight = p;
        
        boundBottomY = Math.max(bottomRight.getY(), bottomLeft.getY());
        boundRightX  = Math.min(topRight.getX(), bottomRight.getX());
      }
      if (p.getX() + p.getY() < bottomLeft.getX() + bottomLeft.getY())
      {
        bottomLeft = p;

        boundBottomY = Math.max(bottomRight.getY(), bottomLeft.getY());
        boundLeftX   = Math.max(bottomLeft.getX(), topLeft.getX());
      }
      
      result.add(p);
    }

    ListIterator<Point> it = result.listIterator();
    while (it.hasNext())
    {
      Point p = it.next();
      
      if (p.getX() > boundLeftX   && p.getX() < boundRightX &&
          p.getY() > boundBottomY && p.getY() < boundTopY)
        it.remove();
    }
    
    return result;
  }
  
  
  private static double getPosition(Point A, Point B, Point M)
  {
    double cross = (B.getY() - A.getY()) * (M.getX() - A.getX()) - (B.getX() - A.getX()) * (M.getY() - A.getY());
    
    return cross <= 0 ? 1 : -1;
  }
  
  
  private static double getApproximateDistance(Point A, Point B, Point M)
  {
    double dx = B.getX() - A.getX();
    double dy = B.getY() - A.getY();
    
    double dst = dx * (M.getY() - A.getY()) - dy * (M.getX() - A.getX());
    
    return Math.abs(dst);
  }
}
