package com.common.tools.loggerx;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;

public class MCsvFormatStrategy implements FormatStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";
    private static final String LOOGER_DIVIDE = " <*** ***> "; //自定义分隔符

    @NonNull
    private final Date date;
    @NonNull
    private final SimpleDateFormat dateFormat;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private MCsvFormatStrategy(@NonNull Builder builder) {
        checkNotNull(builder);

        date = builder.date;
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    //2019.09.20 17:22:40.337,DEBUG,（$onceOnlyTag）
    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        checkNotNull(message);

        String tag = formatTag(onceOnlyTag);

        StringBuilder builder = new StringBuilder();

        date.setTime(System.currentTimeMillis());
        builder.append(dateFormat.format(date));

        // level
        builder.append(SEPARATOR);
        builder.append(logLevel(priority));

        // logger divide
        builder.append(LOOGER_DIVIDE);

        // tag
        builder.append(SEPARATOR);
        builder.append(tag);

        // message
        if (message.contains(NEW_LINE)) {
            // a new line would break the CSV format, so we replace it here
            message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        builder.append(SEPARATOR);
        builder.append(message);

        // new line
        builder.append(NEW_LINE);

        logStrategy.log(priority, tag, builder.toString());
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!isEmpty(tag) && !isEquals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static final class Builder {
        private static final int MAX_BYTES = 1000 * 1024; // 1m averages to a 4000 lines per file

        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag = "PRETTY_LOGGER";
        String defPath; //增加日志路径

        private Builder() {
        }

        @NonNull
        public Builder date(@Nullable Date val) {
            date = val;
            return this;
        }

        @NonNull
        public Builder dateFormat(@Nullable SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }

        @NonNull
        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public Builder defPath(@Nullable String defPath) {
            this.defPath = defPath;
            return this;
        }

        @NonNull
        public MCsvFormatStrategy build() {
            if (date == null) {
                date = new Date();
            }
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            }
            if (logStrategy == null) {
                String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String folder;
                if (TextUtils.isEmpty(defPath)) {
                    folder = diskPath + File.separatorChar + "logger";
                } else {
                    folder = diskPath + File.separatorChar + "logger" + File.separatorChar + defPath;
                }

                HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
                ht.start();
                Handler handler = new MDiskLogStrategy.WriteHandler(ht.getLooper(), folder, MAX_BYTES);
                logStrategy = new DiskLogStrategy(handler);
            }
            return new MCsvFormatStrategy(this);
        }
    }

    @NonNull
    <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    String logLevel(int value) {
        switch (value) {
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    boolean isEquals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        if (a != null && b != null) {
            int length = a.length();
            if (length == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
