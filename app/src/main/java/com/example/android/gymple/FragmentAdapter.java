
package com.example.android.gymple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.gymple.Moments.MomentsFragment;

/**
 * The FragmentAdapter class is a control class that determines which fragment
 * to show on the screen as the user swipe between both fragments (DetailsFragment, MomentsFragment)
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    /**
     * Android-level function: constructor
     * @param fm The fragmentManager
     */
    public FragmentAdapter(FragmentManager fm) {

        super(fm);
    }

    /**
     * getCount returns number of fragment pages
     * @return The number of fragmentss
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Android-level function: gets the value which indicates which fragment to show
     * @param position integer value of which page view it is at
     * @return The fragment that will be shown
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new DetailsFragment();
        } else {
            return new MomentsFragment();
        }
    }

    /**
     * Android-level function: based on the value of page view, determine the title of the page view
     * @param position integer value of which page view it is at
     * @return The current fragment's title
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Description";
        } else {
            return "Moments";
        }
    }
}