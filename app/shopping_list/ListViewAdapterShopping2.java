package com.development.buccola.myrecipes.shopping_list;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.megan.myapplication.R;

import java.util.HashMap;
import java.util.List;

/***************************************************
 * FILE:        ListViewAdapterShopping2
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * PURPOSE:     custom ListView adapter for shopping checked of list
 * EXTENDS:     ArrayAdapter<String>
 ***************************************************/
class ListViewAdapterShopping2 extends ArrayAdapter<String> {
    customButtonListener customListener;
    HashMap<String, Integer> mIdMap = new HashMap<>();

    public class ViewHolder {
        TextView text;
        CheckBox checkBox;
    }

    public ListViewAdapterShopping2(Context context, int textViewResourceId,
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

    public interface customButtonListener {
        void onSelectedAddBackToList(int position,String value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.child_listview_shoppinglist_2, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.childTextView);
            viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (customListener != null) {
                    if (isChecked) {
                        customListener.onSelectedAddBackToList(position, temp);
                    }
                }
            }
        });

        return view;
    }


}