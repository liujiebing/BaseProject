package com.droideek.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Droideek on 2016/3/11.
 */
public abstract class LinkedListAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;

    /**
     * 数据列表
     */
    private LinkedList<T> mListItems;

    public LinkedListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListItems = new LinkedList<T>();
    }

    /**
     * 清理数据域
     */
    public void clear() {
        if (mListItems != null) {
            mListItems.clear();
        }
    }

    /**
     * 为数据域添加多个数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        if (data != null) {
            mListItems.addAll(data);
        }
    }

    /**
     * 为数据域添加多个数据 并且去重
     *
     * @param list
     */
    public void setDataForEquals(List<T> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                if (!mListItems.contains(t)) {
                    mListItems.add(t);
                }
            }
        }
    }

    /**
     * 添加数据到头部 并且去重
     *
     * @param list
     */
    public void setDataFirstForEquals(List<T> list) {
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                T t = list.get(i);
                if (!mListItems.contains(t)) {
                    mListItems.addFirst(t);
                }
            }
        }
    }

    /**
     * 添加数据到尾部 并且去重
     *
     * @param list
     */
    public void setDataLastForEquals(List<T> list) {
        if (list != null) {
            for (T t : list) {
                if (!mListItems.contains(t)) {
                    mListItems.addLast(t);
                }
            }
        }
    }

    /**
     * 添加数据到尾部 并且去重
     */
    public void setDataFirstForEquals(T data) {
        if (data != null) {
            if (mListItems.contains(data)) {
                mListItems.remove(data);
            }
            mListItems.addFirst(data);
        }
    }

    /**
     * 添加数据到尾部 并且去重
     */
    public void setDataLastForEquals(T data) {
        if (data != null) {
            if (mListItems.contains(data)) {
                mListItems.remove(data);
            }
            mListItems.addLast(data);
        }
    }

    /**
     * 设置多个数据到数据域的头部
     *
     * @param data
     */
    public void setDataFirst(List<T> data) {
        if (data != null) {
            mListItems.addAll(0, data);
        }
    }

    /**
     * 设置多个数据到数据域的末尾
     *
     * @param data
     */
    public void setDataLast(List<T> data) {
        if (data != null) {
            mListItems.addAll(getCount(), data);
        }
    }

    /**
     * 保留data 其他全移除
     *
     * @param data
     */
    public void retainAll(List<T> data) {
        if (data != null) {
            mListItems.retainAll(data);
        }
    }

    /**
     * 移除data数据组
     *
     * @param data
     */
    public void removeAll(List<T> data) {
        if (data != null) {
            mListItems.removeAll(data);
        }
    }

    /**
     * 将数据添加到数据域头部
     *
     * @param data
     */
    public void setDataFirst(T data) {
        if (data != null) {
            mListItems.addFirst(data);
        }
    }

    /**
     * 将数据添加到数据域的末尾
     *
     * @param data
     */
    public void setDataLast(T data) {
        if (data != null) {
            mListItems.addLast(data);
        }
    }

    /**
     * 数据将按顺序添加到数据域中
     *
     * @param data
     */
    public void setData(T data) {
        if (data != null) {
            mListItems.add(data);
        }
    }

    /**
     * 增加指定的数据到指定位置
     *
     * @param data
     * @param position
     */
    public void setData(T data, int position) {
        if (data != null) {
            mListItems.add(position, data);
        }
    }

    /**
     * 增加指定的集合数据到指定位置
     *
     * @param data
     * @param position
     */
    public void setData(List<T> data, int position) {
        if (data != null) {
            mListItems.addAll(position, data);
        }
    }

    /**
     * 移除指定position的数据，返回被移除的对像。
     *
     * @param position
     * @return
     */
    public T removeData(int position) {
        if (mListItems != null) {
            try {
                return mListItems.remove(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 移除指定T的数据
     */
    public void removeData(T t) {
        if (mListItems != null) {
            mListItems.remove(t);
        }
    }

    /**
     * 删除指定区间内的数据
     *
     * @param removeStart
     * @param removeEnd
     */
    public void removeData(int removeStart, int removeEnd) {
        if (!isEmpty()) {
            if (removeStart >= 0 && (removeEnd < getCount()) && (removeStart < removeEnd)) {
                List<T> tmp = new ArrayList<T>();
                while (++removeStart < removeEnd) {// 获取要remove的对像。
                    tmp.add(mListItems.get(removeStart));
                }

                for (T t : tmp) {// remove
                    mListItems.remove(t);
                }
                tmp = null;
            }
        }
    }

    /**
     * 移除数据域的第一条数据
     *
     * @return
     */
    public T removeFirst() {
        T result = null;
        if (mListItems != null) {
            result = mListItems.removeFirst();
        }
        return result;
    }

    /**
     * 移除数据域的最后一条数据
     */
    public T removeLast() {
        if (mListItems != null) {
            return mListItems.removeLast();
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mListItems != null)
            return mListItems.size();
        return 0;
    }

    /**
     * 得到指定Position的数据类型
     */
    @Override
    public T getItem(int position) {
        if (isEmpty() || position > mListItems.size() - 1) {
            return null;
        }
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getList() {
        return mListItems;
    }

}
