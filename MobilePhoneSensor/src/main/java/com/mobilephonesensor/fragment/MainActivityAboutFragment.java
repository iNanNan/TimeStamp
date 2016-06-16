package com.mobilephonesensor.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.inject.InjectView;
import com.base.presenter.Presenter;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;
import com.mobilephonesensor.util.permission.PermissionsManager;
import com.mobilephonesensor.util.permission.PermissionsResultAction;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by heng on 16-3-17.
 */
public class MainActivityAboutFragment extends SupperFragment {

    private static final String TAG = MainActivityAboutFragment.class.getSimpleName();

    private static final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/test.txt";

    @InjectView(R.id.frag_about_text)
    TextView textView;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main_about;
    }

    @Override
    protected void onPresenterComplete(Presenter presenter) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] per = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, per, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                writeToStorage("WRITE_EXTERNAL_STORAGE SUCCESS");
                readFromStorage();
            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * Requires Permission: Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    private void writeToStorage(String text) {
        File file = new File(PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Unable to write to storage", e);
        } finally {
            close(outputStream);
        }
    }

    /**
     * Requires Permission: Manifest.permission.READ_EXTERNAL_STORAGE
     */
    private void readFromStorage() {
        File file = new File(PATH);
        BufferedReader inputStream = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            inputStream = new BufferedReader(new InputStreamReader(input));
            String test = inputStream.readLine();
            this.textView.setText(String.format(Locale.getDefault(), getString(R.string.read_rex), test));
        } catch (IOException e) {
            Log.e(TAG, "Unable to read from storage", e);
        } finally {
            close(input);
            close(inputStream);
        }
    }

    private static void close(@Nullable Closeable closeable) {
        if (closeable == null) {return;}
        try {
            closeable.close();
        } catch (IOException ignored) {}
    }
}
