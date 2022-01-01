package com.example.kannan.carpool;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class view_comments extends Fragment {

    View view;
    ListView list;
    ArrayList list1;
    public JSONArray jArray;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_view_comments, container, false);
        list=(ListView)view.findViewById(R.id.listforcomments);
        final String host_id=getArguments().getString("id");



        new comments().execute(host_id);

        return view;

    }



    private class comments extends AsyncTask<String, String, String> {
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

                url = new URL(getResources().getString(R.string.localserver) + "fetchcomments.php");

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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("host_id", params[0]);
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
            list1=new ArrayList();


            if (result.equalsIgnoreCase("no rows")) {

                Toast.makeText(getActivity(), "No comments for this user yet.", Toast.LENGTH_LONG).show();

            } else {


                try{
                     jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        list1.add(i, json_data.getString("comments"));
                    }


                }
                catch (JSONException e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.listitem, list1);
                list.setAdapter(itemsAdapter);

            }

        }

    }


}
