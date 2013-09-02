package com.jameselsey.apps.drivesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.jameselsey.apps.drivesafe.domain.Sms;
import com.jameselsey.apps.drivesafe.util.AnalyticsWrapper;
import com.jameselsey.apps.drivesafe.util.Constants;
import com.jameselsey.apps.drivesafe.util.TtsWrapper;

import java.util.ArrayList;

import static com.jameselsey.apps.drivesafe.util.Constants.CATEGORY_FEATURE;
import static java.lang.String.format;

public class SpeakerService extends Service {

    private final String TAG = format("%s - %s - ", Constants.APP_LOG_NAME, getClass().getSimpleName());

    @Override
    public void onCreate() {
        Log.d(TAG, "In onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "speaking something from intent " + intent.getAction());
        Bundle extras = intent.getExtras();

        if (isPhoneInSilentOrVibrateMode()) {
            AnalyticsWrapper.logEvent(this, CATEGORY_FEATURE, "SMS message ignored", "SMS message has been ignored as the device is on vibrate or silent", 1l);
            return 0;
        }

        if (extras != null) {
            speakMessages(extras);
        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "in onDestroy()");
        TtsWrapper.finishUsingTts(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void speakMessages(Bundle extras) {
        ArrayList<Sms> smsMessages = extras.getParcelableArrayList(Constants.EXTRA_SMS_MESSAGES);
        Log.d(TAG, format("There were %d messages", smsMessages.size()));
        for (Sms sms : smsMessages) {
            speakMessage(sms);
        }
    }

    private void speakMessage(Sms sms) {
        AnalyticsWrapper.logEvent(this, CATEGORY_FEATURE, "SMS message spoken", "SMS message has been spoken", 1l);
        String message = format("SMS received from %s, message is %s", sms.getSender(), sms.getBody());
        Log.d(TAG, "Attempting to speak " + message);

        TextToSpeech mTts = TtsWrapper.getInstance();
        if (mTts != null) {
            mTts.speak(message, TextToSpeech.QUEUE_ADD, null);
        }
    }

    private boolean isPhoneInSilentOrVibrateMode() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        return ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE;
    }
}
