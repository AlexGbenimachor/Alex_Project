package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ordermanagement extends AppCompatActivity {
    String someKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordermanagment);
        Intent Intkeys = getIntent();
        someKeys = Intkeys.getStringExtra("passKeys");


        Button btn_mycart = findViewById(R.id.btnOpenCart);
        btn_mycart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycart(someKeys);
            }
        });
        Button btn_myOrder = findViewById(R.id.btnOpenOrder);
        btn_myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myorder(someKeys);
            }
        });



    }
    public void mycart(String mkeys){
        Intent mycartIntent = new Intent(ordermanagement.this, usercartpage.class);
        mycartIntent.putExtra("passKeys", mkeys);
        startActivity(mycartIntent);


    }

    public void myorder(String mkeys){
        Intent myorderIntent = new Intent(ordermanagement.this, myorderProduct.class);
        myorderIntent.putExtra("passKeys", mkeys);
        startActivity(myorderIntent);


    }
}
