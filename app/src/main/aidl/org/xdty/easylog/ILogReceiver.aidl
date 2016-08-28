package org.xdty.easylog;

import org.xdty.easylog.LogLine;

interface ILogReceiver {
    void log(in LogLine line);
}
