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
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class NativeLibraryLoader
{
  private static boolean isWindows_;
  private static boolean isLinux_;
  private static boolean isMac_;
  private static boolean is64bit;
  
  private static HashSet<String> loadedLibs_;
  
  static
  {
    isWindows_ = System.getProperty("os.name").contains("Windows");
    isLinux_   = System.getProperty("os.name").contains("Linux");
    isMac_     = System.getProperty("os.name").contains("Mac");
    is64bit    = System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64");
    
    loadedLibs_ = new HashSet<String>();
  }
  
  private boolean accept(String fileName)
  {
    if (isWindows_)
    {
      return acceptWithExtension(fileName, ".dll");
    }
    else if (isLinux_)
    {
      return acceptWithExtension(fileName, ".so");
    }
    else if (isMac_)
    {
      return acceptWithExtension(fileName, ".dylib");
    }
    else
    {
      return acceptWithExtension(fileName, ".so");
    }
  }
  
  private boolean acceptWithExtension(String fileName, String extension)
  {
    if (!fileName.endsWith(extension))
      return false;
    
    return fileName.endsWith("64" + extension) == is64bit;
  }
  
  
  public File loadLibraries(String jarPath)
  {
    if (!loadedLibs_.contains(jarPath))
    {
      try
      {
        ZipFile zip = new ZipFile(new File(jarPath));
        
        File tmpFolder = BasicUtils.makeTemporaryFolder("natives" + File.separator + extractLibName(jarPath));
        
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements())
        {
          ZipEntry entry = entries.nextElement();
          if (!entry.isDirectory()) // Ignore directories
          {
            if (accept(entry.getName()))
            {
              File outFile = new File(tmpFolder.getAbsolutePath() + File.separatorChar + entry.getName());
              
              if (!outFile.exists())
              {
                InputStream  in  = zip.getInputStream(entry);
                OutputStream out = new FileOutputStream(outFile);
                
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
              
              try
              {
                System.load(outFile.getAbsolutePath());
              }
              catch (UnsatisfiedLinkError error)
              {
                // For some reason liblwjgl64.so always fails to load but still works, therefore no exception is thrown here
                Debugger.warning("NativeLibraryLoader: loadLibraries()", "Error: Failed to load lib: " + outFile.getName());
              }
            }
          }
        }
        
        loadedLibs_.add(jarPath);
        
        zip.close();
        return tmpFolder;
      } catch (ZipException e)
      {
        throw new IllegalArgumentException("Corrupt jar archive!", e);
      } catch (IOException e)
      {
        throw new IllegalArgumentException("Couldn't open/find jar archive, or failed to extract files!", e);
      }
    }

    return new File(BasicUtils.temporaryFolderRoot().getPath() + File.separator + "natives" + File.separator + extractLibName(jarPath));
  }
  
  private String extractLibName(String path)
  {
    File file = new File(path);
    String name = file.getName();
    int index = name.lastIndexOf('.');
    if (index != -1)
      name = name.substring(0, index);
    return name;
  }
}
