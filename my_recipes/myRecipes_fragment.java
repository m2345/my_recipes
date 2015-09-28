package com.development.buccola.myrecipes.my_recipes;
/***************************************************
 * FILE:        myRecipes_fragment
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15
 * EXTENDS:     Fragment
 * PURPOSE:     My Recipes Fragment
 * LAYOUT:      myrecipes_layout
 ***************************************************/
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.development.buccola.myrecipes.common.RecipeActions;
import com.development.buccola.myrecipes.database.DataHandler;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.util.ArrayList;

public class myRecipes_fragment extends Fragment {
    View rootview;
    ListView listView;
    DataHandler handler;
    ArrayList<String> listOutput;   //array of string to load the listView
    ArrayList<Integer> idOutput;    //array of recipe ids
    ArrayList<Recipe> myRecipes;    //array of recipes
    EditText searchText;
    String typeSelected;
    RadioGroup group;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.myrecipes_layout, container, false); //error shows in video too
        listView = (ListView) rootview.findViewById(R.id.lvMyRecipes);
        searchText = (EditText) rootview.findViewById(R.id.searchText);
        group = (RadioGroup) rootview.findViewById(R.id.radioGroup);
        typeSelected = "all";
        myRecipes = new ArrayList<>();
        loadRecipeList();

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               updateRecipeList();
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBreakfast:
                        typeSelected = "Breakfast";
                        break;
                    case R.id.radioLunch:
                        typeSelected = "Lunch";
                        break;
                    case R.id.radioDinner:
                        typeSelected = "Dinner";
                        break;
                    case R.id.radioDessert:
                        typeSelected = "Dessert";
                        break;
                    default:
                        typeSelected = "all";
                }

                updateRecipeList();
            }
        });

        setAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayRecipe(myRecipes.get(position), position);
            }
        });

        return rootview;
    }

    /*********************************
     FUNCTION:  void updateRecipeList()
     PARAMS:    NONE
     RETURNS:   Nothing
     PURPOSE:   loads list of recipes based on search criteria
     *********************************/
    private void updateRecipeList(){
        String searchTextString = searchText.getText().toString();
        listOutput = new ArrayList<>();
        handler = new DataHandler(rootview.getContext());
        handler.open();
        Cursor C = handler.searchRecipes(searchTextString, typeSelected);
        if (C.moveToFirst()) {
            do {
                Log.e("MYREC", "items returned " + C.getCount());
                listOutput.add(
                        "Recipe: " + C.getString(1) + " " +
                                "Source: " + C.getString(2)
                );
                int id = Integer.parseInt(C.getString(0));
                idOutput.add(id);
                RecipeActions r = new RecipeActions(rootview.getContext());
                Recipe newRecipe = r.getRecipeById(Integer.parseInt(C.getString(0)));
                myRecipes.add(newRecipe);
            } while (C.moveToNext());
        }

        setAdapter();
        handler.close();
    }

    /*********************************
     FUNCTION:  void setAdapter()
     PARAMS:    NONE
     RETURNS:   Nothing
     PURPOSE:   set adapter for the listView of recipes
     NOTES:     Uses arrayAdapter and listOutput to load listView
     *********************************/
    private void setAdapter(){
        ArrayAdapter adapter =
                new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, listOutput);
        listView.setAdapter(adapter);
    }

    /*********************************
     FUNCTION:  void loadRecipeList()
     PARAMS:    NONE
     RETURNS:   Nothing
     PURPOSE:   loads of all recipes
     *********************************/
    private void loadRecipeList() {
        handler = new DataHandler(rootview.getContext());
        handler.open();
        listOutput = new ArrayList<>();
        idOutput = new ArrayList<>();
        Cursor C = handler.getRecipes();
        if (C.moveToFirst()) {
            do {
                listOutput.add(
                        "Recipe: " + C.getString(1) + " \n" +
                                "Source: " + C.getString(2)
                );
                idOutput.add(Integer.parseInt(C.getString(0)));
                RecipeActions r = new RecipeActions(rootview.getContext());
                Recipe newRecipe = r.getRecipeById(Integer.parseInt(C.getString(0)));
                myRecipes.add(newRecipe);
            } while (C.moveToNext());
        }
        handler.close();

    }

    /*********************************
     FUNCTION:  void displayRecipe(final Recipe, final int)
     PARAMS:    Recipe object, int position
     RETURNS:   Nothing
     PURPOSE:   Displays selected recipe in a dialog
     *********************************/
    public void displayRecipe(final Recipe recipe, final int position) {
        final Dialog dialog = new Dialog(rootview.getContext());
        String title = recipe.getTitle();
        dialog.setContentView(R.layout.display_recipe2);
        dialog.setTitle(title);
        Spanned word = Html.fromHtml(RecipeActions.printRecipe(recipe));
        TextView recipeText = (TextView) dialog.findViewById(R.id.recipeInfo);
        recipeText.setText(word);
        Button btnDelete = (Button) dialog.findViewById(R.id.delete);
        Button btnMake = (Button) dialog.findViewById(R.id.make);
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
                        ra.deleteRecipe(recipe.getRecipeId());
                        Toast.makeText(dialog2.getContext(), "Recipe Deleted", Toast.LENGTH_LONG).show();
                        myRecipes.remove(position);
                        listOutput.remove(position);
                        idOutput.remove(position);
                        setAdapter();
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

        /*********************************
         METHOD:  btnMake.setOnClickListener()
         PURPOSE: Makes Recipe when button is pressed
         NOTES:   Called when user presses make recipe button
                    Makes sure user really wants to make the recipe
                    if yes, the recipe is made
                    Also checks to see if recipe is able to be made
                    if no, the item is added to the shopping list.
         *********************************/
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecipeActions ra = new RecipeActions(rootview.getContext());
                final Dialog dialog3 = new Dialog(dialog.getContext());
                dialog3.setContentView(R.layout.custom_dialog);
                ImageView image = (ImageView) dialog3.findViewById(R.id.image);
                image.setImageResource(R.mipmap.ic_launcher);
                TextView textview = (TextView) dialog3.findViewById(R.id.text);
                Button dialogButtonYes = (Button) dialog3.findViewById(R.id.dialogButtonDelete);
                Button dialogButtonCancel = (Button) dialog3.findViewById(R.id.dialogButtonCancel);
                dialogButtonYes.setText("Yes, Add");
                final boolean able = ra.ableToMake(myRecipes.get(position));
                    //confirm make
                if (able) {
                    dialog3.setTitle("Confirm Make");
                    textview.setText("Items in your pantry will be decreased.");
                } else { //confirm add
                    dialog3.setTitle("Add needed Ingrdients to shopping List");
                    textview.setText("You do not have the required ingredients. \n Want to add the needed ingredients to shopping list?");
                }


                dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeActions ra = new RecipeActions(rootview.getContext());
                        if (!able) {
                            Log.e("info", "not bale to make");
                            ra.addNeededIngredients(myRecipes.get(position).getIngredients());
                        } else {
                            Log.e("info", "able to make");
                            ra.makeRecipe(myRecipes.get(position));
                        }
                        dialog3.dismiss();
                        dialog.dismiss();

                    }
                });

                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                dialog3.show();

            }
        });
        }
}
