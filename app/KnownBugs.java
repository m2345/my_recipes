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
 * CREATED:     5/19/15
 * PURPOSE:     To tell the users known bugs and issues the app has
 * EXTENDS:     Fragment
 ***************************************************/
public class KnownBugs extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.known_bugs, container, false);

    }

}
