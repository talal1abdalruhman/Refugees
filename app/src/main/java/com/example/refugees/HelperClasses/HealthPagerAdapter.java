package com.example.refugees.HelperClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.refugees.MainScreenFragments.CommunitySupportFragment;
import com.example.refugees.MainScreenFragments.HSFragment;
import com.example.refugees.MainScreenFragments.HelpdesksFragment;
import com.example.refugees.MainScreenFragments.OfficesFragment;
import com.example.refugees.MainScreenFragments.SHSFragment;

public class HealthPagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public HealthPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HSFragment();
            case 1:
                return new SHSFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}