package org.xdty.easylog;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class LogPool {

    private int mSize;
    private Deque<LogLine> mCache;

    private StringBuffer mStringBuffer;

    public LogPool(int size) {
        mSize = size;
        mCache = new ArrayDeque<>(size);
        mStringBuffer = new StringBuffer();
    }

    public void append(LogLine logLine) {
        if (mCache.size() == mSize) {
            mCache.peek();
        }
        mCache.push(logLine);
    }

    public String cache() {
        mStringBuffer.setLength(0);
        Iterator<LogLine> it = mCache.descendingIterator();
        while (it.hasNext()) {
            mStringBuffer.append("\n").append(it.next().content);
        }
        return mStringBuffer.toString();
    }

}
