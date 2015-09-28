package com.development.buccola.myrecipes.common;

/***************************************************
 * FILE:        SharedRecipeSaved
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/13/15.
 * PURPOSE:     Delete a recipe after it is saved
 *              Recipe isn't actually deleted but it is flagged
 *              in phpmyadmin database as read/saved so not queried again
 * NOTES:        Async task to create a new food category
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class SharedRecipeSaved extends AsyncTask<String, Void, Void> {
    private String result = "";
    private final String REMOVE_URL = "http://myrecipesapp.com/app/removeFromShared.php";
    @Override
    protected Void doInBackground(String... arg) {
        InputStream isr = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("url", arg[0]));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REMOVE_URL);
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        } catch (Exception e) {
            Log.e("SharedRecipeSaved", "Error in http connection" + e.toString());
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "ISO-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            result = sb.toString();
            Log.e("GetShared: ", result);
        } catch (Exception e) {
            Log.e("SharedRecipeSaved", "Error converting result " + e.toString());
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e("SharedRecipeSaved", "done");
    }

}
