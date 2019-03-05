package teguhsusanto.com.crudbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_username, txt_password;
    Intent intent;
    int code;
    String data,msg;
    ConnectivityManager conMgr;

    private static final String TAG = MainActivity.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), Config.ERR_NO_INTERNET_CONNECTION,
                        Toast.LENGTH_LONG).show();
            }
        }

        btn_login = (Button) findViewById(R.id.btn_login);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(Config.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(Config.session_status, false);
        id = sharedpreferences.getString(Config.TAG_ID, null);
        username = sharedpreferences.getString(Config.TAG_USERNAME, null);

        if (session) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra(Config.TAG_ID, id);
            intent.putExtra(Config.TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,Config.ERR_NO_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,Config.ERR_CANNOT_EMPTY, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(Config.STATUS_LOGGING_IN);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    code = jObj.getInt(Config.TAG_CODE);
                    msg = jObj.getString(Config.TAG_MESSAGE);
                    // Check for error node in json
                    if (code == 200) {
                        data = jObj.getString(Config.TAG_DATA);
                        JSONObject jObjData = new JSONObject(data);
                        String token = jObjData.getString(Config.TAG_TOKEN);

                        Log.e("Successfully Login!", token);

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Config.session_status, true);
                        editor.putString(Config.TAG_TOKEN, token);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        intent.putExtra(Config.TAG_TOKEN, token);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                int statusCode = error.networkResponse != null ? error.networkResponse.statusCode : 0;
                String msgError = null;
                if(error.networkResponse.statusCode==401){
                    msgError = "Not Authorized";
                } else if (error instanceof NetworkError) {
                    msgError = "Failed to connect to server ";

                } else if (error instanceof TimeoutError) {
                    msgError = "Timeout for connection exceeded";
                } else {
                    if (error.networkResponse != null && error.networkResponse.data != null && !error.networkResponse.data.equals("")) {
                        try {
                            msgError = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        msgError = error.getMessage();
                    }
                }
                Log.e(TAG, "Login Error: " + msgError);
                Toast.makeText(getApplicationContext(),
                        msgError, Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
