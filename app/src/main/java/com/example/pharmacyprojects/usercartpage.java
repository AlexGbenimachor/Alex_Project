package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class usercartpage extends AppCompatActivity {
    String someKeys;
    int total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userorderpage);
        Intent  xIntent = getIntent();
        someKeys = xIntent.getStringExtra("passKeys");
        Log.e("Get user Key","User Key: " + someKeys);
        mycart(someKeys);


    }
    public void mycart(String Keys){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = database.getReference();

        //define the DataQuery
        DatabaseReference queryLocation = MyRef.child("CartOrder");
        Query qRef = queryLocation.child(Keys);
        int totalx = 0;




        ArrayList<cartorderclass>  cartlist = new ArrayList<>();


        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot Ds : snapshot.getChildren()){
                        String cartID = Ds.child("cartOrdID").getValue(String.class);
                        Log.e("cart Order ID", "cart ID: "+ cartID);
                        String DrugID =Ds.child("drugID").getValue(String.class);
                        Log.e("cart Drug ID", "Drug ID: "+ DrugID);
                        String DrugName =Ds.child("drugName").getValue(String.class);
                        Log.e("cart Drug Name", "Drug Name: "+DrugName);
                        String DrugImgUrl = Ds.child("imageUrl").getValue(String.class);
                        int DrugPrice = Ds.child("productPrice").getValue(Integer.class);
                        int DrugQuantity = Ds.child("productQuantity").getValue(Integer.class);
                        total  = total + (DrugQuantity * DrugPrice);
                        Log.e("compute total"," total"+total);






                        cartorderclass cartclass = new cartorderclass(cartID, DrugID,  DrugName,  DrugImgUrl, DrugPrice, DrugQuantity);

                        cartlist.add(cartclass);


                    }
                    cartorderAdapter cartAdapter = new cartorderAdapter( getApplicationContext(), cartlist);

                    ListView orderListview = findViewById(R.id.orderListView);
                    orderListview.setAdapter(cartAdapter);
                    TextView btntotal = findViewById(R.id.Tv_text_total);
                    btntotal.setText("Total: "+  "\u20ac"+total);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        

    }
}
