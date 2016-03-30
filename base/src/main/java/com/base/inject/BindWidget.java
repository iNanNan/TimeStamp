package com.base.inject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.base.util.StringUtil;

import java.lang.reflect.Field;

/**
 * Created by heng on 16-3-30.
 */
final public class BindWidget {

    public static <V extends View> void init(Object o) {
        try {
            Class<?> clazz = o.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                InjectView iv = field.getAnnotation(InjectView.class);
                if (iv != null) {
                    V v = null;
                    if (o instanceof Activity) {
                        Activity act = (Activity) o;
                        v = (V) (act.findViewById(iv.value()));
                    } else if (o instanceof Fragment) {
                        Fragment fgt = (Fragment) o;
                        View fContent = fgt.getView();
                        if (fContent != null) {
                            v = (V) (fContent.findViewById(iv.value()));
                        }
                    }

                    if (v != null) {
                        if (StringUtil.isNotEmpty(iv.tag())) {
                            v.setTag(iv.tag());
                        }
                        field.setAccessible(true);
                        field.set(o, v);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
