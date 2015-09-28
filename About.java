package com.development.buccola.myrecipes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.megan.myapplication.R;

/***************************************************
 * FILE:        About
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/17/15
 * PURPOSE:     About Fragment
 *              Description of what users are able to do "HELP"
 * EXTENDS:     Fragment
 ***************************************************/
public class About extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.about, container, false);
        return rootview;

    }

}
