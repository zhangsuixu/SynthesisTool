package com.ui.widget.rvadapter;

import android.content.Context;
import android.view.LayoutInflater;

import com.common.tools.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的通用adapter
 */
@SuppressWarnings("all")
public abstract class RvCommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public RvCommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                RvCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    public void setNewDatas(List<T> dataList) {
        checkNotNullOrEmpty();
        mDatas.clear();
        if (CommonUtils.isNotNullOrEmpty(dataList)) {
            mDatas.addAll(dataList);
        }
        notifyDataSetChanged();

    }

    public void addDatas(List<T> dataList) {
        checkNotNullOrEmpty();
        if (CommonUtils.isNotNullOrEmpty(dataList)) {
            mDatas.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    private void checkNotNullOrEmpty() {
        if (CommonUtils.isNullOrEmpty(mDatas)) {
            mDatas = new ArrayList<>();
        }
    }

    public List<T> getData() {
        return mDatas;
    }

}
