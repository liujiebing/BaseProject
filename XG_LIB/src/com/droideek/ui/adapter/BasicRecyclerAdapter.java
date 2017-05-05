package com.droideek.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.droideek.entry.data.Entry;
import com.droideek.entry.widget.SelectionListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final String TAG = getClass().getSimpleName();
    protected final Context mContext;
    protected final LayoutInflater mLayoutInflater;
    protected SelectionListener mListener;

    protected List<T> mDataList = new ArrayList<>();

    public BasicRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setSelectionListener(SelectionListener<Entry> mListener) {
        this.mListener = mListener;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public T getItemData(int position) {
        if(null == mDataList) return null;
        return position < mDataList.size() ? mDataList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 移除某一条记录
     *
     * @param position 移除数据的position
     */
    public void removeItem(int position) {
        if(null == mDataList) return;
        if (position < mDataList.size()) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 添加一条记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItem(T data, int position) {
        if(null == mDataList) return;
        if (position <= mDataList.size()) {
            mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 添加一条记录
     *
     * @param data 需要加入的数据结构
     */
    public void addItem(T data) {
        if(null == mDataList) return;
        addItem(data, mDataList.size());
    }

    /**
     * 移除所有记录
     */
    public void clearItems() {
        if(null == mDataList) return;
        int size = mDataList.size();
        if (size > 0) {
            mDataList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * 批量添加记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItems(List<T> data, int position) {
        if(null == mDataList) return;
        if (position <= mDataList.size() && data != null && data.size() > 0) {
            mDataList.addAll(position, data);
            notifyItemRangeChanged(position, data.size());
        }
    }

    /**
     * 批量添加记录
     *
     * @param data 需要加入的数据结构
     */
    public void addItems(List<T> data) {
        if(null == mDataList) return;
        addItems(data, mDataList.size());
    }

    /**
     * 清空并增加
     * @param data
     */
    public void putItemsAndNotify(List<T> data){
        if(null == mDataList) return;
        mDataList.clear();
        addItems(data);
    }
}
