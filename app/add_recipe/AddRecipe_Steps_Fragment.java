package com.development.buccola.myrecipes.add_recipe;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.buccola.myrecipes.common.RecipeActions;
import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.development.buccola.myrecipes.recipe.Steps;
import com.example.megan.myapplication.R;

import java.util.ArrayList;

/***************************************************
 * FILE:        AddRecipe_Steps_Fragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/26/15.
 * Purpose:     Add Steps to the recipe
 * EXTENDS:     Fragment
 * LAYOUT:      add_recipe_steps_fragment
 ***************************************************/
    public class AddRecipe_Steps_Fragment extends Fragment
        implements AdapterView.OnItemSelectedListener, GetRecipeData {

        View stepsView;
        ImageButton addStep, saveRecipe;
        ListView listView;
        EditText stepText;
        TextView stepNumber;
        int countSteps, nextStepNumber;
        static Recipe recipe;  //Recipe object of the final recipe to be saved
        GetRecipeData SM;
        ArrayList<Steps> stepsArray;  //arrayList of added Steps

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            stepsView = inflater.inflate(R.layout.add_recipe_steps_fragment, container, false);
            addStep = (ImageButton) stepsView.findViewById(R.id.buttonAddStep);
            saveRecipe = (ImageButton) stepsView.findViewById(R.id.buttonSaveRecipe);
            listView = (ListView) stepsView.findViewById(R.id.listView2);
            stepNumber = (TextView) stepsView.findViewById(R.id.txCurrentStepNumber);
            stepText = (EditText) stepsView.findViewById(R.id.stepText);
            stepsArray = new ArrayList<>();
            recipe = new Recipe();

            //add step to stepsArray
            addStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String text = stepText.getText().toString().trim();
                    if (!text.equals("")) {
                        countSteps = stepsArray.size()+1;
                        Steps step = new Steps();
                        step.setStep(countSteps + ") " + text);
                        stepsArray.add(step);
                        stepText.setText("");
                        nextStepNumber = stepsArray.size()+1;  //increase step numnber
                        stepNumber.setText("Step #" + nextStepNumber);
                        loadStepsList();
                    } else {
                        Toast.makeText(getActivity(), "No Step Written",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });


            saveRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipe = new Recipe();
                    SM.RequestForGeneral();
                    SM.RequestForIngredients();

                    for(int i=0; i<stepsArray.size(); i++)
                        recipe.addStep(stepsArray.get(i));


                    if(validateRecipe(recipe)){
                        RecipeActions create = new RecipeActions(stepsView.getContext());
                        create.save(recipe);
                        Toast.makeText(getActivity(), "Recipe Saved", Toast.LENGTH_LONG).show();
                    }

                }
            });

            return stepsView;

        }

    /*********************************
     FUNCTION:  boolean validateRecipe()
     PARAMS:    NONE
     RETUNS:    returns true if valid; false if not valid
     PURPOSE:   Checks if user entered a valid recipe
     *********************************/
        private boolean validateRecipe(Recipe recipe){
            Log.e("ORDER", "ValidateRecipe()");
            if(recipe != null) {
                String error = "";
                int errorCount = 0;
                if (recipe.getIngredients().size() == 0) {
                    error = "Please add ingredients \n";
                    errorCount++;
                }
                if (recipe.getSteps().size() == 0) {
                    error += "Please add Steps \n";
                    errorCount++;
                }
                if (recipe.getTitle() == null) {
                    error += "Please provide a recipe name \n";
                }

                if (errorCount > 0) {
                    Toast.makeText(stepsView.getContext(), error, Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }else {
                Log.e("Error", "Null while validating");
            }
                return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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

    /*********************************
     FUNCTION:  void loadStepsList()
     PARAMS:    NONE
     RETUNS:    returns nothing
     PURPOSE:   loads the listView of added steps
     NOTES:     uses a custom adapter ListViewAdapterAddSteps
     *********************************/

    private void loadStepsList(){
        ArrayList<String> stepsList = new ArrayList<>();
        for(int i=0; i<stepsArray.size(); i++)
            stepsList.add(stepsArray.get(i).getStep());

        ListViewAdapterAddSteps adapter = new ListViewAdapterAddSteps(stepsView.getContext(),
                android.R.layout.simple_list_item_1, stepsList);
        listView.setAdapter(adapter);
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
     FUNCTION:  void getGeneralData(Recipe)
     PARAMS:    Recipe object
     RETUNS:    returns nothing
     PURPOSE:   gets general recipe data from AddRecipe_General_Fragment
                Copies obj data into recipe
     *********************************/
    public void getGeneralData(Recipe obj){
        recipe.setTitle(obj.getTitle());
        recipe.setSharedUrl(obj.getSharedUrl());
        recipe.setServing_size(obj.getServing_size());
        recipe.setMeal_type(obj.getMeal_type());
        recipe.setCalories_per_serving(obj.getCalories_per_serving());
        recipe.setCook_time(obj.getCook_time());
        recipe.setSource(obj.getSource());
        recipe.setPrep_time(obj.getPrep_time());
        recipe.setTotal_servings(obj.getTotal_servings());

    }

    /*********************************
     FUNCTION:  void getIngrdientsData(ArrayList<Ingredient>)
     PARAMS:    ArrayList of Ingredient objects
     RETUNS:    returns nothing
     PURPOSE:   gets ingredients user added from AddRecipe_Steps_Fragment
                inserts each ingredient into recipe
     *********************************/
    public void getIngredientsData(ArrayList<Ingredient> data){
        for (int i = 0; i < data.size(); i++)
            recipe.addIngredients(data.get(i));
    }
}



