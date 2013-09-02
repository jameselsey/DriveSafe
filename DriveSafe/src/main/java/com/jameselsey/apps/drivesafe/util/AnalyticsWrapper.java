package com.jameselsey.apps.drivesafe.util;


import android.content.Context;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.jameselsey.apps.drivesafe.R;

public class AnalyticsWrapper {

    private static AnalyticsWrapper instance;
    private final Tracker tracker;

    public AnalyticsWrapper(Context ctx) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(ctx);
        tracker = analytics.getTracker(ctx.getResources().getString(R.string.ga_trackingId));
    }

    public static void logEvent(Context ctx, String category, String action, String label, long value){
        if (instance == null){
            instance = new AnalyticsWrapper(ctx);
        }

        instance.tracker.sendEvent(category, action, label, value);
    }
}
