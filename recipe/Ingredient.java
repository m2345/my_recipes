package com.development.buccola.myrecipes.recipe;

import java.io.Serializable;

/***************************************************
 * FILE:        Ingredient
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * IMPLEMENTS:  Serializable
 * PURPOSE:     To create an ingredient object
 ***************************************************/
public class Ingredient implements Serializable {

    private int _id;
    private int ingredient_id;
    private int recipe_id;
    private String name;
    private double amt;
    private String unit;        //used in recipe.  This is how everything is stored in the database
    private boolean hasIngredient;
    private String displayUnit; //used in pantry

    public String getPreperation() {
        return preperation;
    }

    public void setPreperation(String preperation) {
        this.preperation = preperation;
    }

    private String preperation;
    public Ingredient(){
        this._id = -1;
        this.ingredient_id = -1;
        this.recipe_id = -1;
        this.name = null;
        this.amt = -1;
        this.unit = null;
        this.hasIngredient = false;
        this.displayUnit = null;
        this.preperation = null;
    }

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public boolean getHasIngredient() {
        return hasIngredient;
    }

    public void setHasIngredient(boolean hasIngredient) {
        this.hasIngredient = hasIngredient;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
