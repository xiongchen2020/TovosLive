package com.example.myapplication.ui.recycleadapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hition on 2016/12/9.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    abstract public void setData(T data);

    protected <V extends View> V $(@IdRes int id) {
        return (V) itemView.findViewById(id);
    }

    protected Context getContext(){
        return itemView.getContext();
    }
}
