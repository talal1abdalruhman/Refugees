package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.Slide;
import androidx.viewpager.widget.ViewPager;

import com.example.refugees.HelperClasses.PagerAdapter;
import com.example.refugees.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class UnhcrFragment extends Fragment {

    public UnhcrFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    }

    View view;
    TabLayout tabLayout;
    TabItem tabOffices, tabHelpdesks, tabCommunitySupport;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        view = inflater.inflate(R.layout.fragment_unhcr, container, false);

        tabLayout = view.findViewById(R.id.unhcr_tabLayout);
        tabOffices = view.findViewById(R.id.offices);
        tabHelpdesks = view.findViewById(R.id.helper_desks);
        tabCommunitySupport = view.findViewById(R.id.community_support);
        viewPager = view.findViewById(R.id.unhcr_view_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(
                getParentFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

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


    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Navigation.findNavController(view).navigate(R.id.action_unhcrFragment_to_home);
        }
    };

}