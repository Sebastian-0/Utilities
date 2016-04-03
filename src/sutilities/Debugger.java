/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

/**
 * This class is used for printing debug/error messages to the default stream
 *  and/or logs. Depending on what type of message that is to be printed the
 *  appropriate method should be chosen. The typical usage is described as follows:
 * <ul>
 *  <li>{@code print():} Ordinary messages, always SYSO. Logged if specified.</li>
 *  <li>{@code debug():} Debug messages, SYSO if in 'debug mode'. Logged if specified.</li>
 *  <li>{@code warning():} Warnings, SYSO if in 'debug mode'. Always logged.</li>
 *  <li>{@code error():} Minor/non-fatal errors, SYSO if in 'debug mode'. Always logged.</li>
 *  <li>{@code fatal():} Fatal errors/'show-stoppers', always SYSE. Always logged.</li>
 * </ul>
 * <b>Note:</b> SYSO means that the message is printed to the default out stream,
 *  and SYSE means that the message is printed to the default error stream. 
 * @author Sebastian Hjelm
 */
public final class Debugger
{
  private static boolean         isInDebugMode_;
  private static LoggerInterface logger_;
  
  private Debugger()
  {
  }
  
  
  /**
   * Sets whether or not the debugger is in 'debug mode'. When it is in debug mode
   *  all messages received in the {@code debug()}, {@code warning()} and
   *  {@code error()} methods are printed to the default out stream. To print
   *  messages that always are printed using the default out stream use
   *  {@link #print(String, String, boolean)} instead).
   * @param isInDebugMode The new state
   */
  public static void setIsInDebugMode(boolean isInDebugMode)
  {
    isInDebugMode_ = isInDebugMode;
  }
  
  /**
   * Sets the logger to use when printing messages and errors, there is no logger
   *  by default.
   * @param logger The logger to use
   */
  public static void setLogger(LoggerInterface logger)
  {
    logger_ = logger;
  }
  
  /**
   * Returns whether or not the debugger is in 'debug mode'. When it is in debug mode
   *  all messages received in the {@code debug()}, {@code warning()} and
   *  {@code error()} methods are printed to the default out stream. To print
   *  messages that always are printed using the default out stream use
   *  {@link #print(String, String, boolean)} instead).
   * @return Whether or not the debugger is in 'debug mode'
   */
  public static boolean isInDebugMode()
  {
    return isInDebugMode_;
  }
  
  /**
   * Returns the current logger that is used when printing messages, default is
   * <code>null</code>.
   * @return The current logger, or <code>null</code> if there is no logger
   */
  public static LoggerInterface getLogger()
  {
    return logger_;
  }

  
  /**
   * Prints the specified message to the default stream and to the log if specified.
   * @param location The class and method the error occurred in, ex: "Debugger: print()"
   * @param message The message to print
   * @param logToFile Whether or not the message should be logged
   */
  public static void print(String location, String message, boolean logToFile)
  {
    System.out.println(location + ": " + message);
    if (logger_ != null && logToFile)
      logger_.log("MESSAGE", location, message, null, false);
  }
  
  /**
   * Prints the specified message to the default stream (if
   *  the debugger is in debug mode), and to the log if specified.
   * @param message The message to print
   * @param logToFile Whether or not the message should be logged
   */
  public static void debug(String location, String message, boolean logToFile)
  {
    if (isInDebugMode_)
      System.out.println(location + ": " + message);
    
    if (logger_ != null && logToFile)
      logger_.log("DEBUG", location, message, null, false);
  }
  
  /**
   * Prints the specified warning to the log, and to the default stream (if
   *  the debugger is in debug mode).
   * @param error The message to print
   */
  public static void warning(String location, String error)
  {
    if (isInDebugMode_)
      System.out.println(location + ": " + error);
    
    if (logger_ != null)
      logger_.log("WARNING", location, error, null, false);
  }
  
  /**
   * Prints the specified warning and cause (exception/error) to the log, and to
   *  the default stream (if the debugger is in debug mode).
   * @param error The message to print
   * @param cause The exception/error that caused the message
   */
  public static void warning(String location, String error, Throwable cause)
  {
    if (isInDebugMode_)
    {
      System.out.println(location + ": " + error);
      cause.printStackTrace();
    }
    
    if (logger_ != null)
      logger_.log("WARNING", location, error, cause, true);
  }
  
  /**
   * Prints the specified error to the log, and to the default stream (if
   *  the debugger is in debug mode).
   * @param error The message to print
   */
  public static void error(String location, String error)
  {
    if (isInDebugMode_)
      System.out.println(location + ": " + error);
    
    if (logger_ != null)
      logger_.log("ERROR", location, error, null, false);
  }
  
  /**
   * Prints the specified error and cause (exception/error) to the log, and to
   *  the default stream (if the debugger is in debug mode).
   * @param error The message to print
   * @param cause The exception/error that caused the message
   */
  public static void error(String location, String error, Throwable cause)
  {
    if (isInDebugMode_)
    {
      System.out.println(location + ": " + error);
      cause.printStackTrace();
    }
    
    if (logger_ != null)
      logger_.log("ERROR", location, error, cause, true);
  }

  /**
   * Prints the specified error to the log and to the error stream.
   * @param error The error message to print
   */
  public static void fatal(String location, String error)
  {
    System.err.println(location + ": " + error);
    
    if (logger_ != null)
      logger_.log("SEVERE", location, error, null, false);
  }

  /**
   * Prints the specified error and cause to the log and to the error stream.
   * @param error The error message to print
   * @param cause The exception/error that caused the error
   */
  public static void fatal(String location, String error, Throwable cause)
  {
    System.err.println(location + ": " + error);
    cause.printStackTrace();
    
    if (logger_ != null)
      logger_.log("SEVERE", location, error, cause, true);
  }
}
