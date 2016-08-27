package org.xdty.easylog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class LogListener implements LogReader.LogListener {

    private final Intent intent = new Intent().setComponent(new ComponentName(
            "org.xdty.easylog",
            "org.xdty.easylog.EasyLogService"));

    private ILogReceiver mLogReceiver;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLogReceiver = ILogReceiver.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLogReceiver = null;
        }
    };

    public void bind(Context context) {
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onReadLine(String line) {
        if (mLogReceiver != null) {
            try {
                mLogReceiver.log(line);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear(Context context) {

        context.unbindService(mConnection);
        mConnection = null;
    }
}