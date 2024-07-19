package com.example.dictionary.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class FragAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;
    public FragAdapter(FragmentActivity fragmentActivity, List<Fragment> Fragments){
        super(fragmentActivity);
        mFragments = Fragments;

    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
