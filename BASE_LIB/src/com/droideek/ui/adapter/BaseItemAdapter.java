package com.droideek.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.droideek.entry.data.Entry;
import com.droideek.entry.widget.SelectionListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;


public abstract class BaseItemAdapter<T> extends BaseAdapter implements ListAdapterInterface<T> {
	protected List<T> mList;// 列表List
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected SelectionListener<Entry> mCallbackListener;

	public void setSelectionListener(SelectionListener<Entry> listener) {
		WeakReference<SelectionListener<Entry>> mListener = new WeakReference<SelectionListener<Entry>>(listener);
		mCallbackListener = mListener.get();
	}

	public BaseItemAdapter(Context context) {
		this(new LinkedList<T>(), context);
	}


	public BaseItemAdapter(List<T> list, Context context) {
		mList = list;
		mContext = context;
		mInflater = LayoutInflater.from(context);
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
		if (null == mList) {
			return;
		}
		if (bean != null) {
			mList.add(bean);
		}
	}

	public void clearNotify() {
		if (null == mList) return;
		clear();
		this.notifyDataSetChanged();
	}

	public void clear() {
		if (null == mList) {
			return;
		}
		mList.clear();
	}

	public void addAll(List<T> list){

		try {
			if (null == mList) {
                return;
            }
			mList.addAll(list);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public void addAllNotify(List<T> list) {
		addAll(list);
		notifyDataSetChanged();
	}

	public void addSingle(T t) {
		if(null == mList) return;

		if(!mList.contains(t)) {
			mList.add(t);
		}
	}

}
