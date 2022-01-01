package com.example.kannan.carpool;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class viewhosts extends Fragment {


    View view;
    ListView hostlist;
    String hosts,nos;
    public ArrayList<String> idss=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_viewhosts, container, false);
        ArrayList<String> list1=new ArrayList<>();

        hostlist=(ListView)view.findViewById(R.id.listView);
        nos=getArguments().getString("nos");
        hosts=getArguments().getString("hosts");


        try{
           JSONArray jArray = new JSONArray(hosts);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    list1.add(i,json_data.getString("username").toUpperCase()+"\n"+json_data.getString("email")+"\nNo of seats available : "+json_data.getString("nos")+
                    "\n"+json_data.getString("flocation")+" to "+json_data.getString("tlocation"));
                    idss.add(i,json_data.getString("host_id"));

                }
        }
        catch (JSONException e) {

            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }



        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.listitem, list1);
        hostlist.setAdapter(itemsAdapter);
        hostlist.setOnItemClickListener(mylistclicklistner);

        return view;

    }

    private AdapterView.OnItemClickListener mylistclicklistner =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            userprofile fragment = new userprofile();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("hosts",idss.get(position));
            bundle.putString("nos",nos);

            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

}
