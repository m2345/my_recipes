package com.development.buccola.myrecipes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.development.buccola.myrecipes.add_recipe.addRecipes_fragment;
import com.development.buccola.myrecipes.my_recipes.myRecipes_fragment;
import com.development.buccola.myrecipes.pantry.myPantry_fragment;
import com.development.buccola.myrecipes.share_recipe.ShareRecipe;
import com.development.buccola.myrecipes.shopping_list.ShoppingList;
import com.development.buccola.myrecipes.wheel.wheel_fragment;
import com.example.megan.myapplication.R;

/***************************************************
 * PROGRAM:     My Recipes
 * PROGRAMMER:  Megan Buccola
 * PURPOSE:     This program is meant to help users organize their pantry and to
 *              know what they are able to make for lunch or dinner
 ***************************************************/

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

     /*
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objFragment = null;
        switch(position){
            case 0:
                objFragment = new Home();
                break;
            case 1:
                objFragment = new myPantry_fragment();
                break;
            case 2:
                objFragment = new ShoppingList();
                break;
            case 3:
                objFragment = new myRecipes_fragment();
                break;
            case 4:
                objFragment = new addRecipes_fragment();
                break;
            case 5:
                objFragment = new wheel_fragment();
                break;
            case 6:
                objFragment = new ShareRecipe();
                break;
            case 7:
                objFragment = new KnownBugs();
                break;
        }

        //Creatd thread for shareRecipe fragment
        // update the main content by replacing fragments
        final Fragment finalObjFragment = objFragment;
        Thread t = new Thread(){
            public void run() {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, finalObjFragment)
                        .commit();
            }};
        t.start();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_home);
                break;
            case 2:
                mTitle = getString(R.string.title_pantry);
                break;
            case 3:
                mTitle = getString(R.string.title_shopping);
                break;
            case 4:
                mTitle = getString(R.string.title_recipes);
                break;
            case 5:
                mTitle = getString(R.string.title_add_recipe);
                break;
            case 6:
                mTitle = getString(R.string.title_wheel);
                break;
            case 7:
                mTitle= getString(R.string.title_share);
                break;
            case 8:
                mTitle = getString(R.string.title_known_bugs);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
