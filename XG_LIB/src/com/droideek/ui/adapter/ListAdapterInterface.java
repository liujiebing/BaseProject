package com.droideek.ui.adapter;

import java.util.List;

/**
 * Created by Droideek on 2016/11/3.
 */

public interface ListAdapterInterface<T> {

    List<T> getData();

    void add(T bean);

    void clearNotify();

    void clear();

    void addAll(List<T> list);

    void addAllNotify(List<T> list);

    void addSingle(T t);

    void notifyDataSetChanged();
}
