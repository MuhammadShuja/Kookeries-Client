package com.kookeries.shop.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class GenericListAdapter<T> extends BaseAdapter {

    private List<T> tList;

    public GenericListAdapter(List<T> items) {
        this.tList = items;
    }

    @Override
    public int getCount() {
        if (tList == null)
            return 0;

        return tList.size();
    }

    @Override
    public Object getItem(int i) {
        return tList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getView(i, view, viewGroup, tList.get(i));
    }

    public abstract View getView(int position, View view, ViewGroup viewGroup, T t);

    public void setData(List<T> tList) {
        this.tList = tList;
        notifyDataSetChanged();
    }
}