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
 * This exception is thrown if an error occurs within {@link Pool}. 
 * @author Sebastian Hjelm
 */
public class PoolException extends RuntimeException
{
  private static final long serialVersionUID = 8186246368223754048L;

  /**
   * Creates a new {@code PoolException} with the specified message.
   * @param message The message for the exception to contain
   */
  public PoolException(String message)
  {
    super (message);
  }
  
  /**
   * Creates a new {@code PoolException} with the specified message, caused
   *  by the specified throwable.
   * @param message The message for the exception to contain
   * @param cause The cause of this exception
   */
  public PoolException(String message, Throwable cause)
  {
    super (message, cause);
  }
}
