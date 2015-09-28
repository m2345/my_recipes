package com.development.buccola.myrecipes.shopping_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.megan.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

/***************************************************
 * FILE:        ListViewAdapterShopping
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * PURPOSE:     custom ListView adapter for shopping list
 * EXTENDS:     ArrayAdapter<String>
 ***************************************************/

class ListViewAdapterShopping extends ArrayAdapter<String> {
    customButtonListener customListener;
    HashMap<String, Integer> mIdMap = new HashMap<>();

    public class ViewHolder {
        TextView text;
        CheckBox checkBox;
        ImageButton button;
    }

    public ListViewAdapterShopping(Context context, int textViewResourceId,
                                   ArrayList<String> objects) {
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

    public interface customButtonListener {
        void onSelectedCrossOff(int position,String value);
        void removeFromList(int position, String value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.child_listview_shoppinglist, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.childTextView);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            viewHolder.button = (ImageButton) view.findViewById(R.id.removeItem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.removeFromList(position, temp);
                }

            }
        });


        viewHolder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {if(customListener != null){
            if(isChecked){
                customListener.onSelectedCrossOff(position, temp);
            }
        }
        }
    }
        );

        return view;
    }


}