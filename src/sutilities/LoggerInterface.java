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
 * This is an interface that describes a logger. The purpose of this interface
 *  is to link the {@link Debugger} with the preferred logger implementation.
 * @author Sebastian Hjelm
 */
public interface LoggerInterface
{
  /**
   * Logs an error or a message, depending on what parameters are passed. The
   *  message type describes the log entry header, for example "INFO", or "ERROR",
   *  the class name describes in which class the error occurred, the message is
   *  the message itself, which can be passed along with an exception/error, and finally
   *  the exception is either printed as only the message or the whole stack trace,
   *  depending on the settings.
   * @param messageType What type of message that is to be logged, ex: "INFO", "ERROR"
   * @param className The name of the class in which the message was caused (may
   *  be <code>null</code>)
   * @param message The message to log
   * @param cause The exception/error that caused the message (may be
   *  <code>null</code>)
   * @param includeStacktrace Whether or not to include the stacktrace of the
   *  exception when logging (exception message is always logged)
   * @return True if the message was logged successfully, or false otherwise
   */
  public abstract boolean log(String messageType, String className, String message, Throwable cause, boolean includeStacktrace);
}
