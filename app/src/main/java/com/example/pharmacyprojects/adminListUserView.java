package com.example.pharmacyprojects;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminListUserView extends AppCompatActivity {
    admUsrClassListview userClass;
    admUserAdapter useradapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminuserview);
        loadUserProfile();


    }

    public void loadUserProfile(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference();
        ArrayList<admUsrClassListview> UserDetails = new ArrayList<>();
        dRef.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot Ds : snapshot.getChildren()){
                    String usrID = Ds.getKey();
                    String userImageData = Ds.child("filepath").getValue(String.class);
                    String userFullName = Ds.child("fullName").getValue(String.class);
                    userClass = new admUsrClassListview(usrID, userImageData, userFullName);
                    UserDetails.add(userClass);

                }

                useradapter = new admUserAdapter(adminListUserView.this, UserDetails);
                ListView userListView = findViewById(R.id.Admin_list_User);
                userListView.setAdapter(useradapter);
                useradapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
