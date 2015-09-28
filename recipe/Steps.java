package com.development.buccola.myrecipes.recipe;

import java.io.Serializable;


/***************************************************
 * FILE:        Steps
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * IMPLEMENTS:  Serializable
 * PURPOSE:     To create a Steps object
 ***************************************************/
public class Steps implements Serializable {
    private int _id;
    private String step;
    private int recipe_id;

    /*********************************
     FUNCTION:  Steps()
     PARAMS:    None
     RETURNS:   nothing
     PURPOSE:   Constructor
     NOTES:     Sets strings to null and ints to -1
     *********************************/
    public Steps(){
        this._id = -1;
        this.step = null;
        this.recipe_id = -1;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

}
