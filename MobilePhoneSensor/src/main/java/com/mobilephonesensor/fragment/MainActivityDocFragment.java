package com.mobilephonesensor.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.inject.InjectView;
import com.base.presenter.Presenter;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;

/**
 * Created by heng on 16-3-17.
 */
public class MainActivityDocFragment extends SupperFragment {

    @InjectView(R.id.doc_layout)
    LinearLayout mLayout;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main_doc;
    }

    @Override
    protected void onPresenterComplete(Presenter presenter) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView test = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160, 160);
        params.setMargins(0, 20, 0, 0);
        test.setLayoutParams(params);
        test.setScaleType(ImageView.ScaleType.CENTER_CROP);
        test.setImageResource(R.mipmap.themebg_d_2);
        mLayout.addView(test);
        mLayout.addView(new SampleView(getActivity()));
    }

    private static class SampleView extends View {

        Paint paint;

        public SampleView(Context context) {
            super(context);
            setFocusable(true);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
        }


        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            canvas.translate(50,50);
            RadialGradient gradient = new RadialGradient(200, 200, 30, new int[]{Color.TRANSPARENT, 0xff669900, Color.TRANSPARENT}
                    , new float[]{0.6f, 0.7f, 1f}, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            canvas.drawCircle(200, 200, 30, paint);
        }
    }
}

