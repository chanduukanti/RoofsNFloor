package com.example.mapslist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    
    public int  tabsCount = 2;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
        case 0:
            // Top Rated fragment activity
           return new ListViewFragment();
        case 1:
            // Games fragment activity
            return new MapViewFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tabsCount;
    }

}
