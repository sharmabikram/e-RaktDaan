package com.together.stand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class loginPage extends AppCompatActivity {

    EditText phone, pass;
    String phoneNo, password;
    ProgressDialog pDialog;
    String add;
    JSONParser parser;
    List<NameValuePair> argument = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_login);
        add = "http://10.0.3.2/donate/login.php"; // the address of login in server
        parser = new JSONParser();
        phone = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);
    }

    public void signIn(View v){
        phoneNo = phone.getText().toString();
        password = pass.getText().toString();
        argument.add(new BasicNameValuePair("phone", phoneNo));
        argument.add(new BasicNameValuePair("pass", password));
        new task().execute(add, "POST");
    }

    public void signup(View v){
        startActivity(new Intent(loginPage.this, MainActivity.class));
    }

    class task extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(loginPage.this);
            pDialog.setMessage("please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject obj;
            String url = strings[0];
            obj = parser.makeHttpRequest(url,"POST", argument );
            return  obj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            pDialog.dismiss();
            String em, pas;
            System.out.println("got jason");
            if (jsonObject != null) {
                try {
                    if(jsonObject.getString("success").equals("1"))
                    {
                        boolean volunteer = false;
                        if(jsonObject.getString("isVolunteer").equals("1"))
                            volunteer = true;
                        SharedPreferences.Editor updateInfo = LaunchingPage.userInfo.edit();
                        updateInfo.putString("phone", phoneNo);
                        updateInfo.putBoolean("volunteer", volunteer);
                        updateInfo.commit();
                        Intent intent = new Intent(loginPage.this, LoggedIn.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid phone or password", Toast.LENGTH_LONG).show();
                        //phone.setText("");
                        pass.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
