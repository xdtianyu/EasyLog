package org.xdty.easylog;

public class EasyLog {

    private static LogReader sLogReader = LogReader.getInstance();
    private static LogReader.LogListener sLogListener;

    public static void enable() {
        sLogReader.restart();
        sLogListener = new LogReader.LogListener() {
            @Override
            public void onReadLine(String line) {

            }
        };
        sLogReader.addListener(sLogListener);
    }

    public static void disable() {
        sLogReader.stop();
        sLogReader.removeListener(sLogListener);
    }
}
