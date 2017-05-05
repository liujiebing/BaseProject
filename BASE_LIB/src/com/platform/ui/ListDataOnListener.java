package com.platform.ui;

import com.droideek.entry.data.Entry;

import java.util.List;

/**
 * Created by Droideek on 2016/1/6.
 */
public interface ListDataOnListener {
    public <E extends Entry> void onSetListData(List<E> list);
}
