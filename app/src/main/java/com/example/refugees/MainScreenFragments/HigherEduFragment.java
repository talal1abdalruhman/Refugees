package com.example.refugees.MainScreenFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refugees.R;
import com.google.android.material.button.MaterialButton;


public class HigherEduFragment extends Fragment implements View.OnClickListener {

    public HigherEduFragment() {
        // Required empty public constructor
    }

    View view;
    MaterialButton ZarDisBtn, AADisBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_higher_edu, container, false);
        ZarDisBtn = view.findViewById(R.id.ZarqaaDiscountBtn);
        ZarDisBtn.setOnClickListener(this);
        AADisBtn = view.findViewById(R.id.AmmanArabDiscountBtn);
        AADisBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String link;
        switch (v.getId()){
            case R.id.ZarqaaDiscountBtn:{
                link = "https://static.help.unhcr.org/wp-content/uploads/sites/46/2020/09/10105224/Zarqaa-University.pdf#_ga=2.205956755.1428249176.1607809913-2145797768.1607609879";
                break;
            }
            case R.id.AmmanArabDiscountBtn:{
                link = "https://static.help.unhcr.org/wp-content/uploads/sites/46/2020/09/28105858/Amman-Arab-University-Rates.pdf#_ga=2.261081613.1428249176.1607809913-2145797768.1607609879";
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }
}