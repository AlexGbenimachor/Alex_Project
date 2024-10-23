package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class adminpage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpage);

        Intent Intkeys = getIntent();
        String someKeys = Intkeys.getStringExtra("passInputString");


        loaduser(someKeys);

        //initialize  a viewing button
        TextView btn_viewProfile =  findViewById(R.id.tv_admProfileButton);
        btn_viewProfile.setOnClickListener(v -> viewProfile(someKeys));


        TextView btn_viewDrug = findViewById(R.id.tv_admDrugButton);
        btn_viewDrug.setOnClickListener(v -> drugProfile());


        TextView btn_AdminUserViewProfile = findViewById(R.id.tv_btnAdmViewUserButton);
        btn_AdminUserViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminViewUserProfile();

            }
        });

        TextView btnViewNotification  = findViewById(R.id.tv_AdmViewNotificationButton);
        btnViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNotification();
            }
        });


        TextView btnViewOrders = findViewById(R.id.tv_admSettingButton);
        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    TextView EmailID_txt = findViewById(R.id.tv_AdminID);
                    EmailID_txt.setText(EmailAddress);

                    //download Image
                    ImageView profileImage;
                    String link = Ds.child("filepath").getValue(String.class);
                    profileImage = findViewById(R.id.Admin_img_profile);
                    //Picasso.get().load(link).into(profileImage);

                    Glide.with(adminpage.this).load(link).into(profileImage);


                    //Name of user
                    String Fname = Ds.child("fullName").getValue(String.class);
                    TextView tv_fullName = findViewById(R.id.tv_admName);
                    tv_fullName.setText(Fname);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    public void viewProfile(String mkeys){

        Intent viewIntent  = new Intent(adminpage.this, adminViewProfile.class);
        viewIntent.putExtra("passKeys", mkeys);
        startActivity(viewIntent);


    }

    public void drugProfile(){
        Intent drugView = new Intent(adminpage.this, viewDrug.class);
        startActivity(drugView);
    }

    public void adminViewUserProfile(){
        Intent AdminView_UserProfile = new Intent(adminpage.this, adminListUserView.class);
        startActivity(AdminView_UserProfile);
    }

    public void viewNotification(){
        Intent viewNotification_Intent = new Intent(getApplicationContext(), adminNotification.class);
        startActivity(viewNotification_Intent);
    }

}
