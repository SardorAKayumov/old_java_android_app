package com.zeroteam.gurudistr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;

import com.google.android.material.tabs.TabLayout;
import com.zeroteam.gurudistr.fragments.OneSellingFragment1;
import com.zeroteam.gurudistr.fragments.OneSellingFragment2;
import com.zeroteam.gurudistr.models.StoreModel;
import com.zeroteam.gurudistr.models.TerritoryModel;

import java.util.ArrayList;
import java.util.List;

public class OneSelling extends AppCompatActivity {
    private StoreModel storeModel;
    private TerritoryModel territoryModel;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_selling);

        Window window = OneSelling.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OneSelling.this, R.color.colorPrimaryDark));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storeModel = (StoreModel) getIntent().getSerializableExtra("storeModel");
        territoryModel = (TerritoryModel) getIntent().getSerializableExtra("territoryModel");
        uid = getIntent().getStringExtra("uid");

        //setTitle(storeModel.getName());

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }

    public StoreModel getStoreModel() {return storeModel;}
    public TerritoryModel getTerritoryModel() {return territoryModel;}
    public String getUid() { return uid; }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new OneSellingFragment1(), "SELL");

        viewPagerAdapter.addFragment(new OneSellingFragment2(), "HISTORY");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        StaticHolderClass.productQuantities.clear();
        StaticHolderClass.productsPrices.clear();
        super.onBackPressed();
    }
}
