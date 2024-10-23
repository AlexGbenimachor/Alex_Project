package com.example.pharmacyprojects;

import android.content.Intent;
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


public class myorderProduct extends AppCompatActivity {
    String SomeKeys;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorderproduct );
        //lets initialize the user key!
        Intent xintent = getIntent();
        SomeKeys = xintent.getStringExtra("passKeys");
        loadOrder(SomeKeys);


        




    }
    public void loadOrder(String somekeys){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference Dbref  = database.getReference("MyOrder").child(somekeys);
        ArrayList<myorderclass > orderlist = new ArrayList<>();



        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot Ds : snapshot.getChildren()) {
                        String OrderID = Ds.child("mOrderID").getValue(String.class);
                        Log.e("Order ID", "My Order ID: " + OrderID);
                        String DrugID = Ds.child("drugID").getValue(String.class);
                        Log.e("Drug ID", "Order Drug ID: " + DrugID);
                        String DrugName = Ds.child("drugName").getValue(String.class);
                        Log.e("Drug Name", "Order Drug Name: " + DrugName);
                        String userName = Ds.child("userName").getValue(String.class);
                        Log.e("Order-User Name", "Order userName: " + userName);
                        int drugPrice = Ds.child("drugPrice").getValue(Integer.class);
                        Log.e("Drug Price", "Order Drug Price: " + drugPrice);
                        int drugQuantity = Ds.child("drugQuantity").getValue(Integer.class);
                        Log.e("Drug Quantity", "Order Drug Quantity: " + drugQuantity);
                        String dateAdded = Ds.child("orderDate").getValue(String.class);
                        Log.e("Drug Date Added", "Order Drug Date: " + dateAdded);
                        String drugImage = Ds.child("imageUrl").getValue(String.class);
                        int Status = Ds.child("status").getValue(Integer.class);


                        int total = Ds.child("total").getValue(Integer.class);
                        Log.e("Drug total", "Order Drug total: " + total);

                        String Address = Ds.child("deliverAddress").getValue(String.class);
                        Log.e("Drug Address", "Order Drug Address: " + Address);

                        //Define the my order class

                        myorderclass orderclass = new myorderclass(OrderID, DrugID, somekeys,  userName, drugImage, DrugName,  drugPrice, drugQuantity,Status, total,  Address, dateAdded);

                        orderlist.add(orderclass);




                    }
                    orderAdapter orderadapter = new orderAdapter(myorderProduct.this, orderlist);
                    ListView orderListView = findViewById(R.id.myorderListView);
                    orderListView.setAdapter(orderadapter);


                    }else{
                    Toast.makeText(getApplicationContext(), "you have no Order", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
