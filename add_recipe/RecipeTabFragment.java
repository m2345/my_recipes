package com.development.buccola.myrecipes.add_recipe;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.megan.myapplication.R;

/***************************************************
 * FILE:        RecipeTabFragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15
 * EXTENDS:     ActionBarActivity
 * Purpose:     Controls the tabs of Add Recipe
 * NOTES:       code from https://www.youtube.com/watch?v=zwqzhY5i2rc
 ***************************************************/

public class RecipeTabFragment extends ActionBarActivity {
    View rootview;
    ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.add_recipes_layout, container, false);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager) rootview.findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getSupportActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        Tab.setAdapter(TabAdapter);
        actionBar = getSupportActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
               // Log.d("selected: ", "onTabSelected at" + "position" + tab.getPosition() + " " + tab.getText());
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
               // Log.d("Unselected: ", "onTabSelected at" + "position" + tab.getPosition() + " " + tab.getText());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
               // Log.d("Reselected: ", "onTabSelected at" + "position" + tab.getPosition() + " " + tab.getText());
            }

        };
        //Add New Tab
        actionBar.addTab(actionBar.newTab().setText("General").setTabListener(tabListener)); //Android
        actionBar.addTab(actionBar.newTab().setText("Add Ingredients").setTabListener(tabListener)); //IOS
        actionBar.addTab(actionBar.newTab().setText("Add steps").setTabListener(tabListener)); //Windows
        return rootview;
    }





}




