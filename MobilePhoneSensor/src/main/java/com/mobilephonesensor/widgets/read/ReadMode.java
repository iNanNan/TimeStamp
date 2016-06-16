package com.mobilephonesensor.widgets.read;

/**
 * Created by heng on 16-5-24.
 */
public enum ReadMode {
    REAL,

    SLIDE_VERTICAL,

    SLIDE_HORIZONTAL;

    protected static ReadMode getDefault() {
        return ReadMode.REAL;
    }
}
