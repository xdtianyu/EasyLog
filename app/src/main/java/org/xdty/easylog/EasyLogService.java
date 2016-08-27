package org.xdty.easylog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class EasyLogService extends Service {

    private static final String TAG = EasyLogService.class.getSimpleName();

    private IBinder mBinder = new ILogReceiver.Stub() {

        @Override
        public void log(String line) throws RemoteException {
            Log.e(TAG, line);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
