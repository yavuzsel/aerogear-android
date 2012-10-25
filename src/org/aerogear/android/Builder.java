/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aerogear.android;

/**
 * Builders offer a different way to create objects which may have
 * complex construction rules.
 */
public interface Builder<T> {
    
    /**
     * 
     * @return a built instance of T
     */
    public T build();
    
}
