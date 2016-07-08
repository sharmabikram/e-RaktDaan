package com.together.stand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LaunchingPage extends AppCompatActivity {

    static SharedPreferences userInfo;
    static String savedPhone;
    static boolean isVolunteer;
    Intent intent;
    String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching_page);
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        savedPhone = userInfo.getString("phone", "NON");
        isVolunteer = userInfo.getBoolean("volunteer", false);
        Log.i(TAG, savedPhone);
        Log.i(TAG, String.valueOf(isVolunteer));
        if(savedPhone.compareToIgnoreCase("NON") == 0)
           intent = new Intent (LaunchingPage.this, loginPage.class);
        else{
            intent = new Intent(LaunchingPage.this, LoggedIn.class);
        }
        startActivity(intent);
        finish();
    }
}
