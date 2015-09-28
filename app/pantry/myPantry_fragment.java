package com.development.buccola.myrecipes.pantry;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.development.buccola.myrecipes.common.Pantry;
import com.development.buccola.myrecipes.common.RecipeActions;
import com.development.buccola.myrecipes.common.UnitConversions;
import com.development.buccola.myrecipes.database.DataHandler;
import com.example.megan.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

/***************************************************
 * FILE:        ViewRecipes
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/22/15
 * EXTENDS:     Fragment
 * IMPLEMENTS:  AdapterView.OnItemSelectedListener
 *              PantryListViewAdapter.customButtonListener
 * PURPOSE:     Fragment to show pantry items and add pantry items
 * LAYOUT:      mypantry_layout
 * NOTES:       code from https://www.youtube.com/watch?v=zwqzhY5i2rc
 ***************************************************/

public class myPantry_fragment extends Fragment implements
        AdapterView.OnItemSelectedListener, PantryListViewAdapter.customButtonListener {
    View rootview;
    ImageButton addBtn;                     //add item
    EditText searchTerm, amtInput;
    DataHandler handler;
    Spinner spinner, unitSpinner;
    ListView listView;
    TextView itemSelectedLabel;
    String itemSelected;                     //selected item
    String unitSelected;                     //selected unit
    ArrayList<Integer> allIds;               //list of all pantry ids
    ArrayList<PantryItem> itemsInPantry;     //list of all items in pantry
    HashMap<String, Integer> spinnerMapId;   //name, ingredientId
    HashMap<String, String> spinnerMapUnits; // name, unit
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mypantry_layout, container, false);
        unitSelected = "";
        spinnerMapId = new HashMap<>();
        spinnerMapUnits = new HashMap<>();
        addBtn = (ImageButton) rootview.findViewById(R.id.addToPantry);
        searchTerm = (EditText) rootview.findViewById(R.id.searchText);
        spinner = (Spinner) rootview.findViewById(R.id.ingredientsSpinner);
        unitSpinner = (Spinner) rootview.findViewById(R.id.unitSpinner);
        itemSelectedLabel = (TextView) rootview.findViewById(R.id.TXselction);
        amtInput = (EditText) rootview.findViewById(R.id.amtNum);
        listView = (ListView) rootview.findViewById(R.id.listView);
        itemsInPantry = new ArrayList<>();
        allIds = new ArrayList<>();

        // Spinner click listen
        spinner.setOnItemSelectedListener(this);
        loadPantry();
        searchTerm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<String> output = new ArrayList<>();
                String search = searchTerm.getText().toString();
                String getName;
                String getUnit;
                String getId;
                handler = new DataHandler(rootview.getContext());
                handler.open();
                Cursor C = handler.searchIngredients(search);
                if (C.moveToFirst()) {
                    do {
                        getId = C.getString(0); //if used parse to int
                        getUnit = C.getString(2).trim();
                        getName = C.getString(1).trim();
                        Log.e("Adding to output", getId + " :id");
                        output.add(getName);
                        spinnerMapUnits.put(getName, getUnit);
                        spinnerMapId.put(getName, Integer.parseInt(getId));
                    } while (C.moveToNext());
                }
                handler.close();
                updateSpinner(output);
                // Log.d("COUNT", "" + spinnerMap.size());
            }
        });

        //unitSpinner
        loadUnitSpinner();
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unitSelected = unitSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button Press", "adding");
                double amt = Double.parseDouble(amtInput.getText().toString());
                // int indexId = getHash(itemSelectedLabel.getText().toString());
                String unit = spinnerMapUnits.get(itemSelected);
                int ingredientId = spinnerMapId.get(itemSelected);
                Log.e("INfo", "unit: " + unit + " ingredientId: " + ingredientId
                        + " amt: " + amt);
                boolean flag = false;
                int flagPosition = -1;
                for (int i = 0; i < itemsInPantry.size(); i++) {
                    if (itemsInPantry.get(i).getIngredientId() == ingredientId) {
                        flag = true;
                        flagPosition = i;
                    }
                }

                if (flag) {
                    Log.e("Pantry", "Update pantry");
                    Pantry pantry = new Pantry(rootview.getContext());
                    String dbU = pantry.getDBUnits(ingredientId).trim();
                    double currentAmt = itemsInPantry.get(flagPosition).getAmtAsStored();

                    UnitConversions uc = new UnitConversions(dbU, unitSelected, amt);
                    double amtToAdd = uc.compareUnits();
                    if (amtToAdd != -1) {
                        double newAmt = amtToAdd + currentAmt;
                        Log.e("New amt Calc:", amtToAdd + " + " + currentAmt + " = " + (currentAmt + amtToAdd));
                        handler = new DataHandler(rootview.getContext());
                        handler.open();
                        handler.updatePantry(newAmt, ingredientId);
                        handler.close();
                    } else {
                        Toast.makeText(rootview.getContext(), "Error Converting Units", Toast.LENGTH_LONG).show();
                        Log.e("Test", "UC is -1 updating");
                    }
                } else {
                    Log.e("Pantry", "Insert into Pantry");
                    Pantry pantry = new Pantry(rootview.getContext());
                    String dbU = pantry.getDBUnits(ingredientId);
                    amt = Double.parseDouble(amtInput.getText().toString());

                    UnitConversions uc = new UnitConversions(dbU, unitSelected, amt);
                    double newAmt = uc.compareUnits();
                    if (newAmt != -1) {
                        handler = new DataHandler(rootview.getContext());
                        handler.open();
                        handler.insertIntoPantry(newAmt, ingredientId);
                        handler.close();
                    } else {
                        Toast.makeText(rootview.getContext(), "Error Converting Units", Toast.LENGTH_LONG).show();
                        Log.e("Debug", "-1 returned on UC");
                    }
                }

                loadPantry();

            }
        });

        return rootview;
    }

    /*********************************
     FUNCTION:  void updateSpinner(ArrayList<String>)
     PARAMS:    ArrayList of labels to be added to listView
     RETURNS:   nothing
     PURPOSE:   updates the spinner.  Uses ArrayAdapter
     *********************************/
    private void updateSpinner(ArrayList<String> labels) {
        Context context = getActivity();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        itemSelected = parent.getItemAtPosition(position).toString().trim();

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }

    /*********************************
     FUNCTION:  void loadUnitSpinner()
     PARAMS:    NONE
     RETURNS:   Nothing
     PURPOSE:   Uses ArrayAdapter to load unitSpinner
                Data used is R.array.Unit_types_array
     *********************************/
    private void loadUnitSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Unit_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

    }

    /*********************************
     FUNCTION:  void loadPantry();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Loads all pantry items into itemsInPantry
                Uses custom adapter - PantryListViewAdapter
     *********************************/
    public void loadPantry(){
        RecipeActions ra = new RecipeActions(rootview.getContext());
        allIds = new ArrayList<>();             //Array of the ids of the pantry items
        allIds = ra.getAllPantryIds();          //gets the ids
        itemsInPantry = new ArrayList<>();

        ArrayList<String> listContents = new ArrayList<>();
        for(int i=0; i<allIds.size(); i++)
            itemsInPantry.add(ra.getPantryItemById(allIds.get(i)));

       for(int i=0; i<itemsInPantry.size(); i++) {
           listContents.add(itemsInPantry.get(i).getIngredientName() + " "
                   + itemsInPantry.get(i).getAmtAsDisplayed() + " " +
                   itemsInPantry.get(i).getDisplayUnit());
       }
        PantryListViewAdapter adapter = new PantryListViewAdapter(rootview.getContext(),
                android.R.layout.simple_list_item_1, listContents);
        adapter.setCustomButtonListner(myPantry_fragment.this);
        listView.setAdapter(adapter);
    }

    /*********************************
     FUNCTION:  onButtonClickListener(int, String)
     PARAMS:    int - position, String - value
     RETURNS:   nothing
     PURPOSE:   When delete button is pressed pantry item is deleted from pantry
     *********************************/
    @Override
    public void onButtonClickListner(int position, String value) {
        int itemId = itemsInPantry.get(position).getIngredientId();
        handler = new DataHandler(rootview.getContext());
        handler.open();
        handler.deleteFromPantry(itemId);
        Toast.makeText(getActivity(), "Item Deleted Successfully",
                Toast.LENGTH_SHORT).show();
        handler.close();
        loadPantry();
    }
}


