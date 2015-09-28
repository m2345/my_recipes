package com.development.buccola.myrecipes.share_recipe;

/***************************************************
 * FILE:        Share
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * PURPOSE:     Share recipe
 * EXTENDS:     AsyncTask<JSONArray, Void, Void>
 ***************************************************/
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
class Share extends AsyncTask<JSONArray, Void, Void> {
    String toName, fromName, toId, fromId;

    Share(String _toName, String _fromName, String _toId, String _fromId){
        Log.e("Share Class", "Called");
        toName = _toName;
        fromName = _fromName;
        toId = _toId;
        fromId = _fromId;
    }

    @Override
    protected Void doInBackground(JSONArray... arg) {
        String result = "";
        InputStream isr = null;
        final String SHARE_URL = "http://myrecipesapp.com/app/shareRecipe.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fromName", fromName));
        params.add(new BasicNameValuePair("toName", toName));
        params.add(new BasicNameValuePair("fromId", fromId));
        params.add(new BasicNameValuePair("toId", toId));

        //create JSONobj
        Log.e("Passing Rec", arg[0].toString());
        params.add(new BasicNameValuePair("jsonObj", arg[0].toString()));
        Log.e("Share Class", "Params added");
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SHARE_URL);
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }catch(Exception e){
            Log.e("Log_tag", "Error in http connection" + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e("Create", "done");
    }
}