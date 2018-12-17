package com.hojong.meokgol.adapter;

import android.widget.BaseAdapter;

public abstract class MyListAdapter extends BaseAdapter
{
    public abstract void clear();
    public abstract void addItem(Object i);
}
