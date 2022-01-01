package com.example.kannan.carpool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private EditText editTextAge;
    private EditText editTextAddress;
    private EditText editTextCarname;
    private String gender;
    private Fcm fcm=new Fcm();
    private Spinner spinner;
    private static final String[]paths = {"M","F"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextAge=(EditText)findViewById(R.id.editTextAge);
        editTextAddress=(EditText)findViewById(R.id.editTextAddress);
        editTextCarname=(EditText)findViewById(R.id.editTextCarname);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gender = "M";
                        break;
                    case 1:
                        gender = "F";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fcm.onTokenRefresh();


                if(!(isValidEmail(editTextEmail.getText().toString()))){
                    editTextEmail.setText(null);
                    Toast.makeText(getApplicationContext(),"enter a valid email address",Toast.LENGTH_LONG).show();
                }
                else{
                registerUser();}
            }
        });


    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void registerUser() {
        Toast.makeText(getApplicationContext(),fcm.recent_token, Toast.LENGTH_SHORT).show();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String address=editTextAddress.getText().toString().trim();
        final String age=editTextAge.getText().toString().trim();
        final String nameofcar=editTextCarname.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,getResources().getString(R.string.localserver)+"register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("age",age);
                params.put("gender",gender);
                params.put("address",address);
                params.put("carname",nameofcar);
                params.put("fcm",fcm.recent_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
