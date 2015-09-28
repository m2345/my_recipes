package com.development.buccola.myrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.megan.myapplication.R;

import java.util.HashMap;
import java.util.List;
/***************************************************
 * FILE:        NavDrawerAdapter
 * PROGRAMMER:  Megan Buccola
 * PURPOSE:     Custom Adapter for the navigation drawer
 * Created:     5/13/15
 * EXTENDS:     ArrayAdapter<String>
 ***************************************************/

class NavDrawerAdapter extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    public class ViewHolder{
        ImageView imageView;
        TextView text;

    }
    public NavDrawerAdapter(Context context, int textViewResourceId,
                            List<NavDrawerItem> objects, List<String> titles) {
        super(context, textViewResourceId, titles);
        for (int i = 0; i < objects.size(); ++i) {
         //   navItems.add(objects.get(i));
            mIdMap.put(objects.get(i).getItemName(), objects.get(i).getImageId());
        }
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.navdrawer, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.textViewListItem);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.imageView.setImageResource((int) getItemId(position));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);

        return view;
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
