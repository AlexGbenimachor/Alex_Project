package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userpage extends AppCompatActivity {
    public static String someKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);
        //load user information the user page
        Intent Intkeys = getIntent();
        someKeys = Intkeys.getStringExtra("userInputString");


        loaduser(someKeys);

        TextView btnViewProfile = findViewById(R.id.tv_User_ViewProfileButton);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile(someKeys);
            }
        });

        TextView btnViewDrug = findViewById(R.id.tv_ViewUsersButton);
        btnViewDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userproduct(someKeys);

            }
        });

        TextView btn_OrderManagement = findViewById(R.id.tv_userOrderButton);
        btn_OrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userordermanagement(someKeys);

            }
        });



    }
    public void loaduser(String someKeys){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = database.getReference();


        //Query Reference
        DatabaseReference queryLocation = MyRef.child("USER");
        Query qRef = queryLocation.orderByKey().equalTo(someKeys);
        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot Ds : snapshot.getChildren()){

                    //Set the Email Address...
                    String EmailAddress = Ds.child("emailAddress").getValue(String.class);
                    TextView EmailID_txt = findViewById(R.id.tv_userID);
                    EmailID_txt.setText(EmailAddress);

                    //download Image
                    ImageView profileImage;
                    String link = Ds.child("filepath").getValue(String.class);
                    profileImage = findViewById(R.id.user_imgView_profile);
                    //Picasso.get().load(link).into(profileImage);
                    Glide.with(getApplicationContext()).load(link).into(profileImage);




                    //Name of user
                    String Fname = Ds.child("fullName").getValue(String.class);
                    TextView tv_fullName = findViewById(R.id.tv_userName);
                    tv_fullName.setText(Fname);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void viewProfile(String mkeys){

        Intent viewIntent  = new Intent(userpage.this, viewprofile.class);
        viewIntent.putExtra("passKeys", mkeys);
        startActivity(viewIntent);


    }

    public void userproduct(String mkeys){
        Intent userproductIntent = new Intent(userpage.this, userproductpage.class);
        userproductIntent.putExtra("passKeys", mkeys);
        startActivity(userproductIntent);
    }
    public void userordermanagement(String mkeys){
        Intent ordermanIntent = new Intent(userpage.this, ordermanagement.class);
        ordermanIntent.putExtra("passKeys", mkeys);
        startActivity(ordermanIntent);

    }

}
