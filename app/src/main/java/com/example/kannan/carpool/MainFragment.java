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


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int FROM_PICKER_REQUEST = 1;
    private static final int TO_PICKER_REQUEST = 0;
    View view;
    public EditText e;
    TextView e2;
    public EditText e1,e3;
    public Button b;
    public double s1,s2,s3,s4;
    DatePicker datePicker;
    LinearLayout l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_main, container, false);
        e=(EditText)view.findViewById(R.id.edittext);
        e1=(EditText)view.findViewById(R.id.edittext2);
        b=(Button)view.findViewById(R.id.btnWithText);
        e2=(TextView)view.findViewById(R.id.editText);
        e3=(EditText)view.findViewById(R.id.editText1);
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




        SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        final String user_id=preferences.getString(Config.ID_SHARED_PREF,"1");


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLogin().execute(user_id,e.getText().toString(),String.valueOf(s1),String.valueOf(s2),
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




    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        private String query;
        AlertDialog dialog = new SpotsDialog(getContext(),R.style.Custom);
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

                url = new URL(getResources().getString(R.string.localserver)+"hosting.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {

                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("host_id",params[0]).appendQueryParameter("flocation",params[1])
                        .appendQueryParameter("flat",params[2]).appendQueryParameter("flng",params[3]).appendQueryParameter("tlocation",params[4])
                        .appendQueryParameter("tlat",params[5]).appendQueryParameter("tlng",params[6]).appendQueryParameter("date",params[7])
                        .appendQueryParameter("nos",params[8]);
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


                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            if(result.equals("1")){

            SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putBoolean(Config.HOSTED,true);
            editor.putInt(Config.SEATS_OF_HOST,Integer.valueOf(e3.getText().toString()));
            editor.commit();




            MainFragmentanim fragment = new MainFragmentanim();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();}



        }

       }

    }

