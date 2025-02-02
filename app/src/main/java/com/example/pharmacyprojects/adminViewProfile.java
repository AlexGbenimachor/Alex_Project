package com.example.pharmacyprojects;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

public class adminViewProfile extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminviewprofile);

        Intent keyIntent = getIntent();
        String vKey = keyIntent.getStringExtra("passKeys");
        loaduser(vKey);


    }
    public void loaduser(String keys){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = database.getReference();
        //initialize View Variables
        //Image View
        ImageView imgProfile = findViewById(R.id.adm_imageProfile);
        //Email Address
        TextView tvEmail = findViewById(R.id.tv_admEmailAddress);
        //Full Name
        TextView tvFullName = findViewById(R.id.tv_admfullname);
        //Gender
        TextView tvGender= findViewById(R.id.tv_admgender);
        //Date of Birth
        TextView TvDob = findViewById(R.id.tv_admDob);
        //country
        TextView TvCountry = findViewById(R.id.tv_admcountry);
        //State
        TextView TvState = findViewById(R.id.tv_admState);
        //Address
        TextView TvAddress = findViewById(R.id.tv_admAddress);
        //User Roles
        TextView TvusrTypes = findViewById(R.id.tv_admUserRole);



        //Query Reference
        DatabaseReference queryLocation = MyRef.child("USER");
        Query qRef = queryLocation.orderByKey().equalTo(keys);

        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot Ds : snapshot.getChildren()){
                    //image
                    String link = Ds.child("filepath").getValue(String.class);
                    //Picasso.get().load(link).into(imgProfile);
                    Glide.with(getApplicationContext()).load(link).into(imgProfile);

                    //initialize the email
                    String EmailAddress = Ds.child("emailAddress").getValue(String.class);
                    tvEmail.setText(EmailAddress);

                    //initialize the full name
                    String Fname = Ds.child("fullName").getValue(String.class);
                    tvFullName.setText(Fname);

                    //initialize the gender
                    String TvGender = Ds.child("gender").getValue(String.class);
                    tvGender.setText(TvGender);

                    //initialize the Date of Birth
                    String strDob = Ds.child("dateOfBirth").getValue(String.class);
                    TvDob.setText(strDob);

                    //initialize the Country
                    String strCountry = Ds.child("country").getValue(String.class);
                    TvCountry.setText(strCountry);

                    //initialize the State
                    String strState = Ds.child("state").getValue(String.class);
                    TvState.setText(strState);

                    //initialize the Address
                    String strAddress = Ds.child("residential_Address").getValue(String.class);
                    TvAddress.setText(strAddress);

                    //user role
                    String strUserRoles = Ds.child("userType").getValue(String.class);
                    TvusrTypes.setText(strUserRoles);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
