package com.together.stand;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class LoggedIn extends AppCompatActivity {

    static boolean home = true;

    String TAG = "Update";
    String url = "http://10.0.3.2/donate/registerVolunteer.php";
    String name, address, blood, factor;
    String height, weight, age;
    List<NameValuePair> param;
    ProgressDialog pDialog;
    Spinner spinner, rhSpinner, genSpinner;
    ArrayAdapter adapter, rhadapter, genAdapter;
    EditText nm, ag, hei, wei, add, pin;
    String []blood_groups;
    String bl_grp, PIN;
    String rh, rh_factor[];
    String gender[], gen;
    Button submit;
    AlertDialog.Builder closeApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        closeApp = new AlertDialog.Builder(this);
        closeApp.setMessage("Do you want to close the application");
        closeApp.setCancelable(false);
        closeApp.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        closeApp.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        blood_groups = new String[]{"A", "B", "AB", "O"};
        rh_factor = new String[]{"+", "-"};
        gender = new String[]{"Male", "Female"};
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blood_groups);
        rhadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rh_factor);
        genAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            SharedPreferences.Editor update = LaunchingPage.userInfo.edit();
            update.putString("phone", " ");
            update.putBoolean("volunteer", false);
            update.commit();
            startActivity(new Intent(getApplicationContext(), loginPage.class));
            finish();
        }
        return true;
    }

    public void getList(View v){
        // get the list
        setContentView(0);
    }

    public void register_volunteer(View v){
        if(LaunchingPage.userInfo.getBoolean("volunteer", false)){
            Toast.makeText(getApplicationContext(), "This phone is already registered", Toast.LENGTH_LONG).show();
            return ;
        }
        setContentView(R.layout.volunteer_form);
        home = false;
        nm = (EditText) findViewById(R.id.name);
        add = (EditText) findViewById(R.id.address);
        pin = (EditText) findViewById(R.id.PIN);
        ag = (EditText) findViewById(R.id.age);
        hei = (EditText) findViewById(R.id.height);
        wei = (EditText) findViewById(R.id.weight);
        submit = (Button) findViewById(R.id.submit);
        spinner = (Spinner) findViewById(R.id.group);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bl_grp = blood_groups[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        rhSpinner = (Spinner) findViewById(R.id.rh_factor);
        rhSpinner.setAdapter(rhadapter);
        rhSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                rh = rh_factor[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        genSpinner = (Spinner) findViewById(R.id.gender);
        genSpinner.setAdapter(genAdapter);
        genSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                gen = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    class task extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            param = new ArrayList<>();
            param.add(new BasicNameValuePair("phone", LaunchingPage.userInfo.getString("phone", " ")));
            param.add(new BasicNameValuePair("name", name));
            param.add(new BasicNameValuePair("address", address));
            param.add(new BasicNameValuePair("pincode", PIN));
            param.add(new BasicNameValuePair("height", height));
            param.add(new BasicNameValuePair("weight", weight));
            param.add(new BasicNameValuePair("blood_group", bl_grp));
            param.add(new BasicNameValuePair("rh_factor", rh));
            param.add(new BasicNameValuePair("gender", gen));
            pDialog = new ProgressDialog(LoggedIn.this);
            pDialog.setMessage("Uploading your data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject obj;
            JSONParser parser = new JSONParser();
            obj = parser.makeHttpRequest(url, "POST", param);
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            pDialog.dismiss();
            if(jsonObject !=null){
                try{
                    if(jsonObject.getString("success").equals("1")){
                        Toast.makeText(getApplicationContext(), "Congrats your data uploaded successfully", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor updateVol = LaunchingPage.userInfo.edit();
                        updateVol.putBoolean("volunteer", true);
                        updateVol.commit();
                        setContentView(R.layout.activity_logged_in);
                    }
                }catch (Exception e){
                    System.out.println("Exception at postExecute at logged in");
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Error occured try later...", Toast.LENGTH_LONG).show();
                setContentView(R.layout.activity_logged_in);
            }

        }
    }

    public void updateDetail(View v){
        Log.i(TAG, "HERE");
        name = nm.getText().toString();
        address = add.getText().toString();
        PIN = pin.getText().toString();
        age = ag.getText().toString();
        height = hei.getText().toString();
        weight = wei.getText().toString();
        new task().execute();
    }

    public void register_organisation(View v){

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "back");
        if(home){
            closeApp.show();
        }
        else {
            Log.d(TAG, "after exit");
            setContentView(R.layout.activity_logged_in);
            home = true;
        }
    }
}
