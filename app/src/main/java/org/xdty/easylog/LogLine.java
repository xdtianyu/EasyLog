package org.xdty.easylog;

import android.os.Parcel;
import android.os.Parcelable;

public class LogLine implements Parcelable {

    public static final Creator<LogLine> CREATOR = new Creator<LogLine>() {
        @Override
        public LogLine createFromParcel(Parcel in) {
            return new LogLine(in);
        }

        @Override
        public LogLine[] newArray(int size) {
            return new LogLine[size];
        }
    };

    long time;
    int type;
    String content;
    String tag;

    public LogLine(String line) {
        content = line;
        time = 0;
        type = 0;
        tag = "";
    }

    protected LogLine(Parcel in) {
        time = in.readLong();
        type = in.readInt();
        content = in.readString();
        tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeString(tag);
    }
}
