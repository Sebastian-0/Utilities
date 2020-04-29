/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class contains some utilities for zip-files to make them easier to work
 *  with.
 * @author Sebastian Hjelm
 */
public final class ZipUtils
{
  private ZipUtils() { }
  
  
  /**
   * Extracts the specified zip-file to the specified directory.
   * @param zip The zip-file to extract
   * @param destinationFolder The target directory
   */
  public static void extract(ZipFile zip, File destinationFolder)
  {
    Enumeration<? extends ZipEntry> entries = zip.entries();
    while (entries.hasMoreElements())
    {
      ZipEntry entry = entries.nextElement();
      extract(zip, entry, destinationFolder);
    }
  }
  
  
  /**
   * Extracts the specified zip-entry of the specified zip-file to the specified
   *  directory.
   * @param zip The zip file whose zip-entry is to be extracted
   * @param entry The zip-entry to extract
   * @param destinationFolder The target folder
   */
  public static void extract(ZipFile zip, ZipEntry entry, File destinationFolder)
  {
    try
    {
      if (entry.isDirectory())
      {
        File subDir = new File(destinationFolder.getPath() + File.separator + entry.getName());
        subDir.mkdirs();
      }
      else
      {
        File destination = new File(destinationFolder.getPath() + File.separator + entry.getName());
        destination.getParentFile().mkdirs();
        
        InputStream  in  = zip.getInputStream(entry);
        OutputStream out = new FileOutputStream(destination);
        
        byte[] data = new byte[1024];
        
        int count = in.read(data);
        while (count > 0)
        {
          out.write(data, 0, count);
          count = in.read(data);
        }
        
        in.close();
        out.close();
      }
    }
    catch (IOException exception) { }
  }
}
