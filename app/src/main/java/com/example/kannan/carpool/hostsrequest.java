package com.example.kannan.carpool;


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
public class hostsrequest extends Fragment {

    View view;
    ListView reqlist;
    String requests;
    public ArrayList<String> idss=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_hostsrequest, container, false);

        ArrayList<String> list1=new ArrayList<>();
        reqlist=(ListView)view.findViewById(R.id.listView);

        requests=getArguments().getString("hosts");


        try{
            JSONArray jArray = new JSONArray(requests);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                list1.add(i,json_data.getString("username").toUpperCase()+"\n"+json_data.getString("email")+"\nNo of seats required : "+json_data.getString("nos"));
                idss.add(i,json_data.getString("r_id"));

            }
        }
        catch (JSONException e) {

            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }



        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.listitem, list1);
        reqlist.setAdapter(itemsAdapter);
        reqlist.setOnItemClickListener(mylistclicklistner);


        return view;

    }

    private AdapterView.OnItemClickListener mylistclicklistner =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            userprofileforhosts fragment = new userprofileforhosts();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            String nos[];
            String nos1[];
            String info=((TextView)v).getText().toString();
            nos=info.split("\n");
            nos1=nos[2].split(":");

            Bundle bundle = new Bundle();
            bundle.putString("hosts",idss.get(position));
            bundle.putString("nos",nos1[1]);

            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

}
