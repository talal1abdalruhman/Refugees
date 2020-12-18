package com.example.refugees.HelperClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.refugees.MainScreenFragments.BasicEduFragment;
import com.example.refugees.MainScreenFragments.HigherEduFragment;


public class EducationPagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public EducationPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BasicEduFragment();
            case 1:
                return new HigherEduFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
