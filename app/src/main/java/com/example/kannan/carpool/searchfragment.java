package com.example.kannan.carpool;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchfragment extends Fragment {


    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int FROM_PICKER_REQUEST = 1;
    private static final int TO_PICKER_REQUEST = 0;
    View view;
    public EditText e;
    public TextView e2;
    public EditText e1,e3;
    public Button b;
    public double s1,s2,s3,s4;
    public LinearLayout l;
    public DatePicker datePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_searchfragment, container, false);
        e=(EditText)view.findViewById(R.id.editText8);
        e1=(EditText)view.findViewById(R.id.editText7);
        b=(Button)view.findViewById(R.id.buttonSearch);
        e2=(TextView)view.findViewById(R.id.editText5);
        e3=(EditText)view.findViewById(R.id.editText6);
        l=(LinearLayout)view.findViewById(R.id.lay);
        l.setVisibility(View.GONE);

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()),FROM_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }

        });

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()),TO_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker=(DatePicker)view.findViewById(R.id.datePicker2);
                l.setVisibility(View.VISIBLE);
                datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener(){
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        e2.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                        l.setVisibility(View.GONE);
                    }
                });

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Search().execute(e.getText().toString(),String.valueOf(s1),String.valueOf(s2),
                        e1.getText().toString(),String.valueOf(s3),String.valueOf(s4),e2.getText().toString(),e3.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==1){
                Place place = PlacePicker.getPlace(data,getActivity());
                e.setText(place.getName());
                s1=place.getLatLng().latitude;
                s2=place.getLatLng().longitude;
            }
            else if(requestCode==0){
                Place place = PlacePicker.getPlace(data,getActivity());
                e1.setText(place.getName());
                s3=place.getLatLng().latitude;
                s4=place.getLatLng().longitude;
            }
        }

    }





    private class Search extends AsyncTask<String, String, String> {
        private String query;
        AlertDialog dialog = new SpotsDialog(getContext(), R.style.Custom);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL(getResources().getString(R.string.localserver) + "search.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("flocation", params[0])
                        .appendQueryParameter("flat", params[1]).appendQueryParameter("flng", params[2]).appendQueryParameter("tlocation", params[3])
                        .appendQueryParameter("tlat", params[4]).appendQueryParameter("tlng", params[5]).appendQueryParameter("date", params[6])
                        .appendQueryParameter("nos", params[7]);
                query = builder.build().getEncodedQuery();


                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();


            } catch (IOException e1) {


                e1.printStackTrace();
                return "exception";

            }

            try {

                int response_code = conn.getResponseCode();


                if (response_code == HttpURLConnection.HTTP_OK) {


                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }


                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";

            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();


            if (result.equalsIgnoreCase("no rows")) {

                Toast.makeText(getActivity(), "No services found.", Toast.LENGTH_LONG).show();

            } else {

                    viewhosts fragment = new viewhosts();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("hosts",result);
                    bundle.putString("nos",e3.getText().toString());

                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

            }

        }

    }

}
