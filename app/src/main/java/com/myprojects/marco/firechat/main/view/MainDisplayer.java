package com.myprojects.marco.firechat.main.view;

import android.view.MenuItem;

import com.myprojects.marco.firechat.user.data_model.User;

/**
 * Created by marco on 16/08/16.
 */

public interface MainDisplayer {

    void attach(DrawerActionListener drawerActionListener, NavigationActionListener navigationActionListener);

    void detach(DrawerActionListener drawerActionListener, NavigationActionListener navigationActionListener);

    void setTitle(String title);

    void setUser(User user);

    void openDrawer();

    void closeDrawer();

    public interface DrawerActionListener {

        void onHeaderSelected();

        void onNavigationItemSelected(MenuItem item);

    }

    interface NavigationActionListener {

        void onHamburgerPressed();

    }

}
