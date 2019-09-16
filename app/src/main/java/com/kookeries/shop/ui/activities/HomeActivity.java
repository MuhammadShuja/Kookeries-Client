package com.kookeries.shop.ui.activities;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.ui.fragments.AccountFragment;
import com.kookeries.shop.ui.fragments.CartFragment;
import com.kookeries.shop.ui.fragments.CategoriesFragment;
import com.kookeries.shop.ui.fragments.HomeFragment;
import com.kookeries.shop.models.Cart;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener{

    private Fragment fragment = null;
    private Class fragmentClass = null;
    private String fragmentTitle = null;
    private BottomNavigationViewEx mBottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        API.instantiate(getApplicationContext());

        initBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();

        addBadgeAt(2, Cart.count());
    }

    private void initBottomNav(){
        mBottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
        mBottomNavigationViewEx.enableAnimation(false);
        mBottomNavigationViewEx.enableShiftingMode(false);
        mBottomNavigationViewEx.enableItemShiftingMode(false);
        mBottomNavigationViewEx.setTextSize(12);

        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                int position = 0;
                switch (id) {
                    case R.id.bnav_home:
                        position = 0;
                        fragmentClass = HomeFragment.class;
                        fragmentTitle = "Home";

                        break;
                    case R.id.bnav_catalog:
                        position = 1;
                        fragmentClass = CategoriesFragment.class;
                        fragmentTitle = "Catalog";

                        break;
                    case R.id.bnav_cart:
                        position = 2;
                        fragmentClass = CartFragment.class;
                        fragmentTitle = "My Cart ("+Cart.count()+")";

                        break;
                    case R.id.bnav_account:
                        position = 3;
                        fragmentClass = AccountFragment.class;
                        fragmentTitle = "Account";

                        break;
                }
                loadFragment();

                mBottomNavigationViewEx.setItemIconTintList(getResources().getColorStateList(R.color.warm_grey_two));
                mBottomNavigationViewEx.setItemTextAppearanceInactive(getResources().getIdentifier("colorTheme", "color", getPackageName()));
                mBottomNavigationViewEx.setIconTintList(position, getResources().getColorStateList(R.color.colorTheme));
                mBottomNavigationViewEx.setTextTintList(position, getResources().getColorStateList(R.color.colorTheme));
                return true;
            }
        });

        mBottomNavigationViewEx.setSelectedItemId(R.id.bnav_home);
        addBadgeAt(2, Cart.count());
    }

    private void loadFragment(){
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

        if (fragmentTitle != null) {
            setTitle(fragmentTitle);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(17, 2, true)
                .bindTarget(mBottomNavigationViewEx.getBottomNavigationItemView(position))
                .setBadgeBackground(getResources().getDrawable(R.drawable.bg_badge))
                .setShowShadow(true);
    }
}
