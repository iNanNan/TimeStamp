package com.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.base.adapter.holder.HolderCallback;
import com.base.adapter.holder.XNHolder;
import com.base.adapter.item.AdapterItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by A Shuai on 2015/8/12.
 */
public abstract class XNAdapter<VH extends XNHolder, DATA> extends BaseAdapter {

    private ArrayList<AdapterItem<DATA>> data = new ArrayList<>();
    //键为view的类型，值为view的资源id
    private HashMap<Integer, Integer> viewType = new HashMap<>();
    private SparseArray<HolderCallback<DATA>> holderBack = new SparseArray<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public XNAdapter(Context context) {
        super();
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        onAddViewType();
    }

    public Context getContext() {
        return mContext;
    }

    public ArrayList<AdapterItem<DATA>> getAdapterData() {
        return data;
    }

    public void addItem(DATA itemData) {
        if (data == null)
            data = new ArrayList<>();
        data.add(data.size(), newItem(itemData));
    }

    public void addItem(DATA itemData, int viewType) {
        if (data == null)
            data = new ArrayList<>();
        data.add(data.size(), newItem(itemData, viewType));
    }

    public void addData(List<DATA> dataList) {
        if (dataList == null || dataList.size() == 0)
            return;
        if (data == null)
            data = new ArrayList<>();
        for (DATA d : dataList) {
            data.add(newItem(d));
        }
    }

    public void removeItem(int position) {
        if (data == null || data.size()==0)
            return;
        data.remove(position);
    }

    public void clearData() {
        if (data == null)
            return;
        data.clear();
    }

    private AdapterItem<DATA> newItem(DATA data) {
        if (data == null)
            return null;
        return new AdapterItem<>(data);
    }

    private AdapterItem<DATA> newItem(DATA data, int viewType) {
        if (data == null)
            return null;
        return new AdapterItem<>(data, viewType);
    }

    /**
     * 添加布局资源
     * @param type 类型从0开始
     * @param resId 不同类型对应的资源id
     */
    protected void addViewType(int type, int resId) {
        if (viewType == null)
            viewType = new HashMap<>();
        viewType.put(type,resId);
    }

    protected abstract VH onCreateViewHolder(View convertView, int viewType);
    protected abstract void onBindViewHolder(VH viewHolder, int position);
    protected abstract void onAddViewType();

    private final VH createViewHolder(View convertView, int viewType) {
        VH holder = this.onCreateViewHolder(convertView, viewType);
        return holder;
    }

    private final void bindViewHolder(VH holder, int position) {
        holder.mPosition = position;
        this.onBindViewHolder(holder,position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public AdapterItem<DATA> getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (data == null || data.size() == 0) {
            return null;
        }
        VH holder;
        AdapterItem<DATA> item = data.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(getLayoutIdByViewType(position), null);
            holder = createViewHolder(convertView, item.getViewType());
        } else {
            holder = (VH) convertView.getTag();
        }
        convertView.setTag(holder);
        holder.setItem(item);
        bindViewHolder(holder, position);
        holderBack.put(position, holder);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return viewType.size();
    }

    private int getLayoutIdByViewType(int position) {
        return viewType.get(getItemViewType(position));
    }

    public void destroy() {
        for (int i=0;i<holderBack.size();i++) {
            holderBack.get(i).onDestroy();
        }
        viewType.clear();
        holderBack.clear();
        data.clear();
        System.gc();
    }
}
