package com.example.dictionary.AuxiliaryClass;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dictionary.BookSearch.BookSearchFragment;
import com.example.dictionary.DailySaying.DailySayingFragment;
import com.example.dictionary.Like.LikeFragment;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.FragAdapter;
import com.example.dictionary.Translation.TranslationFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    List<Fragment> fragments = new ArrayList<>();
    private BottomLayout bottomLayout;
    private FragAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        bottomLayout = findViewById(R.id.bottom);
        fragments.add(new DailySayingFragment());
        fragments.add(new TranslationFragment());
        fragments.add(new LikeFragment());
        fragments.add(new BookSearchFragment());
        viewPager = findViewById(R.id.viewPager);
        adapter = new FragAdapter(this,fragments);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomLayout.updateButtonStates("search");
                        break;
                    case 1:
                        bottomLayout.updateButtonStates("translation");
                        break;
                    case 2:
                        bottomLayout.updateButtonStates("notebook");
                        break;
                    case 3:
                        bottomLayout.updateButtonStates("recite");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomLayout.getViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
    }
}