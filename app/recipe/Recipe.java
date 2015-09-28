package com.development.buccola.myrecipes.recipe;

import java.io.Serializable;
import java.util.ArrayList;

/***************************************************
 * FILE:        Recipe
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * IMPLEMENTS:  Serializable
 * PURPOSE:     To create a recipe object
 ***************************************************/
public class Recipe implements Serializable {
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Steps> Steps;
    private String title, source, status, meal_type;
    private int id;
    private int prep_time;
    private int cook_time;
    private int calories_per_serving;
    private int total_servings;
    private int serving_size;
    private int recipeId;
    private String sharedUrl;


    /*********************************
     FUNCTION:  Recipe()
     PARAMS:    NONE
     RETURNS:   nothing
     PURPOSE:   constructor
     NOTES:      String variables that are required are set to null;
                Those that are not required are set to "N/A"
                Integers are set to -1
     *********************************/

    public Recipe(){
        this.ingredients = new ArrayList<>();
        Steps = new ArrayList<>();
        this.title = null;
        this.source = "N/A";
        this.status = "N/A";
        this.meal_type = "N/A";
        this.id = -1;
        this.prep_time = -1;
        this.cook_time = -1;
        this.calories_per_serving = -1;
        this.total_servings = -1;
        this.serving_size = -1;
        this.recipeId = -1;
        this.sharedUrl = "N/A";
    }


    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredients(Ingredient ingredients) {
        this.ingredients.add(ingredients);
    }

    public ArrayList<com.development.buccola.myrecipes.recipe.Steps> getSteps() {
        return Steps;
    }

    public void addStep(Steps steps) {
        this.Steps.add(steps);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }

    public int getCook_time() {
        return cook_time;
    }

    public void setCook_time(int cook_time) {
        this.cook_time = cook_time;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public int getCalories_per_serving() {
        return calories_per_serving;
    }

    public void setCalories_per_serving(int calories_per_serving) {
        this.calories_per_serving = calories_per_serving;
    }

    public int getTotal_servings() {
        return total_servings;
    }

    public void setTotal_servings(int total_servings) {
        this.total_servings = total_servings;
    }

    public int getServing_size() {
        return serving_size;
    }

    public void setServing_size(int serving_size) {
        this.serving_size = serving_size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
