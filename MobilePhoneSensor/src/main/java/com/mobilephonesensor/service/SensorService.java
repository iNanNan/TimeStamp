package com.mobilephonesensor.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.PowerManager;

import com.mobilephonesensor.base.SupperService;

/**
 * Created by heng on 16-3-22.
 */
public class SensorService extends SupperService {

    private PowerManager mPowerManager;

    private NotificationManager mNotifiManager;

    private Notification.Builder mBuilder;

    private Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initOverAllValues() {
        mNotifiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(this);
        mNotification = mBuilder.build();
    }

    private void sensorDistance() {

    }
}
