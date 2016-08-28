package org.xdty.easylog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LogReader {
    private static final String TAG = "LogReader";

    private Handler mHandler;

    private Handler mMainHandler;

    private Process mProcess;

    private List<LogListener> mLogListeners;

    private List<String> mTagList;

    private LogReader() {
        mMainHandler = new Handler(Looper.getMainLooper());
        HandlerThread handlerThread = new HandlerThread(LogReader.class.getCanonicalName());
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    public static LogReader getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void start() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new LogRunnable());
    }

    public void stop() {
        if (mProcess != null) {
            mProcess.destroy();
        }
    }

    public void restart() {
        stop();
        start();
    }

    public void addListener(LogListener logListener) {
        if (mLogListeners == null) {
            mLogListeners = Collections.synchronizedList(new ArrayList<LogListener>());
        }
        mLogListeners.add(logListener);
    }

    public void removeListener(LogListener logListener) {
        if (mLogListeners != null) {
            int i = mLogListeners.indexOf(logListener);
            if (i >= 0) {
                mLogListeners.remove(i);
            }
        }
    }

    public void addTag(String tag) {
        if (mTagList == null) {
            mTagList = Collections.synchronizedList(new ArrayList<String>());
        }
        mTagList.add(tag);
        restart();
    }

    public void removeTag(String tag) {
        if (mTagList != null) {
            int i = mTagList.indexOf(tag);
            if (i >= 0) {
                mTagList.remove(i);
            }
        }
        restart();
    }

    private String buildCommand() {
        return "logcat -v time " + tags();
    }

    private String tags() {
        if (mTagList == null || mTagList.size() == 0) {
            return "";
        } else {
            return "-s " + TextUtils.join(",", mTagList);
        }
    }

    private void handleLog(final String line) {
        if (mLogListeners != null && mLogListeners.size() > 0) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    final List<LogListener> listeners = mLogListeners;
                    int size = listeners.size();
                    for (int i = 0; i < size; i++) {
                        listeners.get(i).onReadLine(line);
                    }
                }
            });
        }
    }

    public interface LogListener {
        void onReadLine(String line);
    }

    private static class SingletonHelper {
        private final static LogReader INSTANCE = new LogReader();
    }

    private class LogRunnable implements Runnable {

        @Override
        public void run() {
            try {
                String command = buildCommand();

                mProcess = Runtime.getRuntime().exec(command);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(mProcess.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    handleLog(line);
                }
            } catch (IOException ex) {
                // do noting
            }
        }
    }

}