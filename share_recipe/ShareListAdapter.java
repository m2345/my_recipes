package com.development.buccola.myrecipes.share_recipe;

/***************************************************
 * FILE:        ShareListAdapter
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/5/15
 * PURPOSE:     custom array adapter for friends list
 * EXTENDS:     AsyncTask<String>
 * LAYOUT:      child_listview_share
 ***************************************************/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.megan.myapplication.R;

import java.util.ArrayList;


public class ShareListAdapter extends ArrayAdapter<String> {
    customButtonListener customListener;

    public interface customButtonListener {
        void onButtonClickListener(int position, String value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    private Context context;
    private ArrayList<String> data = new ArrayList<String>();

    public ShareListAdapter(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.child_listview_share, dataItem);
        this.data = dataItem;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.child_listview_share, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.childTextView);
            viewHolder.button = (Button) convertView
                    .findViewById(R.id.childButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onButtonClickListener(position,temp);
                }

            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView text;
        Button button;
    }
}