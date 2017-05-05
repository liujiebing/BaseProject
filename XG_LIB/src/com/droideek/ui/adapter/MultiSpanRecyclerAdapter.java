package com.droideek.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Droideek on 2016/12/30.
 */

public abstract class MultiSpanRecyclerAdapter<T> extends BasicRecyclerAdapter<T> {

    public MultiSpanRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getColumns(position);
                }
            });
        }
    }

    protected abstract int getColumns(int position);
}
