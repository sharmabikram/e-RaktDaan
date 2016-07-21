package com.together.stand;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    JSONParser jParser = new JSONParser();
    Button signup;
    String pass,repass;
    String phone;
    EditText Password, Repassword, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sinup);
        //tv = (TextView) findViewById(R.id.tv);

        Password = (EditText) findViewById(R.id.signupPass);
        Repassword = (EditText) findViewById(R.id.signupRePass);
        Phone = (EditText) findViewById(R.id.signupPhone);

        // Create button
        signup = (Button) findViewById(R.id.button2);

        // button click event
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                pass = Password.getText().toString();
                repass =Repassword.getText().toString();
                phone =Phone.getText().toString();
                if(phone.length()==10){
                    if(pass.length()>4){
                        if(pass.compareTo(repass)==0){
                            new task().execute("http://10.0.3.2/donate/signup.php", "POST");
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Password do no match", Toast.LENGTH_LONG).show();
                            Password.setText("");
                            Repassword.setText("");
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_LONG).show();
                        Password.setText("");
                        Repassword.setText("");
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid Phone", Toast.LENGTH_LONG).show();
                    Phone.setText("");
                    Password.setText("");
                    Repassword.setText("");
                }
            }
        });
       // new task().execute("http://10.0.3.2/create_product.php", "POST");
    }

    class task extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String url = params[0];
            String method = "POST";
            JSONParser parser = new JSONParser();
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("pass",pass));
            param.add(new BasicNameValuePair("phone",phone));
            JSONObject obj = parser.makeHttpRequest(url, method, param);
            if (obj != null)
                Log.d("er", "Not Null");
            else
                Log.d("er", "Is equal to null");
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                try {
                    if(jsonObject.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Phone already register. Please Login or signup with other phone", Toast.LENGTH_SHORT).show();
                        Phone.setText("");
                        Password.setText("");
                        Repassword.setText("");
                    }
                } catch (JSONException e) {
                   // e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try Again", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}