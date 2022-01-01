package com.example.kannan.carpool;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class userprofile extends Fragment {

    View view;
    public String details;
    String nos;
    Button cancel,send;
    RatingBar rat;
    ListView comments;
    TextView name,email,dob,address,carname,gender;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public String host_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_userprofile, container, false);

        String[] abc={"View Comments"};
        cancel=(Button)view.findViewById(R.id.FButton1);
        send=(Button)view.findViewById(R.id.FButton);

        comments=(ListView)view.findViewById(R.id.list1);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.listitem, abc);
        comments.setAdapter(itemsAdapter);


        rat=(RatingBar)view.findViewById(R.id.ratingBar2);
        name=(TextView) view.findViewById(R.id.name);
        email=(TextView) view.findViewById(R.id.email);
        dob=(TextView) view.findViewById(R.id.dob);
        gender=(TextView)view.findViewById(R.id.gender);
        address=(TextView) view.findViewById(R.id.address);
        carname=(TextView) view.findViewById(R.id.carname);

        host_id=getArguments().getString("hosts");
        nos=getArguments().getString("nos");
        new fetch_user_info().execute(host_id);


        comments.setOnItemClickListener(mylistclicklistner);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        final String user_id=preferences.getString(Config.ID_SHARED_PREF,"1");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new requesting().execute(user_id,host_id,nos);
            }
        });
        return view;

    }


    private AdapterView.OnItemClickListener mylistclicklistner =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view_comments fragment = new view_comments();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("id",host_id);

            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    };


    private class requesting extends AsyncTask<String, String, String>
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

                url = new URL(getResources().getString(R.string.localserver)+"requesting.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("r_id",params[0]).appendQueryParameter("host_id",params[1]).appendQueryParameter("nos",nos);
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
                SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putBoolean(Config.REQUESTED,true);
                editor.commit();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();}

                aftersendingrequest fragment = new aftersendingrequest();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }



        }

    }






    private class fetch_user_info extends AsyncTask<String, String, String>
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

                url = new URL(getResources().getString(R.string.localserver)+"fetchinfo.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("host_id",params[0]);
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
            try{
                JSONArray jArray = new JSONArray(result);
                name.setText(jArray.getJSONObject(0).getString("username"));
                gender.setText(gender.getText()+jArray.getJSONObject(0).getString("gender"));
                email.setText(jArray.getJSONObject(0).getString("email"));
                address.setText(jArray.getJSONObject(0).getString("address"));
                dob.setText(dob.getText()+jArray.getJSONObject(0).getString("age"));
                carname.setText(carname.getText()+jArray.getJSONObject(0).getString("carname"));
                String rati=jArray.getJSONObject(0).getString("AVG(ratings)");
                if(rati.equals("null")){
                    rati="0.0";
                }
                rat.setRating(Float.parseFloat(rati));


            }
            catch (JSONException e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }


        }

    }

}
