package com.dartstransit.latetrips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    public static final String PREFS_NAME = "LateTripsPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String userid = settings.getString("clientId", "-1");
        String userpass = settings.getString("clientPassword", "-1");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        if (!userid.equals("-1") && !userpass.equals("-1")) {
            mEmailView.setText(userid);
            mPasswordView.setText(userpass);
        }
    }

    /**
     * Called when the user clicks the Save button
     */
    public void SaveData(View view) {
        // Do something in response to button
        String client = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (client.length() > 2 && password.length() > 4) {
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("clientId", client);
            editor.putString("clientPassword", password);
            // Commit the edits!
            editor.commit();
            MsgData.UserId = client;
            MsgData.UserPassword = password;
            this.finish();
        } else {
            Toast.makeText(getBaseContext(), "Passenger Number and/or Password to short", Toast.LENGTH_LONG).show();
        }
    }
}



