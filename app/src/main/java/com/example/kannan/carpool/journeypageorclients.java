package com.example.kannan.carpool;


import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
public class journeypageorclients extends Fragment {

    View view;
    ListView list;
    ArrayList<String> rid=new ArrayList<>();
    ArrayList<String> list1=new ArrayList<>();
    Button cancel;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_journeypageorclients, container, false);
        list=(ListView)view.findViewById(R.id.listView2);
        list1.clear();


        SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String user_id=preferences.getString(Config.ID_SHARED_PREF,"1");


        new fetch_host().execute(user_id);
        new fetch_user_info().execute(user_id);
        cancel=(Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new cancel().execute(user_id);
            }
        });

        return view;
    }





    private class fetch_host extends AsyncTask<String, String, String>
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

                url = new URL(getResources().getString(R.string.localserver)+"fetchhost.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("r_id",params[0]);
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
                     JSONObject json_data = jArray.getJSONObject(0);
                    list1.add(0,json_data.getString("username").toUpperCase()+"\n"+json_data.getString("email")+"\n (HOST)");
                    rid.add(0,json_data.getString("user_id"));

            }
            catch (JSONException e) {

               // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
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

                url = new URL(getResources().getString(R.string.localserver)+"journeyclient.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("r_id",params[0]);
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

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    list1.add(i+1,json_data.getString("username").toUpperCase()+"\n"+json_data.getString("email")+"\n nos : "+json_data.getString("nos"));
                    rid.add(i+1,json_data.getString("r_id"));
                }
            }
            catch (JSONException e) {

              //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.listitem, list1);
            list.setAdapter(itemsAdapter);
            list.setOnItemClickListener(mylistclicklistner);



        }

    }

    private AdapterView.OnItemClickListener mylistclicklistner=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            rateacustomer fragment = new rateacustomer();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("id",rid.get(position));

            String name[];
            String info=list1.get(position);
            name=info.split("\n");
            bundle.putString("name",name[0]);

            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }
    };


    private class cancel extends AsyncTask<String, String, String> {
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

                url = new URL(getResources().getString(R.string.localserver) + "cancelbyclient.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("req_id", params[0]);
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

            SharedPreferences preferences=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putBoolean(Config.REQUESTED,false);
            editor.commit();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            searchfragment fragment = new searchfragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

    }



}
