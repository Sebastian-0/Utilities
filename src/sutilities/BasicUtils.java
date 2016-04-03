/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * This class contains some minor utilities.
 * @author Sebastian Hjelm
 */
public final class BasicUtils
{
	private static String tempFolderName;
  private static File tempFolder;
  
  
  static
  {
  	tempFolderName = BasicUtils.class.getSimpleName();
  }
  
  private BasicUtils() { }
  
  
  /**
   * Sets than name of the temporary folder returned by {@link #temporaryFolderRoot(String)}.
   * @param folderName The name of the temporary folder root
   */
  public static void setTemporaryFolderRoot(String folderName)
  {
  	tempFolderName = folderName;
  }
  
  
  /**
   * Returns the temporary folder used by SBasicGUI. This folder is guaranteed
   *  to exist when returned.
   * @return The temporary folder used by SBasicGUI
   */
  public static File temporaryFolderRoot()
  {
    if (tempFolder == null)
    {
      tempFolder = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + tempFolderName);
      tempFolder.deleteOnExit();
      tempFolder.mkdir();
    }
    
    return tempFolder;
  }
  
  
  /**
   * Creates a new temporary folder as a sub folder to the root temporary folder
   *  (as returned by {@link #temporaryFolderRoot()}). If a folder with the same
   *  name already exists it is emptied of all its content.
   * @param subPath The sub path relative to the root path
   * @return The folder that corresponds to the specified sub path
   */
  public static File makeTemporaryFolder(String subPath)
  {
    File tmpFolder = new File(temporaryFolderRoot().getPath() + File.separator + subPath);
    tmpFolder.deleteOnExit();
    
    if (tmpFolder.exists())
    {
      clearDir(tmpFolder);
    }
    else
    {
      tmpFolder.mkdirs();
    }
    
    return tmpFolder;
  }
  
  private static void clearDir(File dir)
  {
    for (File f : dir.listFiles())
    {
      if (f.isDirectory())
      {
        clearDir(f);
      }
      f.delete();
    }
  }
  
  
  /**
   * Attempts to sleep for the specified amount of milliseconds. Any
   *  {@link InterruptedException} are caught and discarded.
   * @param millis The amount of milliseconds to sleep
   */
  public static void sleepSilently(int millis)
  {
    try
    {
      Thread.sleep(millis);
    } catch (InterruptedException e) { }
  }
  
  
  /**
   * Closes the specified closeable and discards any {@link IOException} that
   *  may occur. 
   * @param c The closeable to close
   */
  public static void closeSilently(Closeable c)
  {
    try {
      c.close();
    } catch (IOException e) { }
  }
  
  
  /**
   * Returns whether or not the specified class object resides within a jar-file.
   * @param classObject The class object to check
   * @return True if the specified class resides within a jar file, false otherwise
   */
  public static boolean isInJar(Class<?> classObject)
  {
    try
    {
      String path = URLDecoder.decode(classObject.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

      if (path.toLowerCase().endsWith(".jar") || path.toLowerCase().endsWith(".exe"))
        return true;
      
    } catch (UnsupportedEncodingException e) { }
    
    return false;
  }
  
  
  /**
   * Returns the path to the jar-file the specified class object resides in. This
   *  only works if the specified class is within a jar-file.
   * @param classObject The class object to check
   * @return The jar-file the specified class object resides in
   */
  public static String getJarPathOf(Class<?> classObject)
  {
    try
    {
      String path = URLDecoder.decode(classObject.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
      return path;
    } catch (UnsupportedEncodingException e) { }
    
    return "";
  }
}
