package com.together.stand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequirementDetail extends AppCompatActivity {

    String url = "http://10.0.3.2/donate/list.php";
    List<NameValuePair> param;
    String TAG = "INFO";
    Spinner req_bl_grp, req_rh_fac;
    EditText reqPIN;
    String pin, rh, bl_grp;
    String []blood_groups;
    String rh_factor[];
    ArrayAdapter bl, rhFac;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_detail);
        req_bl_grp = (Spinner) findViewById(R.id.req_bl_grp);
        reqPIN = (EditText) findViewById(R.id.reqPIN);

        req_rh_fac = (Spinner) findViewById(R.id.req_rh_factor);
        blood_groups = new String[]{"A", "B", "AB", "O"};
        rh_factor = new String[]{"+", "-"};

        bl = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, blood_groups);
        rhFac = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, rh_factor);

        req_bl_grp.setAdapter(bl);
        req_rh_fac.setAdapter(rhFac);

        req_bl_grp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bl_grp = blood_groups[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        req_rh_fac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                rh = rh_factor[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void scanResult(View v){
        pin = reqPIN.getText().toString();

        Log.i(TAG, pin);
        Log.i(TAG, bl_grp);
        Log.i(TAG, rh);
        LoggedIn.reqPin = pin;
        new task().execute();
    }

    class task extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject;
            JSONParser parser = new JSONParser();
            jsonObject = parser.makeHttpRequest(url, "POST", param);
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            param = new ArrayList<>();
            param.add(new BasicNameValuePair("pincode", pin));
            param.add(new BasicNameValuePair("blood_group", bl_grp));
            param.add(new BasicNameValuePair("rhFac", rh));

            progressDialog = new ProgressDialog(RequirementDetail.this);
            progressDialog.setMessage("Fetching the result....");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressDialog.dismiss();
            JSONArray jsonArray = null;
            if(jsonObject != null){

                try {
                    if (jsonObject.getString("success").compareTo("1") == 0) {
                        System.out.println("Got the result");
                        jsonArray = jsonObject.getJSONArray("list");
                        LoggedIn.list = new Volunteer[jsonArray.length()];
                        for(int i = 0; i<jsonArray.length(); ++i){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            LoggedIn.list[i] = new Volunteer();
                            LoggedIn.list[i].address = obj.getString("address");
                            LoggedIn.list[i].name = obj.getString("name");
                            LoggedIn.list[i].pincode = obj.getString("pincode");
                            LoggedIn.list[i].phone = obj.getString("phone");
                            LoggedIn.list[i].blood_group = obj.getString("blood_group");
                            LoggedIn.list[i].rh_factor = obj.getString("rh_factor");
                            LoggedIn.list[i].height = Integer.parseInt(obj.getString("height"));
                            LoggedIn.list[i].weight = Integer.parseInt(obj.getString("weight"));

                            System.out.println(LoggedIn.list[i].name);
                        }

                        Intent intent = new Intent(RequirementDetail.this, ShowList.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.i(TAG, "Exception at post Execute");
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Something Went wrong.. Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
