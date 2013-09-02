package com.jameselsey.apps.drivesafe.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import static com.jameselsey.apps.drivesafe.util.Constants.CATEGORY_FEATURE;
import static java.lang.String.format;

public class TtsWrapper {

    private static final String TAG = Constants.APP_LOG_NAME + " - TtsWrapper ";
    private static TextToSpeech tts;
    private static DateTime startTime;

    private TtsWrapper() {
        // private constructor to prevent instantiation
    }

    public static TextToSpeech getInstance() {
        return tts;
    }

    public static void startTts(Context context, TextToSpeech.OnInitListener listener) {
        AnalyticsWrapper.logEvent(context, CATEGORY_FEATURE, "SMS detection started", "SMS detection has been started, listening for incoming SMS messages", 1l);
        if (tts == null) {
            Log.d(TAG, "tts is null, creating a new instance");
            tts = new TextToSpeech(context, listener);
        } else {
            Log.d(TAG, "TtsWrapper - tts is already assigned, doing nothing");
        }

        startTime = new DateTime();
    }

    public static void finishUsingTts(Context context) {

        long duration = getTtsRunningTime();

        Log.d(TAG, format("TTS was active for %d seconds", duration));
        AnalyticsWrapper.logEvent(context, CATEGORY_FEATURE, "SMS detection stopped", "SMS detection has been stopped, no longer listening for incoming SMS messages", duration);

        if (tts != null) {
            Log.d(TAG, "Shutting down TTS...");
            tts.shutdown();
            tts = null;
        }
    }

    private static long getTtsRunningTime() {
        DateTime now = new DateTime();

        Period period = new Period(startTime, now);
        PeriodFormatter HHMMSSFormater = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendSeconds()
                .toFormatter();

        String durationString = HHMMSSFormater.print(period);

        return Long.parseLong(durationString);
    }

}
