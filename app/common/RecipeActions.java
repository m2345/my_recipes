package com.development.buccola.myrecipes.common;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.development.buccola.myrecipes.database.DataHandler;
import com.development.buccola.myrecipes.pantry.PantryItem;
import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.development.buccola.myrecipes.recipe.Steps;

import java.util.ArrayList;

/***************************************************
 * FILE:        RecipeAction
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15.
 * Purpose:     Common functions performed on recipes
 ***************************************************/
public class RecipeActions {
    Context context;
    DataHandler handler;
    public RecipeActions(Context ctx){
        context = ctx;
    }

    /*********************************
     FUNCTION:  void insertRecipe(String... arg)
     PARAMS:    String variables of the general recipe information
     RETURNS:   Nothing
     PURPOSE:   Insert general recipe info into the sqlite database
     *********************************/
    public void insertRecipe(String... arg){
        for(int i=0; i<arg.length; i++){
            Log.e("FF " + i, arg[i]);
        }

        String passTitle = arg[0];
        String passSource = arg[1];
        int passCook = Integer.parseInt(arg[2]);
        int passPrep = Integer.parseInt(arg[3]);
        String passType = arg[4];
        int passCalPerServing = Integer.parseInt(arg[5]);
        int passServingSize = Integer.parseInt(arg[6]);
        int passTotalServings = Integer.parseInt(arg[7]);

        handler = new DataHandler(context);//getActivity());//getBaseContext());
        handler.open();
        handler.insertRecipe(passTitle, passSource, passPrep, passCook, passType, passServingSize, passTotalServings, passCalPerServing);
        handler.close();

    }

    /*********************************
     FUNCTION:  ArrayList<Integer> getAllPantryIds()
     PARAMS:    none
     RETURNS:   An arrayList of integers
     PURPOSE:   To get the ids of all the pantry items
     NOTES:     Returns an arrayList of all the ids
                After these are returned it is used to load pantry items by id
     *********************************/
    public ArrayList<Integer> getAllPantryIds(){
        ArrayList<Integer> idsArray = new ArrayList<>();
        handler = new DataHandler(context);
        handler.open();
        Cursor C = handler.getAllPantryIds();
        if(C.moveToFirst()) {
            do {
                idsArray.add(Integer.parseInt(C.getString(0)));
            }while(C.moveToNext());
        }
        handler.close();
        return idsArray;
    }

    /*********************************
     FUNCTION:  PantryItem getPantryItemById(int);
     PARAMS:    int - pantryItem id
     RETURNS:   PantryItem object
     PURPOSE:   Get the pantry item by id
     NOTES:     Calls DataHandler and handles Cursor result
     *********************************/
    public PantryItem getPantryItemById(int id){
        PantryItem item = new PantryItem();
        Pantry pantry = new Pantry(context);
        handler = new DataHandler(context);
        handler.open();
        Cursor C = handler.getPantryItemById(id+"");
        if(C.moveToFirst()) {
            do {
                Log.e("handler", "amt " +C.getString(2));
               item.setIngredientId(Integer.parseInt(C.getString(1)));
               item.setAmtAsStored(Double.parseDouble(C.getString(2)));
               item.setPantryItemId(Integer.parseInt(C.getString(0)));
                Log.e("RA Info", "ing id: " + C.getString(1) + " pantry id: " +
                        C.getString(0) + " amt: " + C.getString(2) ) ;
            }while(C.moveToNext());
        }
        handler.close();
        item.setIngredientName(pantry.getIngredientName(item.getIngredientId()).trim());
        item.setDisplayUnit(pantry.getDisplayUnits(item.getIngredientId()).trim());
        item.setDbUnit(pantry.getDBUnits(item.getIngredientId()).trim());
        Log.e("RA ", "DBUNIT: " + item.getDbUnit() + " DISPL: " + item.getDisplayUnit());
        Log.e("RA af DBU", "" + item.getAmtAsDisplayed());
        UnitConversions uc = new UnitConversions(item.getDisplayUnit(), item.getDbUnit(), item.getAmtAsStored());
        Double newAmt = uc.compareUnits();
        Log.e("RA result", "New amt " + newAmt + "");
        if(newAmt != -1) {
            item.setAmtAsDisplayed(uc.compareUnits());
        }else
            Log.e("RA ERROR", "-1 retunred while UC");
        return item;
    }

    /*********************************
     FUNCTION:  boolean addNeededIngredients(ArrayList<Ingredient>);
     PARAMS:    ArrayList of ingredient objects
     RETURNS:   true if ingredient is added successfully; else returns false;
     PURPOSE:   add ingredients to shopping list
     *********************************/
    public boolean addNeededIngredients(ArrayList<Ingredient> ingredients){
        Log.e("Adding", "ok: " + ingredients.size());
        DataHandler handler = new DataHandler(context);
        handler.open();
        Pantry pantry = new Pantry(context);
        for(int i=0; i<ingredients.size(); i++){
            if(!ingredients.get(i).getHasIngredient()){
                int ingredientId = ingredients.get(i).getIngredient_id();
                handler.insertIntoShoppingList(ingredientId, 1, pantry.getDisplayUnits(ingredientId), null);
            }else{
                Log.e("Adding", "We have the ingreinet");
            }
        }
        handler.close();
        return false;

    }
    /*********************************
     FUNCTION:  Recipe getRecipeById(int);
     PARAMS:    int - recipeId
     RETURNS:   Recipe object
     PURPOSE:   Get a recipe by it's id
     NOTES:     Calls DataHandler and handles Cursor result
     *********************************/
    public Recipe getRecipeById(int recipeId){
        Recipe recipe = new Recipe();
        Log.e("Creating", "New recipe");
        handler = new DataHandler(context);
        handler.open();
        Cursor C = handler.getGeneralRecipeInfo(recipeId);
        if(C.moveToFirst()) {
            do{
                recipe.setRecipeId(Integer.parseInt(C.getString(0)));
                recipe.setTitle(C.getString(1));
                recipe.setSource(C.getString(2));
                recipe.setPrep_time(Integer.parseInt(C.getString(3)));
                recipe.setCook_time(Integer.parseInt(C.getString(4)));
                recipe.setMeal_type(C.getString(5));
                recipe.setCalories_per_serving(Integer.parseInt(C.getString(6)));
                recipe.setTotal_servings(Integer.parseInt(C.getString(7)));
                recipe.setServing_size(Integer.parseInt(C.getString(8)));
            }while(C.moveToNext());
        }

        handler = new DataHandler(context);
        handler.open();
        Cursor CC = handler.getIngredientsRecipeInfo(recipeId);
        if(CC.moveToFirst()) {
            do{
                boolean able = hasEnough(CC.getString(1), CC.getString(4));
                Ingredient in = new Ingredient();
                in.setHasIngredient(able);
                in.setRecipe_id(recipeId);
                in.setAmt(Double.parseDouble(CC.getString(4)));
                in.setName(CC.getString(3));
                in.setUnit(CC.getString(5));
                in.setIngredient_id(Integer.parseInt(CC.getString(1)));
                in.setPreperation(CC.getString(6));
                recipe.addIngredients(in);
            }while(CC.moveToNext());
        }

        //loop through ingredients and add display unit
        //if done in side above loop database errors might occur with it being closed at the wrong time
        Pantry pantry = new Pantry(context);
        for(int i=0; i<recipe.getIngredients().size(); i++) {
            int id = recipe.getIngredients().get(i).getIngredient_id();
            String displayUnit = pantry.getDisplayUnits(id);
            recipe.getIngredients().get(i).setDisplayUnit(displayUnit);
        }

        handler = new DataHandler(context);
        handler.open();
        Cursor CCC = handler.getStepsRecipeInfo(recipeId);
        if(CCC.moveToFirst()) {
            do{
                Steps step = new Steps();
                step.setRecipe_id(recipeId);
                step.setStep(CCC.getString(1));
                recipe.addStep(step);
            }while(CCC.moveToNext());
        }

        handler.close();


        return recipe;
    }

    /*********************************
     FUNCTION:  void insertRecipeIngredients(String... args);
     PARAMS:    String variables
     RETURNS:   nothing
     PURPOSE:   inserts recipe ingredients into recipe_ingredients database
     NOTES:     Calls DataHandler and handles Cursor result
     *********************************/
    public void insertRecipeIngredients(String... args){
        handler = new DataHandler(context);
        handler.open();
            int ingredientId = Integer.parseInt(args[0].trim());
            double amt = Double.parseDouble(args[3].trim());
            String name = args[2].trim();
            String units = args[1].trim();
            int recipeId = Integer.parseInt(args[4].trim());
            String prep = args[5].trim();
            Log.e("Test: ", "IngreientId = " + ingredientId + " amt = " + amt + " name = " + name + " units: = " + units);
            handler.insertRecipeIngredients(recipeId, ingredientId, amt, name, units, prep);
        handler.close();
    }

    /*********************************
     FUNCTION:  void insertSteps(String... args);
     PARAMS:    String variables
     RETURNS:   nothing
     PURPOSE:   insert recipe steps in database
     *********************************/
    public void insertSteps(String... args){
        handler = new DataHandler(context);
        handler.open();
        handler.insertSteps(Integer.parseInt(args[0]), args[1]);
        handler.close();
    }

    /*********************************
     FUNCTION:  void deleteRecipe(int);
     PARAMS:    int - recipeId
     RETURNS:   nothing
     PURPOSE:   deletes recipe
     *********************************/
    public void deleteRecipe(int recipeId){
        handler = new DataHandler(context);
        handler.open();
        handler.deleteRecipe(recipeId);
        handler.close();
    }


        /*********************************
         FUNCTION:  boolean makeRecipe(Recipe)
         PARAMS:    Recipe object
         RETUNS:    boolean; true if recipe made successfuy else false
         PURPOSE:   Makes the provided recipe
         Decreases the pantry by the amount of ingredients used.
         Adds items to shopping list if low after ingredient was used.
         NOTES:     Calls Datahandler and handles Cursor result
         *********************************/
        public boolean makeRecipe(Recipe recipe){
            Pantry pantry = new Pantry(context);
            for(int i=0; i<recipe.getIngredients().size(); i++){
                Ingredient current = recipe.getIngredients().get(i);
                double amountUsed = current.getAmt();
                int ingredientId = current.getIngredient_id();
                String displayUnits = pantry.getDisplayUnits(ingredientId);
                int pantryId = pantry.getPantryId(ingredientId);
                double currentAmt = pantry.getPantryAmt(ingredientId);
                double newAmt = currentAmt - amountUsed;
                Log.e("OK", "Current: " + currentAmt + " Used: " + amountUsed);
                handler = new DataHandler(context);
                if(newAmt < 1) {
                    handler.open();
                    handler.insertIntoShoppingList(ingredientId, 1, displayUnits, null);
                    handler.close();
                }

                handler.open();
                Log.e("apntry", pantryId +"");
                boolean error = handler.decreasePantry(pantryId, newAmt);
                handler.close();
                if(error)
                    Toast.makeText(context, "Pantry updated", Toast.LENGTH_LONG).show();
                else Toast.makeText(context, "Error updating pantry", Toast.LENGTH_LONG).show();
            }
            return true;
        }

    /*********************************
     FUNCTION:  boolean hadEnough(String, String, String);
     PARAMS:    String ingredientId, String recipeAmt, String unit
     RETURNS:   boolean - true if enough of given ingredients in pantry,  else false
     PURPOSE:   Checks if the the pantry has enough of the given ingreident and amount
     NOTES:     Calls DataHandler and handles Cursor result
     *********************************/
    private boolean hasEnough(String ingredientId, String recipeAmt) {
        handler = new DataHandler(context);
        handler.open();
        double pantryAmt =0.00;
        Cursor C = handler.getAmt(Integer.parseInt(ingredientId));
        if (C.moveToFirst()) {
            do {
                pantryAmt = Double.parseDouble(C.getString(0).trim());
            } while (C.moveToNext());
        }
        if (C.getCount() == 0)
            pantryAmt = 0.00;
        handler.close();
        if (Double.parseDouble(recipeAmt.trim()) >= pantryAmt)
            return false;
        else
            return true;
    }

    /*********************************
     FUNCTION:  void save(Recipe);
     PARAMS:    Recipe object
     RETURNS:   nothing
     PURPOSE:   Saves the provided recipe
     *********************************/
    public void save(Recipe r){
        String title = r.getTitle();
        String source = r.getSource();
        int cook_time = r.getCook_time();
        int prep_time = r.getPrep_time();
        String meal_type = r.getMeal_type();
        int calories_per_serving = r.getCalories_per_serving();
        int serving_size = r.getServing_size();
        int total_servings = r.getTotal_servings();

        insertRecipe(title, source, cook_time+"", prep_time+"", meal_type, calories_per_serving+"", serving_size+"", total_servings+"");
        int recipeId;
        String idHolder = "";
        DataHandler handler = new DataHandler(context);
        handler.open();
        Cursor C = handler.getRecipeId();
        if(C.moveToFirst()) {
            do{
                idHolder = C.getString(0);
            }while(C.moveToNext());
        }
        ArrayList<Ingredient> ingredients = r.getIngredients();
        ArrayList<Steps> Steps =  r.getSteps();
        recipeId = Integer.parseInt(idHolder);
        if (recipeId >= 0) {
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.get(i).setRecipe_id(recipeId);
                insertRecipeIngredients(ingredients.get(i).getIngredient_id() +"", ingredients.get(i).getUnit(),
                        ingredients.get(i).getName(), ingredients.get(i).getAmt() +"", ingredients.get(i).getRecipe_id() + "",
                        ingredients.get(i).getPreperation());
            }for(int i=0; i<Steps.size(); i++) {
                Steps.get(i).setRecipe_id(recipeId);
                insertSteps(Steps.get(i).getRecipe_id() + "", Steps.get(i).getStep());
            }
        }
    }

    /***********************************************************
     FUNCTION:  String printRecipe(Recipe);
     PARAMS:    Recipe object
     RETURNS:   Returns a string using html markup
     PURPOSE:   Return recipe information to be displayed to the user
     NOTES:     Calls DataHandler and handles Cursor result
     ************************************************************/
    static public String printRecipe(Recipe recipe){
        String servingSize = recipe.getServing_size()+"";
        String prepTime = recipe.getPrep_time()+"";
        String cookTime = recipe.getCook_time()+"";
        String totalServings = recipe.getTotal_servings()+"";
        String caloriesPerServing = recipe.getCalories_per_serving()+"";
        if(servingSize.equals("-1"))
            servingSize = "N/A";
        if(prepTime.equals("-1"))
            prepTime = "N/A";
        if(cookTime.equals("-1"))
            cookTime = "N/A";
        if(caloriesPerServing.equals("-1"))
            caloriesPerServing = "N/A";
        if(totalServings.equals("-1"))
            totalServings = "N/A";

        String output = "Title: " + recipe.getTitle() + "<br>" +
                "Source: " + recipe.getSource() + "<br>" +
                "Serving Size: " +servingSize + "<br>" +
                "Total Servings: " + totalServings + "</br>" +
                "Calories: " + caloriesPerServing + "<br>" +
                "Prep Time: " + prepTime + "<br>" +
                "Cook Time: " + cookTime + "<br>" +
                "<br><br>Ingredients: " + "<br>";
                    for(int i =0; i<recipe.getIngredients().size(); i++){
                        Ingredient in = recipe.getIngredients().get(i);
                        String preparation = in.getPreperation();
                        if(preparation.equals("-1") || preparation.equals("none"))
                            preparation = "";
                        String info = in.getAmt() + " " + preparation +" " + in.getName() + " " + in.getUnit() + "<br>";
                        Log.e("HAS", in.getHasIngredient() + " ");
                        if(!in.getHasIngredient())
                            output += "<font color=red>" + info + "</font>";
                        else
                            output += info;
                    }

               output +=  "<br><br>Steps: <br>";
                    for(int i=0; i<recipe.getSteps().size(); i++){
                        output += recipe.getSteps().get(i).getStep() + "<br>";
                    }

        return output;
    }

    /***********************************************************
     FUNCTION:  ArrayList<Integer> getAllRecipeIds();
     PARAMS:    none
     RETURNS:   ArrayList of integers - all the reicpe ids
     PURPOSE:   To get all the recipe ids in the database;
                After this is returned it is used to get all the recipes in the database
     NOTES:     Calls DataHandler and handles Cursor result
     ************************************************************/
    public ArrayList<Integer> getAllRecipeIds(){
        ArrayList<Integer> list = new ArrayList<>();
        handler = new DataHandler(context);
        handler.open();
        Cursor C = handler.getAllRecipeIds();
        if (C.moveToFirst()) {
            do {
              list.add(Integer.parseInt(C.getString(0)));
            } while (C.moveToNext());
        }
        handler.close();
        return list;
    }

    /***********************************************************
     FUNCTION:  void deleteShared(Recipe);
     PARAMS:    Recipe object
     RETURNS:   nothing
     PURPOSE:   deleteShared recipe after user saves it or deletes it
     NOTES:     gets the URL of the file from the given recipe
     ************************************************************/
    public void deleteShared(Recipe recipe){
        String jsonURL = recipe.getSharedUrl();
        new SharedRecipeSaved().execute(jsonURL);
    }

    /***********************************************************
     FUNCTION:  boolean ableToMake(Recipe);
     PARAMS:    Recipe object
     RETURNS:   boolean - true if user has all ingredients to make recipe else returns false
     PURPOSE:   Returns whether or not the user has the needed ingredients and needed amount in the pantry
     ************************************************************/
    public boolean ableToMake(Recipe recipe){
        for(int i=0; i<recipe.getIngredients().size(); i++)
            if(!recipe.getIngredients().get(i).getHasIngredient())
                return false;
        return true;
    }
}