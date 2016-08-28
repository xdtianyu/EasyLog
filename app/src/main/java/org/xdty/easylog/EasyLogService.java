package org.xdty.easylog;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
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

    private static final String TAG = EasyLogService.class.getSimpleName();

    private View mWindow;
    private TextView mTextView;
    private WindowManager mWindowManager;
    private Setting mSetting;

    private Handler mMainHandler;

    private IBinder mBinder = new ILogReceiver.Stub() {

        @Override
        public void log(final String line) throws RemoteException {
            Log.e(TAG, line);
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.append("\n" + line);
                }
            });
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mSetting = new SettingImpl(this);

        mMainHandler = new Handler(Looper.getMainLooper());

        mWindow = View.inflate(this, R.layout.window, null);
        mTextView = (TextView) mWindow.findViewById(R.id.text);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (mSetting.isWindowEnabled()) {
            int alpha = mSetting.getWindowAlpha();
            mTextView.setTextColor(Color.argb(alpha, 255, 0, 0));
            mWindowManager.addView(mWindow, params);
        }

    }

    @Override
    public void onDestroy() {
        if (mSetting.isWindowEnabled()) {
            mWindowManager.removeView(mWindow);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
