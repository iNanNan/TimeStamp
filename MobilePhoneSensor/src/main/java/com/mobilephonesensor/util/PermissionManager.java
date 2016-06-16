package com.mobilephonesensor.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.List;

/**
 * Created by heng on 16-5-6.
 */
public final class PermissionManager {

    private volatile static PermissionManager mInstance;

    private final static Object LOCK = new Object();

    public static PermissionManager get() {
        synchronized (LOCK) {
            if (mInstance == null) {
                mInstance = new PermissionManager();
            }
            return mInstance;
        }
    }

    public final static int REQUEST_CODE = 1;

    public void requestPermission(Object context, String[] permissions) {
        if (context == null) {
            return;
        }
        if (permissions == null || permissions.length == 0) {
            return;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isRequest(activity, permissions)) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
            }
        } else if (context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            if (isRequest(fragment.getActivity(), permissions)) {
                getParent(fragment).requestPermissions(permissions, REQUEST_CODE);
            }
        }
    }

    public void onRequestPermission(Fragment root, int requestCode, String[] permissions, int[] grantResults) {
        List<Fragment> fragments = root.getChildFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    onRequestPermission(fragment, requestCode, permissions, grantResults);
                }
            }
        }
    }

    private boolean isRequest(Context context, String[] permissions) {
        return ContextCompat.checkSelfPermission(context, permissions[0])
                != PackageManager.PERMISSION_GRANTED;
    }

    private Fragment getParent(Fragment fragment) {
        Fragment result = fragment.getParentFragment();

        if (result == null) {
            return fragment;
        }

        while (result.getParentFragment() != null) {
            result = result.getParentFragment();
        }

        return result;
    }

}
