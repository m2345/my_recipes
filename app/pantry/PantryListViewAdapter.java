package com.development.buccola.myrecipes.pantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.megan.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

/***************************************************
 * FILE:        PantryListViewAdapter
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/13/15
 * EXTENDS:     ArrayAdapter<String>
 * PURPOSE:     Custom Adapter to load Pantry items
 ***************************************************/
class PantryListViewAdapter extends ArrayAdapter<String> {
    customButtonListener customListner;
    HashMap<String, Integer> mIdMap = new HashMap<>();
    private Context context;
    private ArrayList<String> data = new ArrayList<>();

    public interface customButtonListener {
        void onButtonClickListner(int position, String value);
    }

    public class ViewHolder {
        TextView text;
        ImageButton button;
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }



    public PantryListViewAdapter(Context context, int textViewResourceId, ArrayList<String> dataItem) {
        super(context, R.layout.child_listview, dataItem);
        this.data = dataItem;
        this.context = context;
        for (int i = 0; i < dataItem.size(); ++i) {
            mIdMap.put(dataItem.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.child_listview_pantry, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.childTextView);
            viewHolder.button = (ImageButton) convertView
                    .findViewById(R.id.childDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }

            }
        });

        return convertView;
    }


}