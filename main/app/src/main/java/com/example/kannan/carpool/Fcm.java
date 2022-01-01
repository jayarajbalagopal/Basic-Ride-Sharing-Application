package com.example.kannan.carpool;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
public class Fcm extends FirebaseInstanceIdService {

    public String recent_token;

    public void onTokenRefresh(){

        recent_token= FirebaseInstanceId.getInstance().getToken();

    }


}

