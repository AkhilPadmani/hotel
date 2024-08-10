package com.example.hotel.UI.Fragment.Waiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hotel.UI.Adapter.Activity.RecyclerViewAdapter;
import com.example.hotel.Core.Model.Data;
import com.example.hotel.R;
import com.example.hotel.UI.Activity.Waiter.HotelMainActivity;
import com.example.hotel.UI.Interface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ArrayList<Data> DataList = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerViewAdapter Adapter;
    Activity activity;
    private int sliderPosition = 0;
    private Runnable runnableSlide;
    private Handler timerHandler = new Handler();
    private int runnableIntCount = 0;
    private static final String TAG = "HomeFragment";
    ViewPager viewpager;
    private ViewPagerAdapter adapter1;
   /* AdView adView;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        this.recyclerView = v.findViewById(R.id.RecyclerView);
        this.viewpager = v.findViewById(R.id.viewpager);
       // this.adView = v.findViewById(R.id.AdView);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewPager(viewpager);
        initTimer();
        /*AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
        int[] image = {R.drawable.hotel, R.drawable.hotel, R.drawable.hotel, R.drawable.hotel, R.drawable.hotel, R.drawable.hotel, R.drawable.hotel, R.drawable.hotel};
        String[] TableName = {"Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Hardik Pandya", "Ravindra Jadeja"};
        String[] Description = {"Number Of Member :- 4", "Number Of Member :- 2", "Number Of Member :- 5", "Number Of Member :- 2", "Number Of Member :- 6  ", "Nice To Meet You", "How Are You??", "Are You Free Today??"};
        DataList.add(new Data(image[0], TableName[0]));
        DataList.add(new Data(image[1], TableName[1]));
        DataList.add(new Data(image[2], TableName[2]));
        DataList.add(new Data(image[3], TableName[3]));
        DataList.add(new Data(image[4], TableName[4]));
        DataList.add(new Data(image[5], TableName[5]));
        Adapter = new RecyclerViewAdapter(DataList, getContext());
        Adapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void ItemClick(int position) {
                Log.i("TAG", "ItemClick: " + position);
                Intent intent = new Intent(getContext(), HotelMainActivity.class);

                //TODO Comment for Some Little Bit Time
               intent.putExtra("TableName", TableName[position]);
                Log.i(TAG, "ItemClick: "+position+" "+intent);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(Adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initTimer() {
        runnableSlide = new Runnable() {
            @Override
            public void run() {
                try {
                    if (sliderPosition == adapter1.getCount()) {
                        sliderPosition = 0;
                    } else {
                        sliderPosition = sliderPosition + 1;
                    }

                    viewpager.setCurrentItem(sliderPosition, true);

                } catch (Exception e) {
                }
                timerHandler.postDelayed(this, 2000);
            }
        };
        timerHandler.postDelayed(runnableSlide, 2000);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter1 = new ViewPagerAdapter(getChildFragmentManager());
        adapter1.addFragment(new FirstFragment(), "");
        adapter1.addFragment(new SecondFragment(), "");
        adapter1.addFragment(new ThirdFragment(), "");
        adapter1.addFragment(new FourthFragment(), "");
        adapter1.addFragment(new FiveFragment(), "");

        viewPager.setAdapter(adapter1);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}