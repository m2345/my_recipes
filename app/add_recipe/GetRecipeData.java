package com.development.buccola.myrecipes.add_recipe;

import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;

import java.util.ArrayList;

/***************************************************
 * FILE:        interface GetRecipeData
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/28/15.
 * Purpose:     Allows addRecipe tabs to communicate with each other
 ***************************************************/
public interface GetRecipeData {

    //Steps calls these;
     void RequestForGeneral();
     void RequestForIngredients();

    //General and Ingredients calls this
     void sendData(Recipe recipe);
     void sendArrayData(ArrayList<Ingredient> data);

}
