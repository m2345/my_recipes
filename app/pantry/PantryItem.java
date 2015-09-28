package com.development.buccola.myrecipes.pantry;

/***************************************************
 * FILE:        PantryItem
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/12/15
 * PURPOSE:     To create a pantry item object
 ***************************************************/
public class PantryItem {
    private String ingredientName;
    private int ingredientId;
    private int pantryItemId;
    private String displayUnit;
    private String dbUnit;
    private double amtAsStored;
    private double amtAsDisplayed;


    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getPantryItemId() {
        return pantryItemId;
    }

    public void setPantryItemId(int pantryItemId) {
        this.pantryItemId = pantryItemId;
    }

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public String getDbUnit() {
        return dbUnit;
    }

    public void setDbUnit(String dbUnit) {
        this.dbUnit = dbUnit;
    }

    public double getAmtAsStored() {
        return amtAsStored;
    }

    public void setAmtAsStored(double amtAsStored) {
        this.amtAsStored = amtAsStored;
    }

    public double getAmtAsDisplayed() {
        return amtAsDisplayed;
    }

    public void setAmtAsDisplayed(double amtAsDisplayed) {
        this.amtAsDisplayed = amtAsDisplayed;
    }


    public PantryItem(){
        ingredientId = -1;
        ingredientName = null;
        pantryItemId = -1;
        displayUnit = null;
        dbUnit = null;
        amtAsDisplayed = -1;
        amtAsStored = -1;
    }


}
