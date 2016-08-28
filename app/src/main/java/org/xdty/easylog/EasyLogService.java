package org.xdty.easylog;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class EasyLogService extends Service {

    public final static String WINDOW_TRANS = "window_trans";
    public final static String ENABLE_WINDOW = "enable_window";
    private static final String TAG = EasyLogService.class.getSimpleName();

    private View mWindow;
    private TextView mTextView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private Setting mSetting;
    private Handler mMainHandler;

    private boolean isWindowShowing = false;

    private LogPool mLogPool;

    private IBinder mBinder = new ILogReceiver.Stub() {

        @Override
        public void log(final LogLine line) throws RemoteException {
            Log.e(TAG, line.content);
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    handleLog(line);
                }
            });
        }
    };

    private void handleLog(LogLine line) {
        mLogPool.append(line);
        mTextView.setText(mLogPool.cache());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int alpha = bundle.getInt(WINDOW_TRANS);
            if (alpha != 0) {
                mWindow.setAlpha(alpha / 100f);
            }

            if (mSetting.isWindowEnabled()) {
                enableWindow();
            } else {
                disableWindow();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSetting = new SettingImpl(this);

        mMainHandler = new Handler(Looper.getMainLooper());

        mLogPool = new LogPool(50);

        mWindow = View.inflate(this, R.layout.window, null);
        mTextView = (TextView) mWindow.findViewById(R.id.text);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.CENTER;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        enableWindow();

    }

    private void enableWindow() {
        if (!isWindowShowing) {
            int alpha = mSetting.getWindowAlpha();
            mWindow.setAlpha(alpha / 100f);
            mWindowManager.addView(mWindow, mParams);
            isWindowShowing = true;
        }
    }

    private void disableWindow() {
        if (isWindowShowing) {
            mWindowManager.removeView(mWindow);
            isWindowShowing = false;
        }
    }

    @Override
    public void onDestroy() {
        disableWindow();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
