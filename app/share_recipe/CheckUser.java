package com.development.buccola.myrecipes.share_recipe;

/***************************************************
 * FILE:        CheckUser
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/8/15
 * PURPOSE:     Check if user is registered.
 * EXTENDS:     AsyncTask<String, Void, Void>
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class CheckUser extends AsyncTask<String, Void, Void> {
    boolean userExists = false;
    public boolean isUser(){
        return userExists;
    }
    final String CHECK_USER_URL = "http://myrecipesapp.com/app/checkUser.php";

    @Override
    protected Void doInBackground(String... arg) {
        String result = "";
        InputStream isr = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", arg[0]));
        params.add(new BasicNameValuePair("email", arg[1]));
        params.add(new BasicNameValuePair("facebookId", arg[2]));

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(CHECK_USER_URL);
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }catch(Exception e){
            Log.e("Log_tag", "Error in http connection" + e.toString());
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            isr.close();
            result = sb.toString();
            Log.e("Result: ", result);
        }catch (Exception e) {
            Log.e("Log_tag", "Error convferting result " + e.toString());
        }

        //parse json data
        try{
            String s = "";
            JSONArray jArray = new JSONArray((result));
            for(int i=0; i<jArray.length(); i++){
                JSONObject json = jArray.getJSONObject(i);
                s = s +
                        "Name: " + json.getString("FirstName")+ " " + json.getString("LastName") + "\n";
            };
        }catch(Exception e){
            Log.e("LOG_TAG", "Error parsing data " + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e("Create", "done");
    }


}
