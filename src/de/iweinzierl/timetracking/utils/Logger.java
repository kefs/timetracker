package de.iweinzierl.timetracking.utils;

import android.util.Log;

public class Logger {

    public static final String LOG_TAG = "[TimeTracker]";

    public static void debug(Class source, String message) {
        Log.d(createLogTag(source), message);
    }

    public static void info(Class source, String message) {
        Log.i(createLogTag(source), message);
    }

    public static void warn(Class source, String message) {
        Log.w(createLogTag(source), message);
    }

    public static void error(Class source, String message) {
        Log.e(createLogTag(source), message);
    }

    public static void error(Class source, String message, Throwable throwable) {
        Log.e(createLogTag(source), message, throwable);
    }

    private static String createLogTag(Class source) {
        String className = source != null ? source.getName() : "unknown";
        return String.format("[%s] %s", LOG_TAG, className);
    }
}
