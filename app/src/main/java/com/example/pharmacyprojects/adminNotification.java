package com.example.pharmacyprojects;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminNotification extends AppCompatActivity {
    String SomeKeys;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminnotificationlist);
        loadNotification();



    }

    public void loadNotification(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Dbref = database.getReference("Notification");
        ArrayList<NotificationClass> notificationList = new ArrayList<>();
        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for(DataSnapshot Ds : snapshot.getChildren()){
                        String notKey = Ds.getKey().toString().trim();
                        Log.e("Notification key","Not key: "+ notKey);
                        String notID = Ds.child("notID").getValue(String.class);
                        Log.e("Notification ID","Not ID: "+ notID);
                        String userName = Ds.child("userNAme").getValue(String.class);
                        Log.e("Notification Uname","Uname  "+ userName);
                        String orderID = Ds.child("orderID").getValue(String.class);
                        Log.e("Notification OrderID","Order ID:  "+ orderID);
                        int Status = Ds.child("status").getValue(Integer.class);
                        Log.e("Notification Status","Order Status:  "+ Status);
                        String NotMessage = Ds.child("mMessage").getValue(String.class);
                        Log.e("Notification Message","Message:  "+  NotMessage);
                        String AddedDate = Ds.child("dateAdded").getValue(String.class);
                        Log.e("Notification AddedDate","Added Date:  "+  AddedDate);

                        NotificationClass notClass = new NotificationClass(notID,  userName, orderID, NotMessage,  Status, AddedDate);
                        notificationList.add(notClass);



                    }
                    NotificationAdapter notAdapter = new NotificationAdapter(getApplicationContext(), notificationList);
                    ListView notListview = findViewById(R.id.listNotification_View);
                    notListview.setAdapter(notAdapter);


                }else{
                    Toast.makeText(getApplicationContext(), "Not Data found", Toast.LENGTH_LONG).show();
                    Log.e("Database Information", "Not Data found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onStart() {
        super.onStart();
        loadNotification();
    }

}
