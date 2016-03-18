package com.base.adapter.holder;

import android.view.View;
import com.base.adapter.item.AdapterItem;


/**
 * Created by A Shuai on 2015/8/12.
 */
public abstract class XNHolder<DATA> implements HolderCallback<DATA> {
    private AdapterItem<DATA> itemData;
    private final View itemView;
    public int viewType = 0;
    public int mPosition = -1;
    public XNHolder(View itemView, int viewType) {
        if (itemView != null) {
            this.itemView = itemView;
            this.viewType = viewType;
            bindView();
        } else {
            throw new IllegalArgumentException("itemView is null");
        }
    }

    protected <T extends View> T find (int resId) {
        return (T)(itemView.findViewById(resId));
    }

    protected boolean isChangedCurrentEntity(AdapterItem<DATA> newItem) {
        return newItem != itemData;
    }

    protected abstract void bindView();
    protected abstract void bound();
    protected abstract void update();
    protected abstract void removed();
    protected abstract void changed();
    protected abstract void destroy();

    @Override
    public void setItem(AdapterItem<DATA> itemData) {
        if (itemData == null)
            return;
        if (!isChangedCurrentEntity(itemData)) {
            update();
            return;
        }
        this.itemData = itemData;
        bound();
    }

    @Override
    public AdapterItem<DATA> getItemData() {
        return itemData;
    }

    @Override
    public View getHolderRootView() {
        return itemView;
    }

    @Override
    public void onBound() {
        bound();
    }

    @Override
    public void onUpdate() {
        update();
    }

    @Override
    public void onRemoved() {
        removed();
    }

    @Override
    public void onChanged() {
        changed();
    }

    @Override
    public void onDestroy() {
        destroy();
    }
}
