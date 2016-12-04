package com.myprojects.marco.firechat.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsAnalytics implements Analytics {

    private static final String PARAM_USER_ID = "user_id";
    private static final String EVENT_REMOVE_ACCOUNT = "remove_account";
    /*
    private static final String PARAM_SENDER = "sender";
    private static final String EVENT_SIGN_UP_SUCCESS = "sign_up_success";
    private static final String EVENT_MESSAGE_LENGTH = "message_length";
    private static final String EVENT_INVITE_OPENED = "invite_opened";
    private static final String EVENT_INVITE_ACCEPTED = "invite_accepted";
    private static final String EVENT_SEND_INVITES = "send_invites";
    */

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

   /* @Override
    public void trackSignInStarted(String method) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.SIGN_UP_METHOD, method);
        firebaseAnalytics.logEvent(Event.SIGN_UP, bundle);
    }

    @Override
    public void trackSignInSuccessful(String method) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.SIGN_UP_METHOD, method);
        firebaseAnalytics.logEvent(EVENT_SIGN_UP_SUCCESS, bundle);
    }

    @Override
    public void trackMessageLength(int messageLength, String userId, String channelName) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.VALUE, messageLength);
        bundle.putString(PARAM_CHANNEL_NAME, channelName);
        bundle.putString(PARAM_USER_ID, userId);
        firebaseAnalytics.logEvent(EVENT_MESSAGE_LENGTH, bundle);
    }

    @Override
    public void trackInvitationOpened(String senderId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SENDER, senderId);
        firebaseAnalytics.logEvent(EVENT_INVITE_OPENED, bundle);
    }

    @Override
    public void trackInvitationAccepted(String senderId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SENDER, senderId);
        firebaseAnalytics.logEvent(EVENT_INVITE_ACCEPTED, bundle);
    }

    @Override
    public void trackSendInvitesSelected(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        firebaseAnalytics.logEvent(EVENT_SEND_INVITES, bundle);
    }
*/
}
