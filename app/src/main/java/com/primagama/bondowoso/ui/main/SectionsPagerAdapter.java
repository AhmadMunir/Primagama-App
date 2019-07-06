package com.primagama.bondowoso.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentlist = new ArrayList<>();
    private final List<String> mFragmentTitlelist = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        mFragmentlist.add(fragment);
        mFragmentTitlelist.add(title);
    }

    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitlelist.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentlist.size();
    }
}