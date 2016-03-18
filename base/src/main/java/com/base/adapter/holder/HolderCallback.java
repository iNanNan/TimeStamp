package com.base.adapter.holder;

import android.view.View;

import com.base.adapter.item.AdapterItem;


/**
 * Created by A Shuai on 2015/8/14.
 */
public interface HolderCallback<DATA> {

    void setItem(AdapterItem<DATA> itemData);

    AdapterItem<DATA> getItemData();

    View getHolderRootView();

    void onBound();

    void onUpdate();

    void onRemoved();

    void onChanged();

    void onDestroy();
}
