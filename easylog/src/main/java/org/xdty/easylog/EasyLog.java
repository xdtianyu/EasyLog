package org.xdty.easylog;

import android.content.Context;

public class EasyLog {

    private static LogReader sLogReader = LogReader.getInstance();
    private static LogListener sLogListener;

    public static void start(Context context) {
        sLogReader.restart();
        sLogListener = new LogListener();
        sLogReader.addListener(sLogListener);
        sLogListener.bind(context);
    }

    public static void stop(Context context) {
        sLogReader.stop();
        sLogReader.removeListener(sLogListener);
        sLogListener.clear(context);
    }
}
