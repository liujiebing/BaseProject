/**
 * 
 */
package com.droideek.entry.widget;

import com.droideek.entry.data.Entry;

/**
 * @author Kituri
 *
 */
public interface Selectable<E> {

	public void setSelectionListener(SelectionListener<Entry> mListener);
}
