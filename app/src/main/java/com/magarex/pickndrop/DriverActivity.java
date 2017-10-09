package com.magarex.pickndrop;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DriverActivity extends AppCompatActivity {

    private EditText vehicle_Type,vehicle_Name,vehicle_Seats;
    private Button btnAddVehicle;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        vehicle_Type = (EditText)findViewById(R.id.vehicle_Type);
        vehicle_Name = (EditText)findViewById(R.id.vehicle_Name);
        vehicle_Seats = (EditText)findViewById(R.id.vehicle_seats);
        btnAddVehicle = (Button) findViewById(R.id.btnAddVehicle);

        dialog = new ProgressDialog(this);

        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehicle();
            }
        });

    }

    private void addVehicle() {

        final String type = vehicle_Type.getText().toString().trim();
        final String name = vehicle_Name.getText().toString().trim();
        final String seats = vehicle_Seats.getText().toString().trim();

        dialog.setMessage("Adding");
        dialog.show();

        StringRequest stringrequest = new StringRequest(Request.Method.POST, "AddVehicle.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                //Tu karega
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                //Tu karega
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("",type);
                params.put("",name);
                params.put("",seats);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringrequest);

    }
}
