package com.development.buccola.myrecipes.add_recipe;

/***************************************************
 * FILE = AdapterAddRecipe
 * PROGRAMMER = Megan Buccola
 * CREATED: 3/27/15.
 * PURPOSE: controls fragments for each tab.
 * EXTENDS: FragmentStatePagerAdapter
 ***************************************************/


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapterAddRecipe extends FragmentStatePagerAdapter {
    Context ctxt=null;
    public AdapterAddRecipe(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }
    @Override
    public int getCount() {
        return(3);
    }   //return number of pages



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //Fragement for General
                return new AddRecipe_General_Fragment();
            case 1:
                //Fragment Add Rcipe Ingredients
                return new AddRecipe_Ingredients_Fragment();
            case 2:
                //Fragment for Add Recipe Steps
                return new AddRecipe_Steps_Fragment();
        }
        return null;

    }
    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return "General";
            case 1:
                return "Add Ingredients";
            case 2:
                return "Add Steps";
        }
        return null;
    }
}