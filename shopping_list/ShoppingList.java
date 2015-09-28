package com.development.buccola.myrecipes.shopping_list;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.development.buccola.myrecipes.common.Pantry;
import com.development.buccola.myrecipes.common.UnitConversions;
import com.development.buccola.myrecipes.database.DataHandler;
import com.example.megan.myapplication.R;

import java.util.ArrayList;

/***************************************************
 * FILE:        ShoppingList
 * PROGRAMMER:  Megan Buccola
 * CREATED:     4/21/15
 * PURPOSE:     Shopping List Fragment
 * EXTENDS:     Fragment
 * IMPLEMENTS:  ListViewAdapterShopping.customButtonListener
 *              ListViewAdapterShopping2.customButtonListener
 *              AdapterView.OnItemSelectedListener
 * LAYOUT:      shopping_list
 ***************************************************/

public class ShoppingList extends Fragment implements ListViewAdapterShopping.customButtonListener, ListViewAdapterShopping2.customButtonListener, AdapterView.OnItemSelectedListener {
    View rootView;
    ListView listView, checkedItemsListView;
    ArrayList<String> shoppingListArray, CheckedListArray;
    ArrayList<String[]> spinnerMap, itemInfo, checkedItemInfo;
    DataHandler handler;
    Spinner spinnerIngredients, spinnerUnits;
    EditText searchText, amt;
    String unitSelected;
    Button deleteCheckedItems;
    int ingredientIdSelected;
    ImageButton add;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shopping_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvShoppingList);
        checkedItemsListView = (ListView) rootView.findViewById(R.id.lvCheckedItems);
        spinnerIngredients = (Spinner) rootView.findViewById(R.id.spinnerIngredients);
        spinnerUnits = (Spinner) rootView.findViewById(R.id.spinnerUnits);
        searchText = (EditText) rootView.findViewById(R.id.searchText);
        amt = (EditText) rootView.findViewById(R.id.amt);
        add = (ImageButton) rootView.findViewById(R.id.buttonAdd);
        deleteCheckedItems = (Button) rootView.findViewById(R.id.deleteCheckedItems);
        ingredientIdSelected = 0;
        shoppingListArray = new ArrayList<>();
        CheckedListArray = new ArrayList<>();
        itemInfo = new ArrayList<>();
        checkedItemInfo = new ArrayList<>();
        unitSelected = "";

        spinnerIngredients.setOnItemSelectedListener(this);
        loadShoppingListInfo();
        loadListView();

        //onclick
        deleteCheckedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Button Delete");
                int itemId, listId;
                String unit;
                double amt;
                //loop through checked arrays
                //get amt, unit, and item id
                //add items to pantry
                //delete items from checked array
                if(checkedItemInfo.size() == 0)
                    Toast.makeText(getActivity(), "No Items Have Been Crossed Off", Toast.LENGTH_LONG).show();

                for(int i=0; i< checkedItemInfo.size(); i++){
                    itemId = Integer.parseInt(checkedItemInfo.get(i)[0]);
                    amt = Double.parseDouble(checkedItemInfo.get(i)[2]);
                    unit = checkedItemInfo.get(i)[3];
                    listId = Integer.parseInt(checkedItemInfo.get(i)[4]);
                    //add these items to the pantry
                    handler = new DataHandler(rootView.getContext());
                    handler.open();
                    //Convert amt to correct unit
                    Pantry pantry = new Pantry(rootView.getContext());
                    String dbUnit = pantry.getDBUnits(itemId);
                    Log.e("", "DB UNIT: " + dbUnit + " unit on list: " + unit);

                    UnitConversions unitConvert = new UnitConversions(dbUnit.trim(), unit.trim(), amt);
                    amt = unitConvert.compareUnits();

                    //check if already existing item in pantry
                    if(existingItem(itemId)) {
                        amt += getAmount(itemId);
                        handler.updatePantry(amt, itemId);
                    }else {
                        Log.e("ShoppingList add", amt + "");
                        handler.insertIntoPantry(amt, itemId);
                    }
                    //delete from shoppingList
                    handler.deleteShoppingListItem(listId);
                    handler.close();

                    //create empty arrayLists
                    checkedItemInfo = new ArrayList<>();
                    CheckedListArray = new ArrayList<>();

                    //reload listviews
                    loadListView();
                }

            }
        });

        /* Text Change Listener */
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = searchText.getText().toString();
                String getName;
                String getUnit;
                String getId;
                handler = new DataHandler(rootView.getContext());
                handler.open();
                Cursor C = handler.searchIngredients(search);
                ArrayList<String> output = new ArrayList<>();
                spinnerMap = new ArrayList<>();
                if (C.moveToFirst()) {
                    do {
                        getId = C.getString(0); //if used parse to int
                        getUnit = C.getString(2);
                        getName = C.getString(1);
                        output.add(getName);
                        String[] metaToAdd = {getId, getUnit, getName};
                        spinnerMap.add(metaToAdd);
                    } while (C.moveToNext());
                }
                handler.close();
                updateIngredientSpinner(output);
            }
        });

        loadUnitSpinner();
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unitSelected = spinnerUnits.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

         add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amtInput = amt.getText().toString();
                if (!amtInput.equals("") || !unitSelected.equals("") || ingredientIdSelected != -1) {
                    Log.e("ShoppingList", "Button Pressed");
                    handler = new DataHandler(v.getContext());//getActivity());//getBaseContext());
                    handler.open();
                    handler.insertIntoShoppingList(ingredientIdSelected, Double.parseDouble(amtInput.trim()), unitSelected.trim(), null);
                    handler.close();
                    loadShoppingListInfo();  //need to access the DB to fill arrayLists
                    loadListView();          //display the lists
                } else {
                    Log.e("ShoppingList", amtInput + " " + unitSelected + " " + ingredientIdSelected);
                    Toast.makeText(getActivity(), "Please fill out all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    public void loadListView(){
        ListViewAdapterShopping adapter = new ListViewAdapterShopping(rootView.getContext(),
                android.R.layout.simple_list_item_1, shoppingListArray);
        ListViewAdapterShopping2 adapter2 = new ListViewAdapterShopping2(rootView.getContext(),
                android.R.layout.simple_list_item_1, CheckedListArray);

        adapter.setCustomButtonListener(ShoppingList.this);
        adapter2.setCustomButtonListener(ShoppingList.this);

        listView.setAdapter(adapter);
        checkedItemsListView.setAdapter(adapter2);
    }


    /*********************************
     FUNCTION:  void loadUnitSpinner()
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   loads unit spinner.  Uses ArrayAdapter and R.array.Unit_types_array
     *********************************/
    private void loadUnitSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Unit_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);

    }

    /*********************************
     FUNCTION:  void loadShoppingListInfo();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Loads Shoppinh list arrays
     *********************************/
    public void loadShoppingListInfo(){
        Pantry pantry = new Pantry(rootView.getContext());
        handler = new DataHandler(rootView.getContext());
        handler.open();
        shoppingListArray = new ArrayList<>();
        itemInfo = new ArrayList<>();
        itemInfo = new ArrayList<>();
        Cursor C = handler.getShoppingList();
        if(C.moveToFirst()) {
            do{
                String itemName = pantry.getIngredientName(Integer.parseInt(C.getString(1)));
                String amount =  C.getString(2);
                String unit =  C.getString(3);
                String ingredientId = C.getString(1);
                String shoppingListId = C.getString(0);
                Log.e("Shopping Info", "amt: " + C.getString(2) + " unit: " + C.getString(3) + " ing-Id: " + C.getString(1) + " itemId: " + C.getString(0));
                shoppingListArray.add(itemName + ", " + amount + " " + unit);
                String info[] = {ingredientId, itemName, amount, unit, shoppingListId};
                itemInfo.add(info);
            }while(C.moveToNext());
        }
        handler.close();
    }

    /*********************************
     FUNCTION:  updateIngredientSpinner(ArrayList<String>)
     PARAMS:    ArrayList of items to populate spinner
     RETURNS:   nothing
     PURPOSE:   update ingredient spinner with items after search
                Uses ArrayAdapter
     *********************************/
    private void updateIngredientSpinner(ArrayList<String> labels) {
        Context context = getActivity();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, labels);
         dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinnerIngredients.setAdapter(dataAdapter);
    }

    /*********************************
     FUNCTION:  void onItemSelected(AdapterView<?> , View , int , long )
     PARAMS:    parent, view, position and id
     RETURNS:   nothing
     PURPOSE:   ingredient selected set ingredintIdSelected
     *********************************/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        int indexId = getHash(label);
        if(indexId >= 0){
            String itemId = spinnerMap.get(indexId)[0];
            ingredientIdSelected = Integer.parseInt(itemId);
        }
        else ingredientIdSelected = -1;

    }

    /*********************************
     FUNCTION:  int getHash(String)
     PARAMS:    String label
     RETURNS:   int - index of selected item
     PURPOSE:   get index from hash
     *********************************/
    private int getHash(String label){
        for(int i=0; i<spinnerMap.size(); i++){
            if(spinnerMap.get(i)[2].equals(label)){
                return i;
            }else{
                Log.d("Label: ", label + " " + spinnerMap.get(0)[2]);
            }
        }
        return -1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*********************************
     FUNCTION:  void onSelectedCrossOff(int, String)
     PARAMS:    int - position, String - value
     RETURNS:   nothing
     PURPOSE:   Cross item off of shopping list.
                move item to checked arrays
     *********************************/
    @Override
    public void onSelectedCrossOff(int position, String value) {
        String text = shoppingListArray.get(position);
        String itemName = itemInfo.get(position)[1];
        String itemId = itemInfo.get(position)[0];
        String amount = itemInfo.get(position)[2];
        String unit = itemInfo.get(position)[3];
        String listId = itemInfo.get(position)[4];
        //Move items to checked arrays
        //CheckedIndexArray.add(index);
        checkedItemInfo.add(new String[]{itemId, itemName, amount , unit, listId});
        CheckedListArray.add(text);

        //Delete items from ingredient arrays using the position.
       // listIngredientIdArray.remove(position);
        itemInfo.remove(position);
        shoppingListArray.remove(position);

        //reload lists;
        loadListView();
    }

    /*********************************
     FUNCTION:  void removeFromList(int, String)
     PARAMS:    int - position, String - value
     RETURNS:   nothing
     PURPOSE:   Removes item from shopping list
     *********************************/
    @Override
    public void removeFromList(int position, String value) {
        String listId = itemInfo.get(position)[4];
        handler = new DataHandler(rootView.getContext());
        handler.open();
        handler.deleteShoppingListItem(Integer.parseInt(listId));
        handler.close();
        shoppingListArray.remove(position);
        itemInfo.remove(position);
        loadListView();
    }

    /*********************************
     FUNCTION:  void onSelectedAddBackToList(int, String)
     PARAMS:    int - position, String value
     RETURNS:   nothing
     PURPOSE:   moves checked off item back to shopping list
     *********************************/
    @Override
    public void onSelectedAddBackToList(int position, String value) {
         //opposite of onSelected();
        //Get index of ingredient being removed
        //int index = Integer.parseInt(checkedItemInfo.get(position)[0]);
        String text = CheckedListArray.get(position);
        String itemName = checkedItemInfo.get(position)[1];
        String itemId = checkedItemInfo.get(position)[0];
        String amount = checkedItemInfo.get(position)[2];
        String unit = checkedItemInfo.get(position)[3];
        String listId = checkedItemInfo.get(position)[4];
        //Move items to checked arrays
        itemInfo.add(new String[]{itemId, itemName, amount, unit, listId});
        //listIngredientIdArray.add(index);
        shoppingListArray.add(text);

        //Delete items from ingredient arrays using the position.
        //CheckedIndexArray.remove(position);
        checkedItemInfo.remove(position);
        CheckedListArray.remove(position);

        //reload lists;
        loadListView();

    }

    /*********************************
     FUNCTION:  double getAmount(int)
     PARAMS:    int ingredient id
     RETURNS:   double - amount
     PURPOSE:   get Amount in pantry of given ingredient
     *********************************/
    private double getAmount(int id){
        Cursor C = handler.getAmt(id);
        String amount = "";
        double return_;
        if(C.getCount() > 0){
            if(C.moveToFirst()) {
                do{
                    amount = C.getString(0);

                }while(C.moveToNext());
            }
        }
        try{
            return_ = Double.parseDouble(amount);
        }catch(Exception e) {
            return -1;
        }
        return return_;
    }

    /*********************************
     FUNCTION:  boolean existingItem(int)
     PARAMS:    int ingredientId
     RETURNS:   booelan - true if item already exists. False if doesn't exist
     PURPOSE:   Check if item is already in pantry
     *********************************/
    private boolean existingItem(int id){
        boolean result = false;
        Cursor C = handler.existingPantryItem(id);
        if(C.getCount() > 0){
            result = true;
        }
        return result;
    }

}