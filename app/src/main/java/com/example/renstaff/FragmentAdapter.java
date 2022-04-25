package com.example.renstaff;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.renstaff.ui.MainFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MainFragment();
            case 1:
                return new CalculatorFragment();
            case 2:
                return new ArchiveFragment();
            case 3:
                return new StatsFragment();
        }
        return new MainFragment();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
