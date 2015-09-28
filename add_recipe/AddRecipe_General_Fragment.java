package com.development.buccola.myrecipes.add_recipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.util.ArrayList;

/***************************************************
 * FILE:        AddRecipe_General_Fragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15.
 * EXTENDS:     Fragment
 * IMPLEMENTS:  GetRecipeData
 * Purpose:     control fragments for each tab.
 * NOTES:       with code example from https://www.youtube.com/watch?v=zwqzhY5i2rc
 * LAYOUT:      add_recipe_general_fragment
 ***************************************************/

public class AddRecipe_General_Fragment extends Fragment implements GetRecipeData {
    View rootview;
    static EditText title, source, cookTime, prepTime, caloriesPerServing, servingsPerMeal, servingSize;
    Spinner spinnerMealTypes;
    static String currentType;
    int sendCook, sendPrep, sendCalories, sendServingSize, sendServingsPerRecipe;
    String sendTitle, sendSource;
    static GetRecipeData SM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.add_recipe_general_fragment, container, false);

        spinnerMealTypes = (Spinner) rootview.findViewById(R.id.mealTypeSpinner);
        title = (EditText) rootview.findViewById(R.id.title);
        source = (EditText) rootview.findViewById(R.id.source);
        prepTime = (EditText) rootview.findViewById(R.id.etPrepTime);
        cookTime = (EditText) rootview.findViewById(R.id.etCookTime);

        caloriesPerServing = (EditText) rootview.findViewById(R.id.numOfCaloriesPer);
        servingsPerMeal = (EditText) rootview.findViewById(R.id.numOfServings);
        servingSize = (EditText) rootview.findViewById(R.id.servingsPerPerson);
        loadMealTypesSpinner();
        /*********************************
         When a new meal type in the spinner is selected the currentType is changed to the selected item
         *********************************/
        spinnerMealTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                currentType = spinnerMealTypes.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        return rootview;

    }

    /*********************************
     FUNCTION:  void loadmealTypesSpinner()
     PARAMS:    NONE
     RETUNS:    returns nothing
     PURPOSE:   laods meal types spinner.
     NOTES:     uses ArrayAdapter to load spinner. Options are loaded from R.array.Meal_types_array
     *********************************/
    private void loadMealTypesSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Meal_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealTypes.setAdapter(adapter);
    }

            @Override
            public void onAttach(Activity activity){
                 super.onAttach(activity);
                try{
                    SM = (GetRecipeData) getParentFragment();
                }catch(ClassCastException e){
                    throw new ClassCastException("You need to implement all methods");
                }
            }

            @Override
            public void RequestForGeneral() {

            }

            @Override
            public void RequestForIngredients() {

            }

            @Override
            public void sendData(Recipe recipe) {

            }

            @Override
            public void sendArrayData(ArrayList<Ingredient> data) {

            }

    /*********************************
     FUNCTION:  void requestMade()
     PARAMS:    NONE
     RETUNS:    returns nothing
     PURPOSE:   sends the General info to Steps Fragment when user saves recipe
     NOTES:     data is added to a Recipe Object and sent as a recipe object
     *********************************/
            public void requestMade(){
                Recipe recipe = new Recipe();
                sendTitle = title.getText().toString();
                sendSource = source.getText().toString();

                if(cookTime.getText().toString().isEmpty())
                    sendCook =-1;
                else
                    sendCook = Integer.parseInt(cookTime.getText().toString());

                if(prepTime.getText().toString().isEmpty())
                    sendPrep = -1;
                else
                    sendPrep = Integer.parseInt(prepTime.getText().toString());

                if(caloriesPerServing.getText().toString().isEmpty())
                    sendCalories = -1;
                else
                    sendCalories = Integer.parseInt(caloriesPerServing.getText().toString());

                if(servingSize.getText().toString().isEmpty())
                    sendServingSize = -1;
                else
                    sendServingSize = Integer.parseInt(servingSize.getText().toString());

                if(servingsPerMeal.getText().toString().isEmpty())
                    sendServingsPerRecipe = -1;
                else
                    sendServingsPerRecipe = Integer.parseInt(servingsPerMeal.getText().toString());

                recipe.setTitle(sendTitle);
                recipe.setSource(sendSource);
                recipe.setCook_time(sendCook);
                recipe.setPrep_time(sendPrep);
                recipe.setMeal_type(currentType);
                recipe.setCalories_per_serving(sendCalories);
                recipe.setServing_size(sendServingSize);
                recipe.setTotal_servings(sendServingsPerRecipe);

                SM.sendData(recipe);


            }

        }


