package com.droideek.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.droideek.entry.data.Entry;
import com.droideek.entry.widget.SelectionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qibin on 2015/11/5.
 */
public abstract class HeaderRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapterInterface<T>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    protected List<T> mList;

    private View mHeaderView;

    public HeaderRecyclerAdapter(List<T> list) {
        this.mList = list;
    }

    public HeaderRecyclerAdapter() {
        mList = new ArrayList<>();
    }

    protected SelectionListener<Entry> mCallbackListener;

    public void setSelectionListener(SelectionListener<Entry> listener) {
        mCallbackListener = listener;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        //notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(viewHolder);
        final T data = mList.get(pos);
        onBind(viewHolder, pos, data);
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
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);
    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data);

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public void addAll(List<T> list) {
        if (null != mList) {
            mList.addAll(list);
        }
    }

    @Override
    public List<T> getData() {
        return mList;
    }

    public void add(T Ads) {
        if (null != mList) {
            mList.add(Ads);
        }
    }

    @Override
    public void clearNotify() {
        clear();
        notifyDataSetChanged();
    }

    public void clear() {
        if (null != mList) {
            mList.clear();
        }
    }


    @Override
    public void addAllNotify(List<T> list) {
        addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void addSingle(T t) {
        if(null != mList && !mList.contains(t)) {
            mList.add(t);
        }
    }

    public T getItem(int position) {

        if (null == mList || mList.size() <= 0) return null;

        return mList.get(position);
    }

    public void notifyDataOnly() {
        notifyItemRangeChanged(mHeaderView ==null ? 0 : 1, mList.size());
    }

}
