package com.example.renstaff.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.renstaff.ArchiveFragment;
import com.example.renstaff.FragmentAdapter;
import com.example.renstaff.MainActivity;
import com.example.renstaff.R;
import com.example.renstaff.StatsFragment;
import com.example.renstaff.ui.calculator.CalculatorFragment;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

}