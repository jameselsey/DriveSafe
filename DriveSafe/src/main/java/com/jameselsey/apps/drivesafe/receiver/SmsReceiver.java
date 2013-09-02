package com.jameselsey.apps.drivesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.jameselsey.apps.drivesafe.domain.Sms;
import com.jameselsey.apps.drivesafe.service.SpeakerService;
import com.jameselsey.apps.drivesafe.util.AnalyticsWrapper;
import com.jameselsey.apps.drivesafe.util.Constants;

import java.util.ArrayList;

import static com.jameselsey.apps.drivesafe.util.Constants.CATEGORY_FEATURE;
import static java.lang.String.format;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_EXTRA_NAME = "pdus";
    private final String TAG = format("%s - %s - ", Constants.APP_LOG_NAME, getClass().getSimpleName());

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        AnalyticsWrapper.logEvent(context, CATEGORY_FEATURE, "SMS message received", "SMS message has been received", 1l);
        if (extras != null) {
            Object[] smsExtras = (Object[]) extras.get(SMS_EXTRA_NAME);

            ArrayList<Sms> myMessages = new ArrayList<Sms>();

            for (int i = 0; i < smsExtras.length; ++i) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtras[i]);

                Sms mySms = new Sms(sms.getOriginatingAddress(), sms.getMessageBody().toString());
                myMessages.add(mySms);
            }
            Intent smsIntent = new Intent(context, SpeakerService.class);
            intent.setAction(Constants.ACTION_SPEAK_SMS);
            smsIntent.putParcelableArrayListExtra(Constants.EXTRA_SMS_MESSAGES, myMessages);

            Log.d(TAG, "Starting service...");
            context.startService(smsIntent);
        }
    }
}
