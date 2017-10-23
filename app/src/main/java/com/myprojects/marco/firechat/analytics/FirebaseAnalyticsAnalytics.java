package com.myprojects.marco.firechat.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsAnalytics implements Analytics {

    private static final String PARAM_USER_ID = "user_id";
    private static final String EVENT_REMOVE_ACCOUNT = "remove_account";

    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalyticsAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void trackDeleteAccount(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        firebaseAnalytics.logEvent(EVENT_REMOVE_ACCOUNT, bundle);
    }

}
