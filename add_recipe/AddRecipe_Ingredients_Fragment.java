package com.development.buccola.myrecipes.add_recipe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

import com.development.buccola.myrecipes.common.UnitConversions;
import com.development.buccola.myrecipes.database.DataHandler;
import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.example.megan.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***************************************************
 * FILE:        AddRecipe_Ingredients_Fragment
 *              AddIngredientListAdapter
 * PROGRAMMER:  Megan Buccola
 * CREATED:     3/26/15.
 * PURPOSE:     Fragmnet for user to add ingredients
 * IMPLEMENTS:  getRecipeData
 *              AdapterView.OnItemSelectedListener
 *              AddIngredientListAdapter.customButtonListener
 * EXTENDS:     Fragment
  ***************************************************/

public class AddRecipe_Ingredients_Fragment extends Fragment implements AdapterView.OnItemSelectedListener, AddIngredientListAdapter.customButtonListener,GetRecipeData {
    EditText searchTerm;
    EditText amtInput;
    static ArrayList<Ingredient> ingredientList;  //What is send to getRecipeData on save recipe
    ArrayList<String[]> spinnerMap;
    String currentType, unitSelected, selectedPrep;
    Spinner spinnerIngredients, unitSpinner, prepSpinner;
    ImageButton addIngredient;
    DataHandler handler;
    Ingredient newIng;
    ListView ingredientListView;
    View ingredientsView;
    ArrayList<String> outputList;

    static GetRecipeData SM;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ingredientsView = inflater.inflate(R.layout.add_recipe_ingredients_fragment, container, false); //ios_frag
        //((TextView)ingredients.findViewById(R.id.textView)).setText("Ingredients");

        searchTerm = (EditText) ingredientsView.findViewById(R.id.etSearchIngred);
        spinnerIngredients = (Spinner) ingredientsView.findViewById(R.id.spinnerIngredients);
        unitSpinner = (Spinner) ingredientsView.findViewById(R.id.unitSpinner);
        prepSpinner = (Spinner) ingredientsView.findViewById(R.id.spinnerPrep);
        spinnerIngredients.setOnItemSelectedListener(this);
        addIngredient = (ImageButton) ingredientsView.findViewById(R.id.addIngredient);
        amtInput = (EditText) ingredientsView.findViewById(R.id.etAmt);
        currentType = "breakfast";
        ingredientList = new ArrayList<>(); //initialize
        ingredientListView = (ListView) ingredientsView.findViewById(R.id.ingredientListView);
        loadUnitSpinner();
        loadPrepSpinner();


        prepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPrep = prepSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unitSelected = unitSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double newAmt, _amt;
                String dbUnits;
                String amt = amtInput.getText().toString().trim();

                if(amt=="" || amt.isEmpty())
                    Toast.makeText(ingredientsView.getContext(), "Please enter an amount", Toast.LENGTH_LONG).show();

               else {
                    dbUnits = getDBUnits(newIng.getIngredient_id());
                    _amt = Double.parseDouble(amt);

                    UnitConversions compare = new UnitConversions(dbUnits, unitSelected, _amt);
                    newAmt = compare.compareUnits();
                    //newIng.set
                    if (newAmt == -1)
                        newIng.setAmt(_amt);
                    else
                        newIng.setAmt(newAmt);


                    newIng.setPreperation(selectedPrep);
                    ingredientList.add(newIng);
                    newIng = new Ingredient();
                    //TODO Clear all fields
                    loadIngredientList();

                }
            }
        });


        searchTerm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = searchTerm.getText().toString();
                String getName;
                String getUnit;
                String getId;
                handler = new DataHandler(ingredientsView.getContext());
                handler.open();
                Cursor C = handler.searchIngredients(search);
                ArrayList<String> output = new ArrayList<String>();
                spinnerMap = new ArrayList<>();
                if (C.moveToFirst()) {
                    do {
                        getId = C.getString(0);
                        getUnit = C.getString(2);
                        getName = C.getString(1);
                        output.add(getName);
                        String[] metaToAdd = {getId, getUnit, getName};
                        spinnerMap.add(metaToAdd);
                    } while (C.moveToNext());
                }
                handler.close();
                updateSpinner(output);
            }
        });

        return ingredientsView;
    }

   /*********************************
    FUNCTION:  void loadIngredientList()
    PARAMS:    NONE
    RETUNS:    nothing
    PURPOSE:   Loads list view of added ingredients
    NOTES:     Takes teh ingreientList and adds the items to outputList<String> for value of the adapter.
                Uses a custom adapter for the listview - AddIngredientListAdapter
    *********************************/
    private void loadIngredientList(){
        outputList = new ArrayList<>();
        Log.e("ingredientList", ingredientList.size() + " ");
        for(int i=0; i<ingredientList.size(); i++)
            outputList.add(ingredientList.get(i).getAmt() + " " + ingredientList.get(i).getUnit() + " of " + ingredientList.get(i).getName());

        AddIngredientListAdapter adapter = new AddIngredientListAdapter(ingredientsView.getContext(),
                android.R.layout.simple_list_item_1, outputList);
        adapter.setCustomButtonListener(AddRecipe_Ingredients_Fragment.this);
        ingredientListView.setAdapter(adapter);
    }

    /*********************************
     FUNCTION:  void loadPrepSpinner()
     PARAMS:    NONE
     RETUNS:    nothing
     PURPOSE:   Loads preperation spinner items.
     NOTES:     Uses ArrayAdapter.  Items are added using the
                string array R.array.prepare_array
     *********************************/
    private void loadPrepSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.prepare_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prepSpinner.setAdapter(adapter);
    }

    /*********************************
     FUNCTION:  void loadUnitSpinner()
     PARAMS:    NONE
     RETUNS:    nothing
     PURPOSE:   Loads unit spinner items.
     NOTES:     Uses ArrayAdapter.  Items are added using the
     string array R.array.Unit_types_array
     *********************************/
    private void loadUnitSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Unit_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

    }

    /*********************************
     FUNCTION:  void getHash(String)
     PARAMS:    String the chosen item name/label
     RETURNS:    an int / the index of the corresponding text/label
     PURPOSE:   Return index of match array.
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

    /*********************************
     FUNCTION:  void updateSpinner(ArrayList<String>
     PARAMS:    An arrayList of Strings to be added to the spinner
     RETUNS:    returns nothing
     PURPOSE:   updated ingredients spinner.
     NOTES:     This is called onTextChange when user is searching for an ingredient
                uses ArrayAdapter<String>
     *********************************/
    private void updateSpinner(ArrayList<String> labels) {
        Context context = getActivity();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIngredients.setAdapter(dataAdapter);
    }

    /*********************************
     FUNCTION:  String getDBUnits(int itemId)
     PARAMS:    integer of the itemId
     RETUNS:    A string -- the units stored in the database for given ingredient
     PURPOSE:   Gets the dbUnits of an item
     *********************************/
    public String getDBUnits(int itemId){
        String units = null;
        handler = new DataHandler(ingredientsView.getContext());
        handler.open();
        Cursor C = handler.getIngredientUnit(itemId);
        if(C.moveToFirst()) {
            do{
                units = C.getString(0);
            }while(C.moveToNext());
        }
        handler.close();
        return units;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newIng = new Ingredient();  //TODO is this needed as also done when a new ingredient is added??
        String label = parent.getItemAtPosition(position).toString();
        int indexId = getHash(label);
        if(indexId >= 0) {
            String itemId = spinnerMap.get(indexId)[0];
            String itemUnit = spinnerMap.get(indexId)[1];
            newIng.setIngredient_id(Integer.parseInt(itemId));
            newIng.setUnit(itemUnit);
            newIng.setName(label);
        }
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

    public void requestMade(){
        SM.sendArrayData(ingredientList);
    }

    @Override
    public void RequestForGeneral() {

    }

    @Override
    public void RequestForIngredients() {

    }

    @Override
    public void sendData(Recipe data) {

    }

    @Override
    public void sendArrayData(ArrayList<Ingredient> data) {

    }

    @Override
    public void onDeleteButton(int position, String value) {
        //delete item from arrayList and reload listView();
        ingredientList.remove(position);
        loadIngredientList();
    }
}

/***************************************************
 * FILE = AddIngredientListAdapter extends ArrayAdapter<String>
 * PROGRAMMER = Megan Buccola
 * CREATED: 3/26/15.
 * PURPOSE: Adapter for the list view of added ingredients
 * NOTES: uses a custom Button Listener
 ***************************************************/
class AddIngredientListAdapter extends ArrayAdapter<String> {
    customButtonListener customListener;
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public class ViewHolder{
        ImageButton imageButton;
        TextView text;

    }
    public AddIngredientListAdapter(Context context, int textViewResourceId,
                               List<String> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.child_listview_ingredients, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.childTextView);
            viewHolder.imageButton = (ImageButton) view.findViewById(R.id.childDelete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onDeleteButton(position, temp);
                }

            }
        });
        return view;
    }
    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }
    public interface customButtonListener {
        void onDeleteButton(int position,String value);
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

}