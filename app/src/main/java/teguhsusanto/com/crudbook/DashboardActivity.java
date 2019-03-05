package teguhsusanto.com.crudbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static teguhsusanto.com.crudbook.Config.TAG_ID;
import static teguhsusanto.com.crudbook.Config.TAG_TOKEN;
import static teguhsusanto.com.crudbook.Config.TAG_USERNAME;

public class DashboardActivity extends AppCompatActivity {
    Button btnProfile,btnBooks,btnLogout;
    SharedPreferences sharedpreferences;
    String token;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedpreferences = getSharedPreferences(Config.my_shared_preferences, Context.MODE_PRIVATE);
        token = sharedpreferences.getString(Config.TAG_TOKEN,"");

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnBooks = (Button) findViewById(R.id.btnBooks);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        sharedpreferences = getSharedPreferences(Config.my_shared_preferences, Context.MODE_PRIVATE);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkProfile();
            }
        });
        btnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getApplicationContext() ,token, Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Config.session_status, false);
                editor.putString(TAG_TOKEN, null);
                editor.commit();

                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                setLogout();
            }
        });
    }

    private void checkProfile(){
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Config.URL_PROFILE+token, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //pDialog.dismiss();
                            try {
                                //Parse the JSON response
                                String data = response.getString(Config.TAG_DATA);
                                Log.d("data",data);
                                JSONObject jObjData = new JSONObject(data);
                                String fullname      = jObjData.getString("fullname");
                                String username    = jObjData.getString("username");
                                Toast.makeText(DashboardActivity.this, "Full Name : "+fullname+" and User Name : "+username, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //pDialog.dismiss();

                            //Display error message whenever an error occurs
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }

    private void setLogout() {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Config.URL_LOGOUT+token, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //pDialog.dismiss();
                        try {
                            //Parse the JSON response
                            String status = response.getString("status_code");
                            Log.d("data",status);
                            if (status=="200"){
                                Toast.makeText(getApplicationContext() ,token, Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(Config.session_status, false);
                                editor.putString(TAG_TOKEN, null);
                                editor.commit();

                                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}