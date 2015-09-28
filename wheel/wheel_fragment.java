package com.development.buccola.myrecipes.wheel;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.buccola.myrecipes.common.RecipeActions;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

/***************************************************
 * FILE:        wheel_fragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15
 * PURPOSE:     Fragment for wheel
 * EXTENDS:     Fragment
 ***************************************************/
public class wheel_fragment extends Fragment {
    View rootview;
    Button spin, view, make;
    ImageButton delete;
    TextView recipeTxt;
    ArrayList<String[]> recipeList;
    int selectedIndex;
    int selectedRecipeId;
    ArrayList<Recipe> recipesAbleMake;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.wheel_layout, container, false); //error shows in video too
        spin = (Button) rootview.findViewById(R.id.buttonSpin);
        view = (Button) rootview.findViewById(R.id.buttonView);
        make = (Button) rootview.findViewById(R.id.buttonMake);
        delete = (ImageButton) rootview.findViewById(R.id.buttonDelete);
        recipeTxt = (TextView) rootview.findViewById(R.id.selectedMeal);
        recipeList = new ArrayList<>();
        selectedIndex = -1;
        recipesAbleMake = new ArrayList<>();
        selectedRecipeId = -1;
        loadRecipeArray();
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Button clicked");
                if (recipesAbleMake.size() > 0) {
                    selectedIndex = randomChooser();
                    selectedRecipeId = recipesAbleMake.get(selectedIndex).getRecipeId();//Integer.parseInt(recipeList.get(selectedIndex)[0]);
                    recipeTxt.setText(recipesAbleMake.get(selectedIndex).getTitle());
                } else {
                    recipeTxt.setText("There is no recipe you can make");
                }
            }
        });

        //onclick
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Button Delete");
                if(selectedIndex != -1)
                    displayRecipe(recipesAbleMake.get(selectedIndex));
                else
                    Toast.makeText(rootview.getContext(), "Spin to select a recipe", Toast.LENGTH_LONG).show();

            }
        });

        /*********************************
         METHOD:    make.setOnClickListener();
         PURPOSE:   Make randomly selceted recipe
         *********************************/
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex != -1) {


                    final Dialog dialog3 = new Dialog(rootview.getContext());
                    dialog3.setTitle("Confirm Make");
                    dialog3.setContentView(R.layout.custom_dialog);
                    ImageView image = (ImageView) dialog3.findViewById(R.id.image);
                    image.setImageResource(R.mipmap.ic_launcher);
                    TextView textview = (TextView) dialog3.findViewById(R.id.text);
                    Button dialogButtonYes = (Button) dialog3.findViewById(R.id.dialogButtonDelete);
                    Button dialogButtonCancel = (Button) dialog3.findViewById(R.id.dialogButtonCancel);
                    dialogButtonYes.setText("Yes, Make");
                    textview.setText("\nAre you sure you want to make this. \n Items will be decreased from your pantry.\n");

                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeActions ra = new RecipeActions(rootview.getContext());
                            ra.makeRecipe(recipesAbleMake.get(selectedIndex));
                            boolean able = ra.ableToMake(recipesAbleMake.get(selectedIndex));
                            if (!able)
                                ra.addNeededIngredients(recipesAbleMake.get(selectedIndex).getIngredients());
                            dialog3.dismiss();
                            displayRecipe(recipesAbleMake.get(selectedIndex));
                        }
                    });

                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                        }
                    });
                    dialog3.show();


                }else
                    Toast.makeText(rootview.getContext(), "Spin to selecte a recipe", Toast.LENGTH_LONG).show();
            }
        });


        //onclick
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex != -1) {
                    // custom dialog
                    final Dialog dialog = new Dialog(rootview.getContext());
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Confirm Delete");

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("\nAre you sure you want to delete this recipe?\n");
                    ImageView image = (ImageView) dialog.findViewById(R.id.image);
                    image.setImageResource(R.mipmap.ic_launcher);
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonDelete);
                    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);

                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Yes,", "delete");
                            RecipeActions ra = new RecipeActions(rootview.getContext());
                            ra.deleteRecipe(selectedRecipeId);
                            Toast.makeText(dialog.getContext(), "Recipe Deleted", Toast.LENGTH_LONG).show();
                            recipeTxt.setText("");
                            recipeList = new ArrayList<>();
                            selectedIndex = -1;
                            selectedRecipeId = -1;
                            loadRecipeArray();
                            dialog.dismiss();

                        }
                    });

                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("No", "Cancel");
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else{
                    Toast.makeText(rootview.getContext(), "Spin to select a recipe", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootview;
    }
    /*********************************
     FUNCTION:  int randomChooser();
     PARAMS:    none
     RETURNS:   int - arrayList index of selectd recipe
     PURPOSE:   selects a radom index of recipesAbleToMake
     *********************************/
    private int randomChooser() {
        Random randomNum = new Random();
        return randomNum.nextInt(recipesAbleMake.size());
    }

    /*********************************
     FUNCTION:  void displayRecipe(Recipe);
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   disaply selected recipe in dialog
     DIALOG LAYOUT: display_recipe3
     *********************************/
    public void displayRecipe(final Recipe recipe_) {
        final Dialog dialog = new Dialog(rootview.getContext());

        dialog.setContentView(R.layout.display_recipe3);
        dialog.setTitle("Meal Wheel Chosen Recipe");
        Spanned word = Html.fromHtml(RecipeActions.printRecipe(recipe_));
        TextView recipeText = (TextView) dialog.findViewById(R.id.recipeInfo);
        recipeText.setText(word);
        Button btnDelete = (Button) dialog.findViewById(R.id.delete);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog2 = new Dialog(dialog.getContext());
                dialog2.setContentView(R.layout.custom_dialog);
                dialog2.setTitle("Confirm Delete");
                ImageView image = (ImageView) dialog2.findViewById(R.id.image);
                image.setImageResource(R.mipmap.ic_launcher);
                Button dialogButtonYes = (Button) dialog2.findViewById(R.id.dialogButtonDelete);
                Button dialogButtonCancel = (Button) dialog2.findViewById(R.id.dialogButtonCancel);
                dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeActions ra = new RecipeActions(rootview.getContext());
                        ra.deleteRecipe(recipe_.getRecipeId());
                        Toast.makeText(dialog2.getContext(), "Recipe Deleted", Toast.LENGTH_LONG).show();
                        recipesAbleMake.remove(selectedIndex);
                        ra.deleteRecipe(recipe_.getRecipeId());
                        dialog2.dismiss();
                        dialog.dismiss();
                    }
                });

                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
            }
        });

    }

    /*********************************
     FUNCTION:  void loadRecipeArray();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   load ararys of recipes able to make
     *********************************/
    private void loadRecipeArray() {
        recipesAbleMake = new ArrayList<>();
        RecipeActions r = new RecipeActions(rootview.getContext());
        ArrayList<Integer> allRecipeIds = r.getAllRecipeIds();
        for (int i = 0; i < allRecipeIds.size(); i++) {
            RecipeActions ra = new RecipeActions(rootview.getContext());
            Recipe current = r.getRecipeById(allRecipeIds.get(i));
            if (ra.ableToMake(current))
                recipesAbleMake.add(current);
        }

    }
}
