package com.development.buccola.myrecipes.add_recipe;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/***************************************************
 * FILE:        ListViewAdapterAddSteps
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15.
 * EXTENDS:     ArrayAdapter<String>
 * Purpose:     Adapter to load steps listView
 ***************************************************/
class ListViewAdapterAddSteps extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public ListViewAdapterAddSteps(Context context, int textViewResourceId,
                                   List<String> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

