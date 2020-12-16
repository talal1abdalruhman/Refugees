package com.example.refugees.MainScreenFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refugees.HelperClasses.HealthPagerAdapter;
import com.example.refugees.HelperClasses.PagerAdapter;
import com.example.refugees.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HealthServicesFragment extends Fragment {

    public HealthServicesFragment() {
        // Required empty public constructor
    }

    View view;
    TabLayout tabLayout;
    TabItem tabHS, tabSHS;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_services, container, false);

        tabLayout = view.findViewById(R.id.healthServices_tabLayout);
        tabHS = view.findViewById(R.id.tab_health_service);
        tabSHS = view.findViewById(R.id.tab_supported_health_service);
        viewPager = view.findViewById(R.id.healthServices_view_pager);
        HealthPagerAdapter healthPagerAdapter = new HealthPagerAdapter(
                getParentFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(healthPagerAdapter);

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