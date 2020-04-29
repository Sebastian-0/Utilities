/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A generic class that describes an object pool where objects can be stored and
 *  acquired to avoid unnecessary creations of new object instances.
 * @author Sebastian Hjelm
 *
 * @param <E> Which type of objects this pool should contain, must be {@code Poolable}
 */
public class Pool<E extends Poolable> {
  
  private Object lock_;
  
  private Class<E> class_;
  private Constructor<E> constructor_;
  
  private Class<?>[] parameterTypes_;
  private Object[]   argumentList_;
  
  private volatile int balance_;
  
  /**
   * The first node that contains an unused object.
   */
  private volatile PoolNode<E> firstObjectNode_;
  /**
   * The first unused node.
   */
  private volatile PoolNode<E> firstStoredNode_;
  
  /**
   * Creates a new empty object pool, using the specified class and parameter
   *  list. The parameter list is used to invoke the appropriate constructor
   *  on the type this class contains (E) when creating new objects. The list
   *  may be empty if the default constructor is to be used.
   * </br>
   * </br>If you use specify a parameter list you <i>must</i> invoke
   *  {@link #setArgumentList(Object...)} to set the arguments to use when
   *  instantiating objects of the type this class contains (E).
   * @param c The class of the pool type (class of E)
   * @param parameterTypes An (optional) list of parameters to invoke the correct constructor
   * @throws PoolException If the type this class contains (E) doesn't have a
   *  constructor that matches the specified parameter list
   * @see #setArgumentList(Object...)
   */
  public Pool(Class<E> c, Class<?>... parameterTypes)
  {
    lock_ = new Object();
    
    class_ = c;
    parameterTypes_ = parameterTypes;

    try
    {
      constructor_ = class_.getDeclaredConstructor(parameterTypes);
      constructor_.setAccessible(true);
      
      if (parameterTypes.length == 0)
        argumentList_ = new Object[0];
    }
    catch (NoSuchMethodException e)
    {
      throw new PoolException("No constructor exists corresponding to the specified argument list!", e);
    }
  }
  
  
  /**
   * Sets the argument list to use when instantiating objects of the type this
   *  class contains (E). The length of the argument list and the argument types
   *  must match the parameter type list specified when creating this pool.
   * @param initargs The arguments list
   * @throws PoolException If the specified argument list doesn't match the
   *  parameter type list (either by length or by type)
   */
  public void setArgumentList(Object... initargs)
  {
    if (initargs.length != parameterTypes_.length)
      throw new PoolException("The argument list length doesn't match the parameter list length");
    
    for (int i = 0; i < initargs.length; i++)
    {
      if (!initargs[i].getClass().equals(parameterTypes_[i]))
        throw new PoolException("Parameters and argument list types doesn't match!");
    }
    
    argumentList_ = initargs;
  }
  
  
  /**
   * Creates and stores the specified amount of instances of the type this pool
   *  contains (E).
   * @param amount The amount of instances to allocate
   * @throws PoolException If the system failed to create instances of the
   *  type (E)
   */
  public void allocate(int amount)
  {
    synchronized (lock_)
    {
      for (int i = 0; i < amount; i++)
      {
        PoolNode<E> node = new PoolNode<E>();
        
        node.element = createInstance();
        
        node.next = firstObjectNode_;
        firstObjectNode_ = node;
      }
    }
  }
  
  
  /**
   * Acquires and returns an instance of the type this pool contains (E). If the
   *  pool is empty a new instance of E is created and returned.
   * @return An instance of the type this pool contains (E)
   * @throws PoolException If a new instance were to be created and the
   *  creation failed
   */
  public E acquire()
  {
    synchronized (lock_)
    {
      E result = null;
      
      if (firstObjectNode_ != null)
      {
        result = firstObjectNode_.element;
        
        PoolNode<E> node = firstObjectNode_;
        
        firstObjectNode_ = node.next;
        
        node.next    = firstStoredNode_;
        node.element = null;
        
        firstStoredNode_ = node;
      }
      else
      {
        result = createInstance();
      }
      
      balance_ += 1;
      
      return result;
    }
  }
  
  
  /**
   * Stores the specified element in the pool. The element shouldn't be referenced
   *  from the outside once it is in the pool.
   * @param element The element to store in the pool
   * @throws NullPointerException If the element passed is <code>null</code>
   */
  public void store(E element)
  {
    if (element == null)
      throw new NullPointerException("Can't store a null element!");
   
    synchronized (lock_)
    {
      PoolNode<E> node = null;
      
      if (firstStoredNode_ != null)
      {
        node = firstStoredNode_;
        
        firstStoredNode_ = node.next;
      }
      else
      {
        node = new PoolNode<E>();
      }
      
      node.element = element;
      node.next = firstObjectNode_;
      
      firstObjectNode_ = node;
      
      balance_ -= 1;
    }
  }
  
  
  /**
   * Returns the balance of this pool. The balance is defined as:
   *  <pre>acquriedAmount - storedAmount</pre>
   *  Meaning the amount of elements obtained by calling {@code acquire()}  
   *  minus the amount of elements put into the pool by calling {@code store()}.
   * @return The balance of this pool
   */
  public int getBalance()
  {
    synchronized (lock_)
    {
      return balance_;
    }
  }
  
  
  private E createInstance()
  {
    E result = null;
    
    try
    {
      result = constructor_.newInstance(argumentList_);
    }
    catch (InstantiationException e)
    {
      throw new PoolException("Couldn't instantiate class, it might be abstract or an interface", e);
    }
    catch (IllegalAccessException e)
    {
      throw new PoolException("Constructor isn't visible!", e);
    }
    catch (IllegalArgumentException e)
    {
      throw new PoolException("Parameters and argument list types doesn't match!", e);
    }
    catch (InvocationTargetException e)
    {
      throw new PoolException("Exception occurred when invoking constructor!", e);
    }
    
    return result;
  }
  
  
  private static class PoolNode<E extends Poolable>
  {
    public PoolNode<E> next;
    
    public E element;
  }
}
