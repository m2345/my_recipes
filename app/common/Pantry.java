package com.development.buccola.myrecipes.common;
import android.content.Context;
import android.database.Cursor;

import com.development.buccola.myrecipes.database.DataHandler;

/***************************************************
 * FILE:        Pantry
 * PROGRAMMER:  Megan Buccola
 * CREATED:     4/21/15.
 * Purpose:     common function for pantry
 ***************************************************/
public class Pantry {
    Context ctx;
    DataHandler handler;

    public Pantry(Context context){
        this.ctx = context;
    }


    /*********************************
     FUNCTION:  double getPantryAmt(int)
     PARAMS:    int- the ingredient id
     RETURNS:   double - amount in pantry
     PURPOSE:   Checks the amount in the pantry for the given ingredient
     *********************************/
    public double getPantryAmt(int ingredientId){
        double amt = 0;
        handler = new DataHandler(ctx);
        handler.open();
        Cursor C = handler.getPantryAmt(ingredientId);
        if(C.moveToFirst()){
            do{
                amt= Double.parseDouble(C.getString(0));
            }while(C.moveToNext());
        }
        handler.close();
        return amt;
    }

    /*********************************
     FUNCTION:  int getPantryId(int)
     PARAMS:    int - ingredientId
     RETUNS:    returns an integer - The id unique id of the pantry item
     PURPOSE:   get the unique id of the ingredient of the pantry.
     *********************************/
    public int getPantryId(int ingredientId){
        int id = -1;
        handler = new DataHandler(ctx);
        handler.open();
        Cursor C = handler.getPantryId(ingredientId);
        if(C.moveToFirst()){
            do{
                id= Integer.parseInt(C.getString(0));
            }while(C.moveToNext());
        }
        handler.close();
        return id;

    }

    /*********************************
     FUNCTION:  String getIngredientName(int)
     PARAMS:    int - ingredientId
     RETURNS:   String - name of the given ingredient
     PURPOSE:   Get the name of the provided ingredient
     *********************************/
    public String getIngredientName(int ingredientId){
        String name = "";
        handler = new DataHandler(ctx);
        handler.open();
        Cursor C = handler.getIngredientName(ingredientId);
        if(C.moveToFirst()){
            do{
                name = C.getString(0);
            }while(C.moveToNext());
        }
        handler.close();
        return name;
    }

    /*********************************
     FUNCTION:  String getDisplayUnits(int)
     PARAMS:    int - ingredinetId
     RETURNS:   String - Display Units
     PURPOSE:   Get the units of the given ingredient to be displayed to the user in the pantry
     *********************************/
    public String getDisplayUnits(int ingredientId){
        String units = null;
        handler = new DataHandler(ctx);
        handler.open();

        Cursor C = handler.getDisplayUnits(ingredientId);
        if(C.moveToFirst()){
            do{
                units = C.getString(0);
            }while(C.moveToNext());
        }

            handler.close();
        return units;
    }

    /*********************************
     FUNCTION:  String getDBUnits(int)
     PARAMS:    int - ingredientId
     RETUNS:    String - dbUnits
     PURPOSE:   Returns the units of the given ingredient that is stored in the database
     *********************************/
    public String getDBUnits(int ingredientId){
        String units = "";
        boolean failOpen = false;
        try {
            handler = new DataHandler(ctx);
            handler.open();
        }catch(IllegalStateException e){
            failOpen = true;
        }
        Cursor C = handler.getDBUnits(ingredientId);
        if(C.moveToFirst()){
            do{
                units = C.getString(0);
            }while(C.moveToNext());
        }
        if(!failOpen)
            handler.close();
        return units;
    }
}
