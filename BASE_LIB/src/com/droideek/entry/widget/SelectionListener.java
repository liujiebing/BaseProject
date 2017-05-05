/**
 * 
 */
package com.droideek.entry.widget;

import android.content.Intent;

/**
 * @author Kituri
 *
 */
public interface SelectionListener<E> {
	
	void onSelectionChanged (E item, boolean selected, Intent intent);
}
