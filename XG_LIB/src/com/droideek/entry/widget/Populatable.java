/**
 * 
 */
package com.droideek.entry.widget;

/**
 * @author kituri
 *
 * This interface must be implemented by all classes that wish to support populating. 
 */
public interface Populatable<E> {
	
	void populate(E data);
}
