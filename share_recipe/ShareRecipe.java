package com.development.buccola.myrecipes.share_recipe;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.development.buccola.myrecipes.database.DataHandler;
import com.development.buccola.myrecipes.recipe.Ingredient;
import com.development.buccola.myrecipes.recipe.Recipe;
import com.development.buccola.myrecipes.recipe.Steps;
import com.development.buccola.myrecipes.common.ViewRecipes;
import com.example.megan.myapplication.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/***************************************************
 * FILE:        ShareRecipe
 * PROGRAMMER:  Megan Buccola
 * CREATED:     5/6/15
 * PURPOSE:     Share Recipe Fragment
 * EXTENDS:     Fragment
 * IMPLEMENTS:  ShareListAdapter.customButtonListener
 *              AdapterView.OnItemSelectedListener
 * LAYOUT:      share
 * SUBCLASS:    GetSharedRecipes
 ***************************************************/
public class ShareRecipe extends Fragment implements ShareListAdapter.customButtonListener, AdapterView.OnItemSelectedListener {
    View rootview;
    TextView greeting;
    ListView listView;
    TextView error;
    Spinner recipeSpinner;
    ParseInstallation installation;
    public TextView tvShared;
    TextView errorList;
    private Profile profile;
    private ParseUser currentUser;
    private ArrayList<String> userNames, userIds;
    private ArrayList<Integer> recipeIds;
    private static final String TAG = ShareRecipe.class.getSimpleName();
    Button addRecipes;
    int selectedRecipeId;
    int num;
    Recipe newRecipe;
    GetSharedRecipes gsr = new GetSharedRecipes();
    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.facebook.samples.hellofacebook:PendingAction";
    boolean loggedin;
    private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private final List<String> permissions;

    public ShareRecipe(){
        permissions = Arrays.asList("public_profile", "user_friends");//"user_status");
    }

    /*********************************
     FUNCTION:  FacebookCallback<Sharer.Result/>
     PURPOSE:   Facebook Callback
     *********************************/
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(rootview.getContext())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userNames = new ArrayList<>();
        userIds = new ArrayList<>();
        num = 0;
        currentUser = ParseUser.getCurrentUser();
        rootview = inflater.inflate(R.layout.share, container, false);
        LoginButton authButton = (LoginButton) rootview.findViewById(R.id.facebook_login);
        authButton.setFragment(this);
        authButton.setReadPermissions(permissions);
        callbackManager = CallbackManager.Factory.create();

        authButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loggedin = true;

                        handlePendingAction();
                        updateUI();
                        Log.e("Facebook", "onSuccess");
                    }

                    @Override
                    public void onCancel() {
                        Log.e("Facebook", "onCancel");
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook", "onError");
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    private void showAlert() {
                        Log.e("Facebook", "showAlert");
                        new AlertDialog.Builder(rootview.getContext())
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        greeting = (TextView) rootview.findViewById(R.id.greeting);
        listView = (ListView) rootview.findViewById(R.id.lvFriends);
        error = (TextView) rootview.findViewById(R.id.error);
        addRecipes = (Button) rootview.findViewById(R.id.addNewRecipes);
        errorList = (TextView) rootview.findViewById(R.id.errorList);
        recipeSpinner = (Spinner) rootview.findViewById(R.id.recipeSpinner);
        tvShared = (TextView) rootview.findViewById(R.id.shared);
        addRecipes = (Button) rootview.findViewById(R.id.addNewRecipes);
        updateUI();

        if (isOnline() && profile != null) { //was currentUser != null
            profile = Profile.getCurrentProfile();
            Log.e("Current FB Id", profile.getId());
            gsr = new GetSharedRecipes();
            Log.e("DEBUG", "calling Getshared");
            gsr.execute(profile.getName(), profile.getId(), getEmail()); 
            loadFriends();
        } else
            showErrors();

        if (currentUser != null)
            Log.e(TAG, " Current user Not null");
        else {
            //user not logged into parse
            //ok if logged into facebook
            Log.e(TAG, "Current user is Null attempting to log in");
            //checking this caused an error
            if(currentUser == null)
                Log.e("Done", "currentUse is still null");
            else
                Log.e("Done", "curretnUse is NOT null");
        }


        currentUserInfo();

        /* Load recipe spinner */
        recipeSpinner.setOnItemSelectedListener(this);
        loadRecipeSpinner();

        addRecipes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<Recipe> list = gsr.getRecipeObject();
                if (list.size() == 0) {
                    Toast.makeText(rootview.getContext(), "You don't have any recipes to view", Toast.LENGTH_LONG).show();
                } else {
                    Fragment newFragment = new ViewRecipes();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    try{
                        bundle.putByteArray("JSON", object2Bytes(list));
                    }catch(IOException e){
                        Log.e("SHARE00", "ERROR");
                        e.printStackTrace();
                    }
                    //
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    newFragment.setArguments(bundle);
                    transaction.replace(R.id.container, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                }
            }

        });

        return rootview;
    }

    /*********************************
     FUNCTION:  byte[] object2Butes(Object)
     PARAMS:    object to convert to bytes
     RETURNS:   byte array
     PURPOSE:   Converting objects to byte arrays
     *********************************/
    static public byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(o);
        return baos.toByteArray();
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and utr
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(rootview.getContext());

        updateUI();
    /*    Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  uiHelper.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(rootview.getContext());
        // uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRecipeId = recipeIds.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onButtonClickListener(int position, String value) {
        Log.e("Custom Listener", "Button pressed");
        String toName = userNames.get(position);
        String toId = userIds.get(position);
        String fromName = profile.getName();
        String fromId = profile.getId();

        JSONArray recipeJSON = getRecipeJSON(selectedRecipeId);
        Log.e("Recipe JSON:", recipeJSON.toString());

        new Share(toName, fromName, toId, fromId).execute(recipeJSON);

        Toast.makeText(rootview.getContext(), "Recipe Shared",
                Toast.LENGTH_LONG).show();

        sendPush(toId);
    }

    private enum PendingAction {
        NONE,
    }

    /*********************************
     FUNCTION:  void login(String, String)
     PARAMS:    uesrname and password
     RETURNS:   nothings
     PURPOSE:   Logs the user into parse if they are a returning user
     *********************************/
    private void login(String lowerCase, String password) {
        ParseUser.logInInBackground(lowerCase, "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null)
                    loginSuccessful();
                else {
                    Log.e("Login error:", e.toString());
                    loginUnSuccessful();
                }
            }
        });

    }
    private void loginUnSuccessful(){
        Log.e("Error", "Login Not Successful");
    }

    /*********************************
     FUNCTION:  void loginSuccessful();
     PARAMS:    nothing
     RETURNS:   none
     PURPOSE:   Set the currentUser variable
     *********************************/
private void loginSuccessful(){
    Log.e("Login", "successful");
    currentUser = ParseUser.getCurrentUser();
}

    /*********************************
     FUNCTION:  JSONArray getRecipeJSON(int)
     PARAMS:    int - recipeId
     RETURNS:   JSON Array
     PURPOSE:   Gets recipe of given id to a JSONArray
     *********************************/
    private JSONArray getRecipeJSON(int recipeId) {
        DataHandler handler = new DataHandler(rootview.getContext());
        handler.open();
        JSONArray myArray = new JSONArray();
        JSONObject ingredietObj = new JSONObject();
        JSONArray ingredientsArray = new JSONArray();
        JSONObject stepsObj = new JSONObject();
        JSONArray stepsArray = new JSONArray();
        JSONObject general = new JSONObject();

        /* Ingredients */
        try {
            Cursor C = handler.getRecipeIngredients(recipeId + "");
            if (C.moveToFirst()) {
                do {
                    JSONArray aa = new JSONArray();
                    JSONObject a = new JSONObject();
                    a.put("_id", C.getString(0));
                    a.put("ingredient_id", C.getString(1));
                    a.put("recipe_id", C.getString(2));
                    a.put("name", C.getString(3));
                    a.put("amt", C.getString(4));
                    a.put("unit", C.getString(5));
                    a.put("preporation", C.getString(6));
                    Log.e("Prep wes", C.getString(6));
                    aa.put(a);
                    ingredientsArray.put(aa);
                } while (C.moveToNext());
            }
            ingredietObj.put("Ingredient", ingredientsArray);
        } catch (JSONException e) {

        }

        /* Steps */
        try {
            Cursor C = handler.getStepsRecipeInfo(recipeId);
            if (C.moveToFirst()) {
                do {
                    JSONArray aa = new JSONArray();
                    JSONObject a = new JSONObject();
                    a.put("_id", C.getString(0));
                    a.put("step", C.getString(1));
                    a.put("recipe_id", C.getString(2));
                    aa.put(a);
                    stepsArray.put(aa);
                } while (C.moveToNext());
            }
            stepsObj.put("Step", stepsArray);
        } catch (JSONException e) {

        }

        /* GENERAL */
        try {
            Cursor C = handler.getGeneralRecipeInfo(recipeId);
            if (C.moveToFirst()) {
                do {
                    JSONArray aa = new JSONArray();
                    JSONObject a = new JSONObject();

                    a.put("_id", C.getString(0));
                    a.put("title", C.getString(1));
                    a.put("source", C.getString(2));
                    a.put("prep_time", C.getString(3));
                    a.put("cook_time", C.getString(4));
                    a.put("meal_type", C.getString(5));
                    a.put("calories_per_serving", C.getString(6));
                    a.put("total_servings", C.getString(7));
                    a.put("serving_size", C.getString(8));
                    a.put("status", C.getString(9));

                    aa.put(a);
                    general.put("General", aa);
                } while (C.moveToNext());
            }

        } catch (JSONException e) {
                Log.e("JSONEXCETION", e.toString());
        }
        handler.close();
        myArray.put(ingredietObj);
        myArray.put(stepsObj);
        myArray.put(general);
        return myArray;
    }


    /*********************************
     FUNCTION:  void showErrors();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   show erros if user is not logged into facebook
                and/or if not internet connection is found
     *********************************/
    private void showErrors() {
        if (!isOnline())
            error.setText("No Internet Connection. \n");

        if (profile == null) {
            error.setText(error.getText() + "Please Login to Facebook");
            Log.e("Profile", "Is Null");
        }
    }

    /*********************************
     FUNCTION:  boolean isOnline();
     PARAMS:    none
     RETURNS:   ture if connected to internet, false if not
     PURPOSE:   Check for an internet connection
     *********************************/
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /*********************************
     FUNCTION:  boolean hasFreindsPermission();
     PARAMS:    none
     RETURNS:   boolean - true if friends permission exists else retunrs false
     PURPOSE:   Converting objects to byte arrays
     *********************************/
    private boolean hasFriendsPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");

    }

    /*********************************
     FUNCTION:  void loginWihtPermission();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Login the user with read permissions
     *********************************/
    private void loginWithPermssins() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends"));
    }

    /*********************************
     FUNCTION:  void updateUI();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Update the UI with the user's facebook name
                Load friends listView if not already displayed
     *********************************/
    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        profile = Profile.getCurrentProfile();
        if(profile != null)
            Log.e("FB", "update UI username: " + profile.getFirstName());
        if (enableButtons && profile != null) {
            //if(userNames.size() == 0)
             //   loadFriends();
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
            if (loggedin) addNewUser();

            //update parse installation table
            installation = ParseInstallation.getCurrentInstallation().getCurrentInstallation();
            installation.put("username", profile.getId());
            installation.saveInBackground();
            currentUser = ParseUser.getCurrentUser();
            Log.e("FB2", "done udpate and assigned ");
        } else {
            greeting.setText(null);
        }
    }

    /*********************************
     FUNCTION:  void loadListView();
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   load listViewWith freinds also using the app
     NOTES:     Uses userNames to populate the listView
                Users custom adapter - ShareListAdapter
     *********************************/
    private void loadListView() {
        ShareListAdapter adapter;
        if (userNames.size() == 0)
            errorList.setText("None of your friends are using MyPanty");

        else {
            adapter = new ShareListAdapter(listView.getContext(), userNames);
            adapter.setCustomButtonListener(ShareRecipe.this);
            listView.setAdapter(adapter);
        }
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }

    /*********************************
     FUNCTION:  void addNewUser()
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Adds user to parse and our db if necessary
     *********************************/
    public void addNewUser() {
        //from facebook
        profile = Profile.getCurrentProfile();
        String name = profile.getName();
        String id = profile.getId();
        String email = getEmail();

        //parse.com
        //TODO this always return true;
        if (checkNewUser(name, email, id)) {
            ParseUser user = new ParseUser();
            user.setUsername(id.toString()); //the facebookId is used as the username as it is the primary key
            user.put("name", name);
            user.setPassword("password");
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e("error signing up", " " + e);
                        login(profile.getId(), "");
                    }else {
                        Log.e("", "Registration Successful");
                        Log.e("Hi", ParseUser.getCurrentUser().getUsername() + "");
                        updateUI();
                    }
                }
            });
        }
        if(currentUser == null)
            login(profile.getId(), "");
    }

    /*********************************
     FUNCTION:  void loadFriends()
     PARAMS:    none
     RETURNS:   none
     PURPOSE:  Load friends ArrayList
     *********************************/
    public void loadFriends() {
        if (!hasFriendsPermission())
            loginWithPermssins();

        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray object, GraphResponse response) {
                        if (object != null) {
                            Log.e("Printing JSON", object.toString());
                            try {
                                parseFriendsJSON(object);
                            } catch (JSONException e) {
                                Log.e("JSON", "JSON Exception: " + e);
                            }
                        } else {
                            Log.e("JSON", "jsonObject is null");
                        }
                        if (response != null)
                            Log.e("res ", response.toString());
                        else
                            Log.e("res", "response is null");

                        Log.e("Friends", "completed");
                    }
                }
        );
        Bundle params = new Bundle();
        params.putString("fields", "id, name, picture");
        request.setParameters(params);
        request.executeAsync();

    }

    /*********************************
     FUNCTION:  void parseFriendsJSON(JSONArray) throws JSONException
     PARAMS:    JSONArry
     RETURNS:   nothing
     PURPOSE:   parses the json response form facebook graph API call
                loads userNames and userIds ArrayLists
     *********************************/
    private void parseFriendsJSON(JSONArray obj) throws JSONException {
        userNames = new ArrayList<>();
        userIds = new ArrayList<>();
        for (int i = 0; i < obj.length(); i++) {
            JSONObject json_obj = obj.getJSONObject(i);
            String name = json_obj.getString("name");
            String id = json_obj.getString("id");
            userNames.add(name);
            userIds.add(id);
        }

        loadListView();
    }

    /*********************************
     FUNCTION:  void currentUserInfo()
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Get the information of the current facebook user
     NOTES:     https://developers.facebook.com/docs/android/graph
     *********************************/
    public void currentUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        if (object != null) {
                            Log.e("Printing JSON", object.toString());

                        } else {
                            Log.e("Res", "jsonObject is null");
                        }
                        if (response != null)
                            Log.e("res ", response.toString());
                        else Log.e("res", "response is null");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /*
        Check if User is in our database;
     */
    /*********************************
     FUNCTION:  boolean checkNewUser(String, String ,String)
     PARAMS:    String - name, String - email, String -id
     RETURNS:   true
     PURPOSE:   Executes CheckUser class
     *********************************/

    public boolean checkNewUser(String name, String email, String id) {
        new CheckUser().execute(name, email, id);
        return true;  //TODO this always returns true  -- this is ok for now as above code logs in if parse throws already registered error
    }

    /*********************************
     FUNCTION:  ArrayList<String> recipesArray()
     PARAMS:    none
     RETURNS:   ArrayList<String>
     PURPOSE:   Get data for recipes to choose
     NOTES:     Loads recipeIds arrayList
     *********************************/

    private ArrayList<String> recipesArray() {
        ArrayList<String> labels = new ArrayList<>();
        recipeIds = new ArrayList<>();
        DataHandler handler = new DataHandler(rootview.getContext());
        handler.open();
        Cursor C = handler.getRecipes();
        if (C.moveToFirst()) {
            do {
                labels.add(C.getString(1));
                recipeIds.add(Integer.parseInt(C.getString(0)));
            } while (C.moveToNext());
        }
        handler.close();
        return labels;
    }

    /*********************************
     FUNCTION:  void loadRecipeSpinner()
     PARAMS:    none
     RETURNS:   nothing
     PURPOSE:   Loads Recipe Spinners
     NOTES:     Uses ArrayAdapter.  uses recipesArray to populate spinner
     *********************************/

    private void loadRecipeSpinner() {
        ArrayList<String> labels = recipesArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootview.getContext(),
                android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeSpinner.setAdapter(adapter);

    }


    /*********************************
     FUNCTION:  void sendPush(String)
     PARAMS:    Stirng id of the user to send recipe to
     RETURNS:   nothing
     PURPOSE:   Send push to notify user a recipe was shared with them.
     *********************************/

    public void sendPush(String toUser) {
        ParsePush parsePush = new ParsePush();
        ParseQuery<ParseInstallation> parseQuery = ParseQuery.getQuery(ParseInstallation.class);
        parseQuery.whereEqualTo("username", toUser);
        parsePush.setQuery(parseQuery);
        String message = profile.getName() + " sent you a recipe";
        parsePush.setMessage(message);
        parsePush.sendInBackground();
    }

    /*********************************
     FUNCTION:  String getEmail()
     PARAMS:    none
     RETURNS:   String - users email
     PURPOSE:   Get the gamil account associated with the android device / the user
     *********************************/

    public String getEmail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity().getBaseContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name;
            }
        }
        return null;
    }

    /***************************************************
     * SUBCLASS:    GetSharedRecipes
     * PROGRAMMER:  Megan Buccola
     * CREATED:     5/6/15
     * PURPOSE:     Get Recipes that was shared with the user
     * EXTENDS:     AsuncTask<String, Void, Void>
     ***************************************************/
    class GetSharedRecipes extends AsyncTask<String, Void, Void> {
        private final String URL_GET_SHARE = "http://myrecipesapp.com/app/getSharedRecipes.php";
        private int numOfNewRecipes = 0;
        private String result = "";
        private JSONObject object;
        private JSONArray recipeArray;
        ArrayList<Recipe> RecipesToView = new ArrayList<>();
        public int getText() {
            return numOfNewRecipes;
        }

        public GetSharedRecipes() {}

        public ArrayList<Recipe> getRecipeObject(){
            return RecipesToView;
        }
        @Override
        protected Void doInBackground(String... arg) {
            InputStream isr = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("currentUsername", arg[0]));
            params.add(new BasicNameValuePair("currentUserId", arg[1]));
            params.add(new BasicNameValuePair("currentUserEmail", arg[2]));
            Log.e("Get Shared", "Inside get shared" + arg[1]);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL_GET_SHARE);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("GetSharedRecipes", "Error in http connection" + e.toString());
                Toast.makeText(getActivity(), "An error occurred.  Unable to retrieve recipes that were shared with you.", Toast.LENGTH_LONG).show();
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
                Log.e("GetSharedRecipes", "Error converting result " + e.toString());
                Toast.makeText(getActivity(), "An error occurred.  Unable to retrieve recipes that were shared with you.", Toast.LENGTH_LONG).show();
            }

            try {
                object = new JSONObject(result);
                recipeArray = object.getJSONArray("recipe");
                numOfNewRecipes = recipeArray.length();
            } catch (JSONException e) {
                Log.e("Error", e.toString());
            }

            parseResponse();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e("Create", "done");
            tvShared.setText("New Recipes: " + numOfNewRecipes);
            num = numOfNewRecipes;
        }


        /*********************************
         FUNCTION:  void parseResponse()
         PARAMS:    none
         RETURNS:   nothing
         PURPOSE:   parse recipe json response
                    Decdides whether to call parseAndroid or parseWeb
         *********************************/
        private void parseResponse() {
            try {
                Log.e("Parsing JSON", result.toString());
                for (int i = 0; i < recipeArray.length(); i++) {
                    newRecipe = new Recipe();       //create new recipe
                    JSONObject Jsonobject = recipeArray.getJSONObject(i);
                    String url = Jsonobject.getString("fileUrl");
                    String js = Jsonobject.getString("jsonObj");
                    String folder = Jsonobject.getString("folder").toLowerCase();
                    String fromName = Jsonobject.getString("fromName");
                    js = js.substring(1, js.length() - 1);
                    if(folder.equals("android"))
                        parseAndroid(js, fromName, url);
                    else parseWeb(js, fromName, url);
                }

            } catch (Exception e) {
                Log.e("GetSharedRecipes", "Error parsing data " + e.toString());

            }
        }


        /*********************************
         FUNCTION:  boolean parseAndroid(String, String, String)
         PARAMS:    String - JSONString, String fromName, String url
         RETURNS:   boolean - true on success
         PURPOSE:   parse recipe json that was shared using the app
         *********************************/

        private boolean parseAndroid(String js, String fromName, String url){
            js = js.substring(1, js.length() - 1);
            js = js.replace("\\\"", "\""); //android

            try {
                Log.e("parseAndoird unString", js);
                JSONArray jsonArray = new JSONArray(js);
                JSONObject ingredObj = new JSONObject(jsonArray.getString(0));
                JSONArray ingredArray = ingredObj.getJSONArray("Ingredient");
                JSONObject stepsObj = new JSONObject(jsonArray.getString(1));
                JSONArray stepsArray = stepsObj.getJSONArray("Step");
                JSONObject generalObj = new JSONObject(jsonArray.getString(2));
                JSONArray generalArray = generalObj.getJSONArray("General");
                insertIngredientsAndroid(ingredArray);
                insertStepsAndroid(stepsArray);
                insertGeneralAndroid(generalArray, fromName, url);
                RecipesToView.add(newRecipe);
                newRecipe = new Recipe();
            }catch(JSONException e){
                Log.e("parseandroidexec", e.toString());
                return false;
            }
            return true;
        }



        /*********************************
         FUNCTION:  boolean parseWeb(String, String, String);
         PARAMS:    String - jsonString, String - fromName, String url
         RETURNS:   boolean - ture on success
         PURPOSE:   parse recipe shared via website
         *********************************/

        private boolean parseWeb(String js, String fromName, String url){
            Log.e("parseWeb", "Parsing");
            js = js.replace("\\\"", "\"");

            try {
                Log.e("ParseWeb unString", js);
                JSONObject jsonArray = new JSONObject(js);
                //Get Ingredient Array
                 JSONArray ingredArray = jsonArray.getJSONArray("Ingredient");
                JSONArray stepsArray = jsonArray.getJSONArray("step");
                JSONObject generalObj = jsonArray.getJSONObject("general");//new JSONObject("general");

                Log.e("ingrdArray", ingredArray.toString());
                insertIngredientsWeb(ingredArray);
                insertStepsWeb(stepsArray);
                insertGeneralWeb(generalObj, fromName, url);
                // saveRecipe();
                RecipesToView.add(newRecipe);
                newRecipe = new Recipe();
            }catch(JSONException e){
                Log.e("parsewebexec", e.toString());
                return false;
            }
            return true;
        }


        /*********************************
         FUNCTION:  boolean insertGeneralWeb(JSONOBject, String fromName, String url)
         PARAMS:    JSONObject - array of the reccipe, String - fromName; String url
         RETURNS:   boolean - return true
         PURPOSE:   insert general information for recipe from website
         *********************************/

        private boolean insertGeneralWeb(JSONObject array, String fromName, String url) {
            if (array != null) {
                Log.e("GEN W", array.toString());
                Log.e("In Count Gen Web", array.length() + "");
                try {
                    String cook_time = array.getString("cook_time");
                    String title = array.getString("title");
                    String mealType = array.getString("meal_type");
                    String source = array.getString("source");
                    String total_servings = array.getString("total_servings");
                    String calories_per_serving = array.getString("calories_per_serving");
                    String servingSize = array.getString("serving_size");
                    String prep_time = array.getString("prep_time");
                    Log.e("insertGen INFO", cook_time + " " + title + " " + mealType + " " + source + " " + total_servings + " " + calories_per_serving + " " + servingSize + " " + prep_time);

                    newRecipe.setCalories_per_serving(Integer.parseInt(calories_per_serving));
                    newRecipe.setCook_time(Integer.parseInt(cook_time));
                    newRecipe.setId(-1);
                    newRecipe.setMeal_type(mealType);
                    newRecipe.setPrep_time(Integer.parseInt(prep_time));
                    newRecipe.setServing_size(Integer.parseInt(servingSize));
                    newRecipe.setTotal_servings(Integer.parseInt(total_servings));
                    newRecipe.setTitle(title);
                    newRecipe.setSource(source);
                    newRecipe.setSharedUrl(url);
                } catch (JSONException e) {
                    Log.e("insertGen error: ", e.toString());
                }

            } else {
                Log.e("INSERT", "Unable to parse json");
            }
            return true;
        }

        /*********************************
         FUNCTION:  boolean insertGeneralAndroid(JSONArray, String fromName, String url)
         PARAMS:    JSONArray - array of the recipe, String - fromName; String url
         RETURNS:   boolean - returns true
         PURPOSE:   insert general information for recipe from android
         *********************************/
        private boolean insertGeneralAndroid(JSONArray array, String fromName, String url) {
            String source;
            if (array != null) {
                try {
                    JSONObject jo = array.getJSONObject(0);
                    Log.e("insertGen", array.getString(0));
                    String cook_time = jo.getString("cook_time");
                    String title = jo.getString("title");
                    String mealType = jo.getString("meal_type");
                    Log.e("JSON", jo.getString("source")+ " " + fromName);
                    source = jo.getString("source");
                    String total_servings = jo.getString("total_servings");
                    String calories_per_serving = jo.getString("calories_per_serving");
                    String servingSize = jo.getString("serving_size");
                    String prep_time = jo.getString("prep_time");
                    Log.e("insertGen INFO", cook_time + " " + title + " " + mealType + " " + source + " " + total_servings + " " + calories_per_serving + " " + servingSize + " " + prep_time);

                    newRecipe.setCalories_per_serving(Integer.parseInt(calories_per_serving));
                    newRecipe.setCook_time(Integer.parseInt(cook_time));
                    newRecipe.setId(-1);
                    newRecipe.setMeal_type(mealType);
                    newRecipe.setPrep_time(Integer.parseInt(prep_time));
                    newRecipe.setServing_size(Integer.parseInt(servingSize));
                    newRecipe.setTotal_servings(Integer.parseInt(total_servings));
                    newRecipe.setTitle(title);
                    newRecipe.setSource(source);
                    newRecipe.setSharedUrl(url);
                } catch (JSONException e) {
                    Log.e("insertGen error: ", e.toString());
                }

            } else {
                Log.e("INSERT", "Unable to parse json");
            }
            return true;
        }

        /*********************************
         FUNCTION:  boolean insertIngredientsWeb(JSONArray)
         PARAMS:    JSONArray - array
         RETURNS:   boolean - returns true
         PURPOSE:   insert ingredients information for recipe from website
         *********************************/
        private boolean insertIngredientsWeb(JSONArray array){
            if (array != null) {
                Log.e("In Count", array.length() + "");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        Ingredient newIngredient = new Ingredient();
                        JSONObject jo = array.getJSONObject(i);
                        Log.e("insertIngredients: ", array.get(i).toString());
                        String unit = jo.getString("unit");
                        String amt = jo.getString("amt");
                        String ingredient_id = jo.getString("ingredient_id");
                        String name = jo.getString("name");
                        String preperation = jo.getString("preporation");
                        newIngredient.set_id(-1);
                        newIngredient.setAmt(Double.parseDouble(amt));
                        newIngredient.setIngredient_id(Integer.parseInt(ingredient_id));
                        newIngredient.setName(name);
                        newIngredient.setUnit(unit);
                        newIngredient.setPreperation(preperation);
                        newRecipe.addIngredients(newIngredient);
                        Log.e("insertIngredients", "unit: " + unit + " amt: " + amt + " ingredeintId: " + ingredient_id + " name " + name);
                    } catch (JSONException e) {
                        Log.e("insertIngredients: ", e.toString());
                    }
                }
            } else {
                Log.e("insertIngredients", "Unable to parse json");
            }


            return true;

        }

        /*********************************
         FUNCTION:  boolean insertIngredientsAndroid(JSONArray)
         PARAMS:    JSONArray - array
         RETURNS:   boolean - returns true
         PURPOSE:   insert ingreidnets information for recipe from android
         *********************************/
        private boolean insertIngredientsAndroid(JSONArray array) {
            if (array != null) {
                 for (int i = 0; i < array.length(); i++) {
                    try {
                        Ingredient newIngredient = new Ingredient();
                        JSONArray ja = array.getJSONArray(i);
                        JSONObject jo = ja.getJSONObject(0);
                        Log.e("insertIngredients: ", array.get(i).toString());
                        Log.e("insertIngredients JO", ja.getString(0));
                        String unit = jo.getString("unit");
                        String amt = jo.getString("amt");
                        String ingredient_id = jo.getString("ingredient_id");
                        String name = jo.getString("name");
                        String preporation = jo.getString("preporation");
                        newIngredient.set_id(-1);
                        newIngredient.setAmt(Double.parseDouble(amt));
                        newIngredient.setIngredient_id(Integer.parseInt(ingredient_id));
                        newIngredient.setName(name);
                        newIngredient.setUnit(unit);
                        newIngredient.setPreperation(preporation);
                        newRecipe.addIngredients(newIngredient);
                        Log.e("insertIngredients", "unit: " + unit + " amt: " + amt + " ingredeintId: " + ingredient_id + " name " + name);
                    } catch (JSONException e) {
                        Log.e("insertIngredients: ", e.toString());
                    }
                }
            } else {
                Log.e("insertIngredients", "Unable to parse json");
            }


            return true;
        }

        /*********************************
         FUNCTION:  boolean insertStepsAndroid(JSONArray)
         PARAMS:    JSONArray - array
         RETURNS:   boolean - returns true
         PURPOSE:   insert ingredients information for recipe from web
         *********************************/
        private boolean insertStepsWeb(JSONArray array) {
            if (array != null) {
                Steps newStep = new Steps();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject jo = array.getJSONObject(i);
                        String step = jo.getString("step");
                        newStep.set_id(-1);
                        newStep.setRecipe_id(-1);
                        newStep.setStep(step);
                        newRecipe.addStep(newStep);
                    } catch (JSONException e) {
                        Log.e("insertSteps error: ", e.toString());
                    }
                }
            } else {
                Log.e("INSERT", "Unable to parse json");
            }
            return true;
        }

        /*********************************
         FUNCTION:  boolean insertStepsAndroid(JSONArray)
         PARAMS:    JSONArray - array
         RETURNS:   boolean - returns true
         PURPOSE:   insert step information for recipe from android
         *********************************/
        private boolean insertStepsAndroid(JSONArray array) {
            if (array != null) {
                Steps newStep = new Steps();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONArray ja = array.getJSONArray(i);
                        JSONObject jo = ja.getJSONObject(0);
                        String step = jo.getString("step");
                        newStep.set_id(-1);
                        newStep.setRecipe_id(-1);
                        newStep.setStep(step);
                        newRecipe.addStep(newStep);
                    } catch (JSONException e) {
                        Log.e("insertSteps error: ", e.toString());
                    }
                }
            } else {
                Log.e("INSERT", "Unable to parse json");
            }
            return true;
        }
    }

}