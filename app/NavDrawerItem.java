package com.development.buccola.myrecipes;
import android.app.Activity;
/***************************************************
 * FILE:        NavDrawerItem
 * PROGRAMMER:  Megan Buccola
 * PURPOSE:     To be used with the NavDrawerAdapter to allow image icons in the navDrawer
 * Created:     5/13/15
 * EXTENDS:     Activity
 ***************************************************/
public class NavDrawerItem extends Activity {
    private int imageId;
    private String itemName;

    public NavDrawerItem(String itemName, int imageId) {
        super();
        this.imageId = imageId;
        this.itemName = itemName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
