package com.development.buccola.myrecipes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.megan.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by megan on 3/23/15.
 *
 */
public class DataHandler {
    public static String DB_PATH = "/data/data/YOUR_PACKAGE/databases";
    public static final String NAME = "name";
    public static final String SOURCE = "source";
    public static final String PREP = "prep_time";
    public static final String COOK = "cook_time";
    public static final String MEALTYPE = "meal_type";
    public static final String UNITS = "unit";
    public static final String AMT = "amt";
    public static final String ITEM_ID = "item_id";

    //recipe
    public static final String SERVING_SIZE = "serving_size";
    public static final String SERVINGS_PER_RECIPE = "total_servings";
    public static final String CALORIES_PER_SERVING = "calories_per_serving";
    public static final String TITLE = "title";
    public static final String STATUS = "status";
    public static final String TOTAL_SERVINGS = "total_servings";

    //steps
    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP = "step";

    //recipeIngredients
    public static final String INGREDIENT_ID = "ingredient_id";

    public static final String TABLE_NAME_RECIPES = "recipes";
    public static final String TABLE_NAME_INGREDIENTS = "ingredients";
    public static final String TABLE_NAME_PANTRY = "pantry";
    public static final String TABLE_NAME_STEPS = "steps";
    public static final String TABLE_NAME_REC_INGREDIENTS = "recipe_ingredients";
    public static final String TABLE_NAME_SHOPPING_LIST = "shopping_list";
   // public static final String TABLE_NAME_CREATE = "create";

    public static final String DB_NAME = "myDB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_RECIPES_CREATE =
            "create table recipes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title text not null, " +
                "source text not null, " +
                "prep_time integer not null, " +
                "cook_time integer not null," +
                "meal_type text not null," +
                "calories_per_serving integer not null," +
                "total_servings integer not null," +
                "serving_size integer not null,"+
                "status text not null" +
            ");";
    public static final String TABLE_STEPS_CREATE =
            "create table steps(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "step text not null, " +
                "recipe_id id not null" +
            ");";

    public static final String TABLE_RECIPE_INGREDIENTS_CREATE =
        "create table recipe_ingredients(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ingredient_id integer not null, " +
            "recipe_id integer not null, " +
            "name text not null, " +
            "amt real not null, " +
            "unit text not null, " +
                " prepare text not null " +
        ");";

    public static final String TABLE_INGREDIENTS_CREATE =
            "create table if not exists ingredients(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name text not null, " +
                "unit text not null, " +
                "unitDisplay text not null, " +
                    "category text" +
            ");";

    public static final String TABLE_PANTRY_CREATE =
            "create table if not exists pantry(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "item_id integer not null, " +
                    "amt real not null" +
             ");";

    public static final String TABLE_SHOPPING_LIST_CREATE =
            "create table if not exists shopping_list(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "item_id integer not null, " +
                    "amt real not null, " +
                    "unit text not null, " +
                    "notes text not null" +
                    ");";

    DBHelper dbhelper;
    static Context ctx, context;
    SQLiteDatabase db;

    public DataHandler(Context ctx){
        this.ctx = ctx;
        this.context = ctx;
        dbhelper = new DBHelper(ctx);
    }

    private static class DBHelper extends SQLiteOpenHelper{

       public DBHelper(Context ctx){
            super(ctx, DB_NAME, null, DB_VERSION);  //SUPER USES SQL OPENHELPER (EXTENDS)
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_PANTRY_CREATE);
            db.execSQL(TABLE_INGREDIENTS_CREATE);
            db.execSQL(TABLE_STEPS_CREATE);
            db.execSQL(TABLE_RECIPES_CREATE);
            db.execSQL(TABLE_RECIPE_INGREDIENTS_CREATE);
            db.execSQL(TABLE_SHOPPING_LIST_CREATE);
            try {
                String str;
                String output= "";
                String[] colArray;
                String name, unitsDB, unitsDisplay, category;

                InputStream is = context.getResources().openRawResource(R.raw.load);//this.getResources().openRawResource(R.drawable.load);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                if (is!=null) {
                    while ((str = reader.readLine()) != null) {
                        colArray = str.split(",");
                        if(colArray.length == 5) {
                            name = colArray[1].trim();
                            unitsDB = colArray[2].trim();
                            unitsDisplay = colArray[3].trim();
                            category = colArray[4].trim();
                            ContentValues content = new ContentValues();
                            content.put(NAME, name);
                            content.put(UNITS, unitsDB);
                            content.put("unitDisplay", unitsDisplay);
                            content.put("category", category);
                            db.insertOrThrow(TABLE_NAME_INGREDIENTS, null, content);
                        }else Log.e("DB", "Insertion error");
                    }
                }
                is.close();
                Toast.makeText(context, output, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "Erorr adding ingredients to the database", Toast.LENGTH_LONG).show();
            }

            //Insert default Recipe hamburgers
            ContentValues content = new ContentValues();
            content.put(TITLE, "Hamburgers");
            content.put(SOURCE, "My Recipes");
            content.put(PREP, "2");
            content.put(COOK, "10");
            content.put(MEALTYPE, "dinner");
            content.put(SERVING_SIZE, "1");
            content.put(SERVINGS_PER_RECIPE, "4");
            content.put(CALORIES_PER_SERVING, "-1");
            content.put(STATUS, "YES");
            db.insertOrThrow(TABLE_NAME_RECIPES, null, content);

            content = new ContentValues();
            content.put(NAME, "hamburger buns");  //key value pair?
            content.put(RECIPE_ID, "1");
            content.put(INGREDIENT_ID, "706");
            content.put(AMT, "4");
            content.put(UNITS, "each");
            content.put("prepare", "-1");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(NAME, "beef");  //key value pair?
            content.put(RECIPE_ID, "1");
            content.put(INGREDIENT_ID, "707");
            content.put(AMT, "1");
            content.put(UNITS, "lbs");
            content.put("prepare", "-1");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(STEP, "form beef into hamburger pattys");  //key value pair?
            content.put(RECIPE_ID, "1");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "Place hamburger pattys on the grill or stove");  //key value pair?
            content.put(RECIPE_ID, "1");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            //Recipe Tacos
            //TODO no total servings?
            content = new ContentValues();
            content.put(TITLE, "Tacos");
            content.put(SOURCE, "My Recipes");
            content.put(PREP, "2");
            content.put(COOK, "20");
            content.put(MEALTYPE, "dinner");
            content.put(SERVING_SIZE, "2");
            content.put(SERVINGS_PER_RECIPE, "4");
            content.put(CALORIES_PER_SERVING, "-1");
            content.put(STATUS, "YES");
            db.insertOrThrow(TABLE_NAME_RECIPES, null, content);

            content = new ContentValues();
            content.put(NAME, "beef");  //key value pair?
            content.put(RECIPE_ID, "1");
            content.put(INGREDIENT_ID, "707");
            content.put(AMT, "2");
            content.put(UNITS, "lbs");
            content.put("prepare", "ground");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(NAME, "taco shells");  //key value pair?
            content.put(RECIPE_ID, "2");
            content.put(INGREDIENT_ID, "624");
            content.put(AMT, "4");
            content.put(UNITS, "each");
            content.put("prepare", "none");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(NAME, "lettuce greenleaf");  //key value pair?
            content.put(RECIPE_ID, "2");
            content.put(INGREDIENT_ID, "353");
            content.put(AMT, "1");
            content.put(UNITS, "lbs");
            content.put("prepare", "ground");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(NAME, "cheddar Cheese");  //key value pair?
            content.put(RECIPE_ID, "2");
            content.put(INGREDIENT_ID, "7");
            content.put(AMT, "1");
            content.put(UNITS, "cups");
            content.put("prepare", "none");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(STEP, "Ground the beef");  //key value pair?
            content.put(RECIPE_ID, "2");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "Heat up taco shells");  //key value pair?
            content.put(RECIPE_ID, "2");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "Add Taco Seasoning to beef");  //key value pair?
            content.put(RECIPE_ID, "2");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "Scoop ground beef into taco shells");  //key value pair?
            content.put(RECIPE_ID, "2");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "Add cheese and lettuce to tacos if desired");  //key value pair?
            content.put(RECIPE_ID, "2");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);


            //New Recipe Hotdogs
            content = new ContentValues();
            content.put(TITLE, "Hot Dogs");
            content.put(SOURCE, "My Recipes");
            content.put(PREP, "0");
            content.put(COOK, "10");
            content.put(MEALTYPE, "dinner");
            content.put(SERVING_SIZE, "1");
            content.put(SERVINGS_PER_RECIPE, "4");
            content.put(CALORIES_PER_SERVING, "-1");
            content.put(STATUS, "YES");
            db.insertOrThrow(TABLE_NAME_RECIPES, null, content);

            content = new ContentValues();
            content.put(NAME, "hotdog");  //key value pair?
            content.put(RECIPE_ID, "3");
            content.put(INGREDIENT_ID, "209");
            content.put(AMT, "4");
            content.put(UNITS, "each");
            content.put("prepare", "-1");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(NAME, "hotdog buns");  //key value pair?
            content.put(RECIPE_ID, "3");
            content.put(INGREDIENT_ID, "706");
            content.put(AMT, "4");
            content.put(UNITS, "each");
            content.put("prepare", "-1");
            db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);

            content = new ContentValues();
            content.put(STEP, "Cook hotdogs in microwave, grill or in boiling water");  //key value pair?
            content.put(RECIPE_ID, "3");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

            content = new ContentValues();
            content.put(STEP, "When fully cooked palce in hotdog bun and serve");  //key value pair?
            content.put(RECIPE_ID, "3");
            db.insertOrThrow(TABLE_NAME_STEPS, null, content);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS ingredients");
            db.execSQL("DROP TABLE IF EXISTS pantry");
            db.execSQL("DROP TABLE IF EXISTS steps");
            db.execSQL("DROP TABLE IF EXISTS recipes");
            db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
            onCreate(db);
        }
    }


    public DataHandler open(){
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbhelper.close();
    }

    public long insertRecipe(String title, String source, int prepTime, int cookTime, String mealType,
                             int servingSize, int servingsPerRecipe, int caloriesPerServing){
        ContentValues content = new ContentValues();
        content.put(TITLE, title);
        content.put(SOURCE, source);
        content.put(PREP, prepTime);
        content.put(COOK, cookTime);
        content.put(MEALTYPE, mealType);
        content.put(SERVING_SIZE, servingSize);
        content.put(SERVINGS_PER_RECIPE, servingsPerRecipe);
        content.put(CALORIES_PER_SERVING, caloriesPerServing);
        content.put(STATUS, "YES");

        return db.insertOrThrow(TABLE_NAME_RECIPES, null, content);

    }

    public long insertRecipeIngredients(int recipeId, int ingredientId, double amt, String name, String units, String preparation){
        ContentValues content = new ContentValues();
        content.put(NAME, name);  //key value pair?
        content.put(RECIPE_ID, recipeId);
        content.put(INGREDIENT_ID, ingredientId);
        content.put(AMT, amt);
        content.put(UNITS, units);
        content.put("prepare", preparation);
        return db.insertOrThrow(TABLE_NAME_REC_INGREDIENTS, null, content);
    }

    public long insertSteps(int recipeId, String stepText){
        ContentValues content = new ContentValues();
        content.put(STEP, stepText);  //key value pair?
        content.put(RECIPE_ID, recipeId);
        return db.insertOrThrow(TABLE_NAME_STEPS, null, content);
    }

    public Cursor getAllRecipeIds(){
        return db.rawQuery("SELECT _id FROM recipes", null);
    }

    /* For wheel page*/
    public Cursor getRecipeIngredients(String _recipeId){
        //Do multiple DB calls here???
        //int recipeId = Integer.parseInt(_recipeId);
        return db.rawQuery("SELECT * FROM recipe_ingredients WHERE " + RECIPE_ID + "=?" , new String[]{_recipeId});
    }

    public Cursor getRecipes(){
        return db.rawQuery("SELECT * FROM recipes", null);
    }

    public Cursor getRecipeId(){
        //Get id from last recipe added
        return db.rawQuery("SELECT _id FROM recipes order by _id DESC LIMIT 1", null);
    }

    /* Changed name form getPantryUnits*/
    public Cursor getDBUnits(int ingredientId){
        return db.rawQuery("SELECT unit FROM ingredients WHERE _id=" +ingredientId + " LIMIT 1", null);
    }

    public Cursor getDisplayUnits(int ingredientId){
        return db.rawQuery("SELECT unitDisplay FROM ingredients WHERE _id=" +ingredientId + " LIMIT 1", null);
    }


    public Cursor checkPantry(int ingredientId, String _amt){
        //Get id from last recipe added
        //Assume unit is the same.
        int amt = Integer.parseInt(_amt);
        //TODO allow user to input new unit of measure but convert it to regular in DB
        //amt col is the amount you have.  it needs to be less or equal to the amount needed
        return db.rawQuery("SELECT * FROM pantry WHERE amt<=" + amt + " AND _id=" + ingredientId, null);
    }

    public Cursor getGeneralRecipeInfo(int recipeId){
        return db.rawQuery("SELECT * FROM recipes WHERE _id ="+ recipeId, null);
    }

    public Cursor getIngredientsRecipeInfo(int recipeId){
        return db.rawQuery("SELECT * FROM recipe_ingredients WHERE recipe_id ="+ recipeId, null);
    }

    public Cursor getStepsRecipeInfo(int recipeId){
        return db.rawQuery("SELECT * FROM steps WHERE recipe_id ="+ recipeId, null);
    }

    public Cursor searchRecipes(String search, String type){
        if(type == "all"){
            return db.rawQuery("SELECT * FROM recipes WHERE title like?",
                    new String[] {"%" + search + "%"});
        }else
        return db.rawQuery("SELECT * FROM recipes WHERE meal_type ='" + type + "' AND title like?",
                new String[] {"%" + search + "%"});
    }

    public Cursor getIngredientUnit(int itemId){
        return db.rawQuery("SELECT unit FROM ingredients WHERE _id ="+ itemId, null);
    }

    public long insertIntoShoppingList(int itemId, double amt, String units, String notes){
        ContentValues content = new ContentValues();
        content.put(AMT, amt);
        content.put(ITEM_ID, itemId);
        content.put(UNITS, units);
        content.put("notes", "");

        return db.insertOrThrow(TABLE_NAME_SHOPPING_LIST, null, content);
    }

    public Cursor getIngredientName(int item_id){
        return db.rawQuery("SELECT name FROM " + TABLE_NAME_INGREDIENTS + " WHERE _id =" + item_id, null);
    }

    public Cursor getShoppingList(){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_SHOPPING_LIST, null);
    }

    public long insertIntoPantry(double amt, int itemId){
        ContentValues content = new ContentValues();
        content.put(AMT, amt);
        content.put(ITEM_ID, itemId);
        Log.e("Inserting: ", "amt: " + amt + " itemId: " + itemId);
        return db.insertOrThrow(TABLE_NAME_PANTRY, null, content);
    }

    public Cursor existingPantryItem(int id){
        return db.rawQuery("SELECT * FROM pantry WHERE item_id=" + id, null);
    }

    public Cursor getAmt(int id){
        Log.e("Id: ", id + "");
        return db.rawQuery("SELECT amt FROM pantry WHERE item_id="+id, null);

    }

    public boolean deleteFromPantry(int itemId){
        return db.delete(TABLE_NAME_PANTRY, ITEM_ID + "=" + itemId, null) > 0;
    }

    public boolean deleteShoppingListItem(int itemId){
        Log.e("DataHandler", "Deleting Shopping List Item " + itemId);
        return db.delete(TABLE_NAME_SHOPPING_LIST, "_id" + "=" + itemId, null) > 0;
    }

    public boolean deleteRecipe(int recipeId){
        int count = 0;
        if(db.delete(TABLE_NAME_RECIPES, "_id" + "=" + recipeId, null) > 0) {
            count++;
        }if(db.delete(TABLE_NAME_REC_INGREDIENTS, RECIPE_ID + "=" + recipeId, null) > 0) {
            count++;
        }if(db.delete(TABLE_NAME_STEPS, RECIPE_ID + "=" + recipeId, null) > 0) {
            count++;
        }
        if(count==3) //count increases on success
            return true;
        else return false;
    }

    public int updatePantry(double amt, int ingredientId){
       // int _amt = (int) amt;
        ContentValues args = new ContentValues();
        args.put(AMT, amt);

        return db.update(TABLE_NAME_PANTRY, args,
            ITEM_ID + "=?",
            new String[] {
                    ingredientId + ""
            }
        );
    }


    public Cursor getIngredientsCount(){
        return db.rawQuery("SELECT count(_id) FROM ingredients", null);
    }


    public Cursor getPantry(){
        return db.rawQuery("SELECT pantry.amt, ingredients.name, ingredients.unit, ingredients._id, ingredients.unitDisplay FROM pantry, ingredients WHERE pantry.item_id = ingredients._id", null);
    }

    public Cursor getPantryId(int ingredientId){
        return db.rawQuery("SELECT _id FROM pantry WHERE item_id = " + ingredientId, null);
    }

    /* Search ingredients */
    public Cursor searchIngredients(String searchTerm){
        return db.rawQuery("SELECT * FROM ingredients WHERE name like?",
                new String[] {"%" + searchTerm + "%"});
    }

    public Cursor getAllPantryIds(){
        return db.rawQuery("SELECT _id FROM pantry", null);
    }

    public Cursor getPantryItemById(String id){
        return db.rawQuery("SELECT * FROM pantry WHERE _id=?", new String[]{id});
    }

/*********************************************************************************
    PURPOSE:    decreases the pantry AI(auto increment) item by provided amount
                if provided amount is less than or equal to 0 it instead deletes
                the item from the pantry.
    RETURNS:    and integer corresponding to the number of records affected
    CALLED FROM:   called from with makeRecipe(),
                    when user deletes item from myPantry page

 *******************************************************************************/
    public boolean decreasePantry(int _id, double newAmt){
        ContentValues args = new ContentValues();
        args.put("amt", newAmt);
        Log.e("New AMt:", "" + newAmt);
        if(newAmt <= 0)
            return db.delete(TABLE_NAME_PANTRY, "_id" + "=" + _id, null) > 0;

        int affected  = db.update(TABLE_NAME_PANTRY, args, "_id" + "=?", new String[]{_id+""});
        if(affected > 0)
            return true;
        else return false;
    }

    public Cursor getPantryAmt(int ingredientId){
        return db.rawQuery("SELECT amt FROM pantry WHERE item_id =?",
                new String[] {""+ingredientId});
    }

    public Cursor getIngredientRecipeAmt(int ingredientId, int recipeId){
        return db.rawQuery("SELECT amt FROM recipe_ingredients WHERE ingredient_id ="+ingredientId+" AND recipe_id ="+recipeId, null);
    }
}
