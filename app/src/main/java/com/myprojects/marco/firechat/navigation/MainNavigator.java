package com.myprojects.marco.firechat.navigation;

/**
 * Created by marco on 16/08/16.
 */

public interface MainNavigator {

    void attach();

    void detach();

    void init();

    void toConversations();

    void toGlobalRoom();

    void toUserList();

    void toInvite();

    void toProfile();

    void toFirstLogin();

    void toGoogleSignOut(int method);

    void toLogin();

    void showProgessDialog();

    void hideProgessDialog();

    Boolean onBackPressed();

}
