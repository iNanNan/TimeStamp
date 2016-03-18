package com.base.adapter.item;

import java.io.Serializable;

/**
 * Created by A Shuai on 2015/8/13.
 */
public class AdapterItem<DATA> implements Serializable {
    private static final long serialVersionUID = 1L;
    private DATA data;
    private int viewType = 0;

    public AdapterItem(DATA data) {
        if (data == null)
            throw new IllegalArgumentException("data is null");
        this.data = data;
    }

    public AdapterItem(DATA data, int type){
        if (data == null)
            throw new IllegalArgumentException("data is null");
        this.data = data;
        this.viewType = type;
    }

    public void setData(DATA data) {
        this.data = data;
    }

    public DATA getData() {
        return data;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
