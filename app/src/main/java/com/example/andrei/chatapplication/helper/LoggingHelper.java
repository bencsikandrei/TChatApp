package com.example.andrei.chatapplication.helper;

import android.util.Log;

/**
 * @author Andrei
 *         Log helper
 *         debug and error wrappers
 */

public class LoggingHelper {
    public static final String TAG = "eu.telecom.afbencsi";

    public static void logDebug(String callClass, String callMethod) {
        new LoggingHelper().log('d', callClass, callMethod);
    }

    /* TODO error wrapper */
    public static void logError(String callClass, String callMethod) {
        new LoggingHelper().log('e', callClass, callMethod);
    }

    public static void logInfo(String callClass, String callMethod) {
        new LoggingHelper().log('i', callClass, callMethod);
    }

    private void log(char type, String callClass, String callMethod) {
        switch (type) {
            case 'e':
                Log.e(TAG, " | ERROR | " + callClass + " : " + callMethod);
                break;
            case 'd':
                Log.d(TAG, " | DEBUG | " + callClass + " : " + callMethod);
                break;
            case 'i':
                Log.i(TAG, " | INFO | " + callClass + " : " + callMethod);
                break;
        }

    }

}
