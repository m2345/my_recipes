package com.development.buccola.myrecipes.add_recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.util.ArrayList;

/***************************************************
 * FILE:        addRecipes_Fragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15.
 * PURPOSE:     Holder of the tabbed fragmnets and processes GetRecipeData requests
 * EXTENDS:     Fragment
 * IMPLEMENTS:  GetRecipeData
 * NOTES:       code from https://www.youtube.com/watch?v=zwqzhY5i2rc
 * LAYOUT:      add_recipes_layout
 ***************************************************/

public class addRecipes_fragment extends Fragment implements GetRecipeData {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.add_recipes_layout, container, false);
        ViewPager pager=(ViewPager)result.findViewById(R.id.pager);
        pager.setAdapter(buildAdapter());
        return(result);
    }
    private PagerAdapter buildAdapter() {

       return(new AdapterAddRecipe(getActivity(), getChildFragmentManager()));
    }

    @Override
    public void RequestForGeneral() {
        AdapterAddRecipe adapter = new AdapterAddRecipe(getActivity(), getChildFragmentManager());
        AddRecipe_General_Fragment f2 = (AddRecipe_General_Fragment) adapter.getItem(0); //getFragmentManager().findFragmentById(R.id.f2);
        f2.requestMade();
    }

    @Override
    public void RequestForIngredients() {
        AdapterAddRecipe adapter = new AdapterAddRecipe(getActivity(), getChildFragmentManager());
        AddRecipe_Ingredients_Fragment f2 = (AddRecipe_Ingredients_Fragment) adapter.getItem(1); //getFragmentManager().findFragmentById(R.id.f2);
        f2.requestMade();
       /* AddRecipe_Ingredients_Fragment f2 = (AddRecipe_Ingredients_Fragment) getFragmentManager().findFragmentById(R.id.f2);
        f2.requestMade();*/
    }

    @Override
    public void sendData(Recipe recipe) {
        AdapterAddRecipe adapter = new AdapterAddRecipe(getActivity(), getChildFragmentManager());
        AddRecipe_Steps_Fragment f2 = (AddRecipe_Steps_Fragment) adapter.getItem(2); //getFragmentManager().findFragmentById(R.id.f1);


        f2.getGeneralData(recipe);
    }

    @Override
    public void sendArrayData(ArrayList<Ingredient> ingredients) {
        AdapterAddRecipe adapter = new AdapterAddRecipe(getActivity(), getChildFragmentManager());
        AddRecipe_Steps_Fragment f2 = (AddRecipe_Steps_Fragment) adapter.getItem(2); //getFragmentManager().findFragmentById(R.id.f1);


        f2.getIngredientsData(ingredients);
    }
}




