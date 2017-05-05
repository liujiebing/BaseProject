package com.droideek.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Droideek on 2016/3/8.
 */
public abstract class XListAdapter<T> extends BaseAdapter{

    protected List<T> mList;// 列表List
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnCustomListener listener;

    /**
     * 构造器
     *
     * @param list
     *            起始数据
     */
    protected XListAdapter(Context context, List<T> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    protected XListAdapter(Context context) {
        mList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setListener(OnCustomListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 获取该适配器的列表数据
     *
     * @return
     */
    public List<T> getData() {
        return mList;
    }

    public void add(T bean) {
        if (bean != null) {
            mList.add(bean);
        }
    }

    public void clean() {
        mList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<T> list){
        if (null == mList) {
            return;
        }
        mList.addAll(list);
    }

}
