package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
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

public class userproductpage extends AppCompatActivity {
    String XuserKey;
    String someKeys;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userproductpage);
        Intent Intkeys = getIntent();
        someKeys = Intkeys.getStringExtra("passKeys");
        loadDrug();






    }
    public void loadDrug(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference();
        ArrayList<drugDbClass> drugDetails = new ArrayList<>();

        dRef.child("Drug").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot Ds : snapshot.getChildren()) {
                   String  drugID = Ds.getKey();
                   String DrugName = Ds.child("drugName").getValue(String.class);
                   String DrugUrl = Ds.child("drugFilePath").getValue(String.class);
                   String DrugDescription = Ds.child("drugDescription").getValue(String.class);
                   int DrugPrice = Ds.child("drugPrice").getValue(int.class);
                   int DrugQuantity = Ds.child("drugQuantity").getValue(int.class);

                    drugDbClass drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity, DrugDescription);

                    drugDetails.add(drug);


                }

                //construct the data source

                // Create the adapter to convert the array to views

               userproductAdapter drugadapter = new userproductAdapter(userproductpage.this, drugDetails);
                // Attach the adapter to a ListView
                ListView drugListView = (ListView) findViewById(R.id.listDrug_View);
                //drugDbClass drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity,  DrugDescription);
                drugListView.setAdapter(drugadapter);





                //drugClass...


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /*
    public void setID(String ID){
        this.someKeys = ID;
    }
    public String getKey(){
        return someKeys;
    }*/

}
