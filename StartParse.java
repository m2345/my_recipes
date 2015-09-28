package com.development.buccola.myrecipes;
import android.app.Application;

import com.parse.Parse;

/***************************************************
 * FILE:        StartParse
 * PROGRAMMER:  Megan Buccola
 * PURPOSE:     To initialize parse
 * Created:     5/13/15
 * EXTENDS:     Application
 ***************************************************/
public class StartParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "NchqTw4HkEYPHdWtY8IMQgSnXlSbAWQlhmmG1o0T", "XJhu55r1nbmgr3cvOkMe5JZf4w15Xe9NVnYeUxl2");
        //Parse.initialize(this, "5Z0xJeXfzPy4mQ0ThsGPHNJwx1WRSOWyeylTGTG4", "Qu129PcU7wewM63H3GTCJ44vOdHw2PLyrMlB161d");

    }
}
