package com.example.renstaff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.renstaff.ArchiveFragment;
import com.example.renstaff.StatsFragment;
import com.example.renstaff.ui.TabLayoutFragment;
import com.example.renstaff.ui.calculator.CalculatorFragment;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private final String [] tabTitles = new String[]{"Главная","Калькулятор","Архив","Общие"};

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainFragment();
            case 1:
                return new CalculatorFragment();
            case 2:
                return new ArchiveFragment();
            case 3:
                return new StatsFragment();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}





