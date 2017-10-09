package com.magarex.pickndrop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RegistrationActivity extends AppCompatActivity {

    private EditText R_username,R_email,R_password,R_phoneNo;
    private Button btnRegister,btnLinkToLoginScreen;
    private ProgressDialog progressDialog;
    private TelephonyManager manager;
    private SQLiteHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        R_username = (EditText)findViewById(R.id.R_username);
        R_email = (EditText)findViewById(R.id.R_username);
        R_password = (EditText)findViewById(R.id.R_username);
        R_phoneNo = (EditText)findViewById(R.id.R_username);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button)findViewById(R.id.btnLinkToLoginScreen);
        progressDialog = new ProgressDialog(this);
        manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register_user();
            }
        });

        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void Register_user() {

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }

        final String Username = R_username.getText().toString().trim();
        final String Email = R_email.getText().toString().trim();
        final String Password = R_password.getText().toString().trim();
        final String PhoneNo = R_phoneNo.getText().toString().trim();
        final String ImeiNo = manager.getDeviceId().toString().trim();

        progressDialog.setMessage("Inserting");
        progressDialog.show();
        StringRequest stringrequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                handler = new SQLiteHandler(getBaseContext());
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    Toast.makeText(getBaseContext(),jsonobject.getString("message"),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                R_username.setText(error.toString());
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", Username);
                params.put("email", Email);
                params.put("password", Password);
                params.put("phoneNo", PhoneNo);
                params.put("imeiNo", ImeiNo);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringrequest);
    }
}
