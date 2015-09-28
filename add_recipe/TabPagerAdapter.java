package com.development.buccola.myrecipes.add_recipe;

/***************************************************
 * FILE:        TabPagerAdapter
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/26/15
 * EXTENDS:     FragmentStatePagerAdapter
 * Purpose:     Adapter for tabs
 ***************************************************/
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Fragment for Add Recipe General
                return new AddRecipe_General_Fragment();
            case 1:
                //Fragment for Add Recipe Ingredients
                return new AddRecipe_Ingredients_Fragment();
            case 2:
                //Fragment for Add Recipe Steps
                return new AddRecipe_Steps_Fragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3; //Number of Tabs
    }
}