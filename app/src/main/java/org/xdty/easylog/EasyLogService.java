package org.xdty.easylog;

import android.app.Service;
import android.content.Intent;
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
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;

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

        mMainHandler = new Handler(Looper.getMainLooper());

        mWindow = View.inflate(this, R.layout.window, null);
        mTextView = (TextView) mWindow.findViewById(R.id.text);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.CENTER;
        mParams.setTitle("Window test");

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mWindow, mParams);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mWindow);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
