package njit.myapp;

//import android.app.Fragment;

import android.content.Context;

import android.os.Bundle;

//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Nidhish on 11-04-2015.
 */

public class TestPagerAdapter extends FragmentPagerAdapter
        implements ViewPager.OnPageChangeListener{


    ProfileFragment profileFragment;
    NewsFeedFragment newsFeedFragment;
    NeedAttentionFragment needAttentionFragment;

    private Context context;
    private ViewPager pager;
//    private Vector<Fragment> fragments = new Vector<Fragment>();
    CharSequence Titles[]={"My Profile","NewsFeed", "Need Attention"};



    public TestPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int index) {
        if(index == 0 ) {
            profileFragment = ProfileFragment.getInstance();
            return  profileFragment;
        } else if(index == 1) {
            newsFeedFragment = new NewsFeedFragment();
            return  newsFeedFragment;
        } else {
            needAttentionFragment = new NeedAttentionFragment();
            return  needAttentionFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return Titles[position];
    }

    @Override
    public int getCount() {
        return 3;

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
   //     actionBar.setSelectedNavigationItem(position);
    }
}