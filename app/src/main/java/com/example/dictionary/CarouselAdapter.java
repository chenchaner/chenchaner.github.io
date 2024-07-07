package com.example.dictionary;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public CarouselAdapter(ReciteBookActivity fragment) {
        super(fragment);
    }

    public void addItem(Fragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
