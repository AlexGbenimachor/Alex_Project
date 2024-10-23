package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewDrug extends AppCompatActivity {
    String DrugName;
    String drugID;
    String DrugUrl;
    String DrugDescription;
    int DrugPrice;
    int DrugQuantity  ;
    drugDbClass drug;
    drugAdapter drugadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdrug);
        loadDrug();

        Button btn_gotoDrugForm = findViewById(R.id.btn_AdminAddDrug);
        btn_gotoDrugForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAdd();
            }
        });

        //ListView
        //define the Search string...
        EditText searchText = findViewById(R.id.edt_searchText);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchText.getText().toString().trim());
            }
        });

    }
    public void loadDrug(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference();
        ArrayList<drugDbClass> drugDetails = new ArrayList<>();

        dRef.child("Drug").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot Ds : snapshot.getChildren()) {
                    drugID = Ds.getKey();
                    DrugName = Ds.child("drugName").getValue(String.class);
                    DrugUrl = Ds.child("drugFilePath").getValue(String.class);
                    DrugDescription = Ds.child("drugDescription").getValue(String.class);
                    DrugPrice = Ds.child("drugPrice").getValue(int.class);
                    DrugQuantity = Ds.child("drugQuantity").getValue(int.class);

                    drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity, DrugDescription);

                    drugDetails.add(drug);


                }

                //construct the data source

                // Create the adapter to convert the array to views

                drugadapter = new drugAdapter(viewDrug.this, drugDetails);
                // Attach the adapter to a ListView
                ListView drugListView = (ListView) findViewById(R.id.listDrug);
                //drugDbClass drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity,  DrugDescription);
                drugListView.setAdapter(drugadapter);





                //drugClass...


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void gotoAdd(){
        Intent gotoDrug = new Intent(viewDrug.this, drugform.class);
        startActivity(gotoDrug);
    }
    public  void search(String searchString){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DbRef = database.getReference();
        ArrayList<drugDbClass> drugDetails = new ArrayList<>();
        //initialize the search string
        if(searchString != null && searchString.length()>0){
            char[] letter = searchString.toCharArray();
            String firstLetter = String.valueOf(letter[0]).toUpperCase();
            String remainingLetters = searchString.substring(1);
            searchString = firstLetter+remainingLetters;

        }

        //define data query...
        Query QRef = DbRef.child("Drug").orderByChild("drugName").startAt(searchString).endAt(searchString + "uf8ff");

        QRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot Ds : snapshot.getChildren()) {
                        String drugID = Ds.getKey();
                        String DrugName = Ds.child("drugName").getValue(String.class);
                        String DrugUrl = Ds.child("drugFilePath").getValue(String.class);
                        String DrugDescription = Ds.child("drugDescription").getValue(String.class);
                        int  DrugPrice = Ds.child("drugPrice").getValue(int.class);
                        int  DrugQuantity = Ds.child("drugQuantity").getValue(int.class);

                        drugDbClass  drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity, DrugDescription);

                        drugDetails.add(drug);


                }
                    // Create the adapter to convert the array to views

                    drugAdapter drugadapter = new drugAdapter(viewDrug.this, drugDetails);
                    // Attach the adapter to a ListView
                    ListView drugListView = (ListView) findViewById(R.id.listDrug);
                    //drugDbClass drug = new drugDbClass(drugID, DrugName, DrugUrl, DrugPrice, DrugQuantity,  DrugDescription);
                    drugListView.setAdapter(drugadapter);

                }else{

                    Toast.makeText(viewDrug.this, "no data found", Toast.LENGTH_LONG).show();
                    loadDrug();//refresh



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error", error.getMessage());

            }
        });

    }
}
