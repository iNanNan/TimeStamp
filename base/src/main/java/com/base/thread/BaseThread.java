package com.base.thread;

import android.os.AsyncTask;

/**
 * Created by heng on 16-3-21.
 */
public abstract class BaseThread extends AsyncTask {

    private double total;

    private double current;

    private boolean isProgress;

    private int getScale() {
        return (int) (current / total);
    }

    public void setIsProgress(boolean isProgress) {
        this.isProgress = isProgress;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    private void calculateTotal() {

    }

    private void calculateCurrent() {

    }
}
