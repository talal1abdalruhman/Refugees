package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.refugees.HelperClasses.EducationPagerAdapter;
import com.example.refugees.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class EducationFragment extends Fragment {

    public EducationFragment() {
        // Required empty public constructor
    }

    View view;
    TabLayout tabLayout;
    TabItem tabBasic, tabHigher;
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Navigation.findNavController(view).navigate(R.id.action_educationFragment_to_home);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_education, container, false);
        tabLayout = view.findViewById(R.id.education_tabLayout);
        tabBasic = view.findViewById(R.id.basic_education_tab);
        tabHigher = view.findViewById(R.id.higher_education_tab);
        viewPager = view.findViewById(R.id.education_view_pager);
        EducationPagerAdapter educationPagerAdapter = new EducationPagerAdapter(
                getParentFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(educationPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}