package com.development.buccola.myrecipes.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/***************************************************
 * FILE:        ViewRecipes
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * EXTENDS:     Fragment
 * IMPLEMENTS:  Serializable
 * PURPOSE:     Displays list of Recipes
 * LAYOUT:      mypantry_layout
 ***************************************************/

public class ViewRecipes extends Fragment implements Serializable{
    ArrayList<Recipe> recipeArray;
    ListView listView;
    View rootview;
    ArrayList<String> listOutput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootview = inflater.inflate(R.layout.mypantry_layout, container, false);
        Bundle extras = this.getArguments();
        if (extras != null) {
            try {
                recipeArray = (ArrayList<Recipe>) bytes2Object(extras.getByteArray("JSON"));
            }catch(IOException e){
                Log.e("ioexce", "IO Exception");
            }catch(ClassNotFoundException e){
                Log.e("class", "class not found exception");
            }catch(ClassCastException e){
                Log.e("exception", "Class Cast excetpion");
            }
        }
        listView = (ListView) rootview.findViewById(R.id.listView);

      loadListView();
        return rootview;
    }

    /*********************************
     FUNCTION:  object bytes2Object(byte[])
     PARAMS:    byte array
     RETURNS:   and object
     PURPOSE:   Converting byte arrays to objects
     *********************************/
    static public Object bytes2Object( byte raw[] )
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }

    /*********************************
     FUNCTION:  object loadListView()
     PARAMS:    None
     RETURNS:   Nothing
     PURPOSE:   Loads listView of recipes
     NOTES:     Uses ArrayAdapter and listOutput is used to load the list
     *********************************/
    public void loadListView(){
        ArrayAdapter adapter;
        listOutput = new ArrayList<>();
        ArrayList<String> empty = new ArrayList<>();
        empty.add("No New Recipes");

        if(recipeArray.size() == 0) {
            adapter = new ArrayAdapter<>(rootview.getContext(), R.layout.textview, listOutput);
            listView.setAdapter(adapter);
        }
        else {
            for (int i = 0; i < recipeArray.size(); i++) {
                listOutput.add("name " + recipeArray.get(i).getTitle() + "\n"
                        + "Source: " + recipeArray.get(i).getSource());
            }
            adapter = new ArrayAdapter<>(rootview.getContext(), R.layout.textview, listOutput);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    displayRecipe(recipeArray.get(position), position);
                }
            });
        }
    }

    /*********************************
     FUNCTION:  void diplayRecipe(Recipe, int)
     PARAMS:    Recipe object to be displayed and int - the positon
     RETURNS:   NOTHING
     PURPOSE:   Display the given recipe
     DIALOG LAYOUT: display_recipe
     *********************************/
    public void displayRecipe(final Recipe recipe, final int position){
        final Dialog dialog = new Dialog(rootview.getContext());

        dialog.setContentView(R.layout.display_recipe);
        dialog.setTitle("Chosen Recipe");

        TextView recipeText =(TextView) dialog.findViewById(R.id.recipeInfo);
        Spanned word = Html.fromHtml(RecipeActions.printRecipe(recipe));
        recipeText.setText(word);

        Button btnSave=(Button)dialog.findViewById(R.id.save);
        Button btnCancel=(Button)dialog.findViewById(R.id.cancel);
        Button btnIgnore = (Button) dialog.findViewById(R.id.ignore);
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeActions r = new RecipeActions(rootview.getContext());
                r.save(recipe);
                r.deleteShared(recipe);
                dialog.dismiss();
                recipeArray.remove(position);
                loadListView();
            }
        });

        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeActions r = new RecipeActions(rootview.getContext());
                r.deleteShared(recipe);
                dialog.dismiss();
                recipeArray.remove(position);
                loadListView();
            }
        });

    }


}