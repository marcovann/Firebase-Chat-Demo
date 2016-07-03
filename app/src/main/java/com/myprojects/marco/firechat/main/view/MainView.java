package com.myprojects.marco.firechat.main.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marco on 16/08/16.
 */

public class MainView extends CoordinatorLayout implements MainDisplayer {

    private Toolbar toolbar;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavigationView logoutView;
    private CircleImageView profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;

    private DrawerActionListener drawerActionListener;
    private NavigationActionListener navigationActionListener;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_main_view, this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_hamburger);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        logoutView = (NavigationView) navigationView.findViewById(R.id.logout_view);
        View headerLayout = navigationView.getHeaderView(0);
        profileImageView = (CircleImageView) headerLayout.findViewById(R.id.profileImageView);
        nameTextView = (TextView) headerLayout.findViewById(R.id.nameTextView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.emailTextView);
    }

    @Override
    public void attach(final DrawerActionListener drawerActionListener, NavigationActionListener navigationActionListener) {

        this.drawerActionListener = drawerActionListener;
        this.navigationActionListener = navigationActionListener;

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        logoutView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        profileImageView.setOnClickListener(headerClickListener);
        nameTextView.setOnClickListener(headerClickListener);
        emailTextView.setOnClickListener(headerClickListener);
        toolbar.setNavigationOnClickListener(navigationClickListener);
    }

    @Override
    public void detach(DrawerActionListener drawerActionListener, NavigationActionListener navigationActionListener) {
        //drawer.removeDrawerListener();
        navigationView.setNavigationItemSelectedListener(null);
        logoutView.setNavigationItemSelectedListener(null);
        profileImageView.setOnClickListener(null);
        nameTextView.setOnClickListener(null);
        emailTextView.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setUser(User user) {
        Utils.loadImageElseWhite(user.getImage(),profileImageView,getContext());
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
    }

    @Override
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            drawerActionListener.onNavigationItemSelected(item);
            return true;
        }
    };

    private final OnClickListener headerClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            drawerActionListener.onHeaderSelected();
        }
    };

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            navigationActionListener.onHamburgerPressed();
        }
    };

}

