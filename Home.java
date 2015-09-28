package com.development.buccola.myrecipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.development.buccola.myrecipes.add_recipe.addRecipes_fragment;
import com.development.buccola.myrecipes.my_recipes.myRecipes_fragment;
import com.development.buccola.myrecipes.pantry.myPantry_fragment;
import com.development.buccola.myrecipes.share_recipe.ShareRecipe;
import com.development.buccola.myrecipes.shopping_list.ShoppingList;
import com.development.buccola.myrecipes.wheel.wheel_fragment;
import com.example.megan.myapplication.R;

/**
 * Created by megan on 3/22/15.
 *
 * code from https://www.youtube.com/watch?v=zwqzhY5i2rc
 */


public class Home extends Fragment{
    View homeView;
    Button pantry, myRecipes, addRecipes, wheel, shoppingList, shareRecipe, test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home, container, false);
        pantry = (Button) homeView.findViewById(R.id.pantry);
        myRecipes = (Button) homeView.findViewById(R.id.recipes);
        addRecipes = (Button) homeView.findViewById(R.id.addRecipe);
        wheel = (Button) homeView.findViewById(R.id.mealWheel);
        shoppingList = (Button) homeView.findViewById(R.id.shoppingList);
        shareRecipe = (Button) homeView.findViewById(R.id.shareRecipe);
        pantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new myPantry_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        shoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new ShoppingList();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        myRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new myRecipes_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });


        addRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new addRecipes_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });


        wheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new wheel_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        //This is not a fragment just a new instatnce
        shareRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(){
                    public void run(){
                       // your_stuff();

                Fragment newFragment = new ShareRecipe();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
           /*     Intent i = new Intent(getActivity(), ShareRecipe.class);
                startActivity(i);*/
                    }
                };
                t.start();
            }
        });


                return homeView;

    }

}


