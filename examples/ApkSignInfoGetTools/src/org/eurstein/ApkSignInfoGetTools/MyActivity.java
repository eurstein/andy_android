package org.eurstein.ApkSignInfoGetTools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import org.eurstein.utils.AndyLog;

import java.io.File;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.button).setOnClickListener(onClickListener);
        findViewById(R.id.button2).setOnClickListener(onClickListener);
        TextView textView = ((TextView) findViewById(R.id.textView2));
        textView.setMovementMethod(
                ScrollingMovementMethod.getInstance());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideEditTextIM();
        return super.onTouchEvent(event);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                final int vid = v.getId();
                new Thread() {
                    public void run() {
                        switch (vid) {
                            case R.id.button:
                                showUninstalledApkSignatureInfo();
                                break;
                            case R.id.button2:
                                showInstalledApkSignatureInfo();
                                break;
                            default:
                                break;
                        }
                    }
                }.start();
            } catch (Exception ignore) {
            }
        }
    };

    private void showUninstalledApkSignatureInfo() {
        SignatureGet.YYBMD5 = "";
        hideEditTextIM();
        showResult("waiting ...");
        String apkPath = "/sdcard/1.apk";
        if (new File(apkPath).exists()) {
            String result = SignatureGet
                    .getUninstallApkSigntureInfo(getApplicationContext(), apkPath);
            showResult("[md5 in yyb]: " + SignatureGet.YYBMD5 + result);
            showResult(apkPath + ":\n" + "[md5 in yyb]: " + SignatureGet.YYBMD5 + "\n\n" + result);
        } else {
            showResult(apkPath + " not exists");
        }
    }

    private void showInstalledApkSignatureInfo() {
        SignatureGet.YYBMD5 = "";
        hideEditTextIM();
        String packageName = ((EditText) findViewById(R.id.editText)).getText().toString();
        String result;
        if (!TextUtils.isEmpty(packageName)) {
            result = SignatureGet.getInstalledApkSignInfo(getApplicationContext(), packageName);
        } else {
            result = "please give me a package name";
        }
        showResult(packageName + ":\n" + "[md5 in yyb]: " + SignatureGet.YYBMD5 + "\n\n" + result);
    }

    private void hideEditTextIM() {
        // try to hide input_method:
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = findViewById(R.id.editText).getWindowToken();
            if (windowToken != null) {
                // always de-activate IM
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {
            AndyLog.e("HideInputMethod", "failed:" + e.getMessage());
        }
    }

    private void showResult(final String signInfo) {
        AndyLog.d(getResources().getString(R.string.app_name), signInfo);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                TextView textView = ((TextView) findViewById(R.id.textView2));
                textView.setText(signInfo);
                textView.scrollTo(0, 0);
            }
        });
    }
}
