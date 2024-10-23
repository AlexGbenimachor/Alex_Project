package com.example.pharmacyprojects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Psalm 46
    /*Name: Ugochukwu Alex Gbenimachor
      Matricola: 1722987
      Project Title: Pharmacy project
      Aim: the project is aimed to help users order their over the counter medications from their local Pharmacy store...
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateRoles();

        Button btn_login = findViewById(R.id.btnLogin);
        btn_login.setOnClickListener(v -> validateUser());

        TextView openTvSignup = findViewById(R.id.tv_btnsignup);
        openTvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();

            }
        });



    }

    public void validateUser(){
        //initialize the database and the Table reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        //initialize the need variables
        EditText edt_emailAddress = findViewById(R.id.edt_EmailAddress);
        EditText edt_passwords = findViewById(R.id.edt_Password);
        Spinner sp_Roles = findViewById(R.id.sp_UserRole);
        String IsAdmin = "Admin";
        String Iscostumer = "customer";
        String str_emailAddress = edt_emailAddress.getText().toString().trim();
        String str_password = edt_passwords.getText().toString().trim();
        String str_roles = sp_Roles.getSelectedItem().toString().trim();


        //create a query
        DatabaseReference queryLocation = dbRef.child("USER");
        Query usrQuery = queryLocation.orderByChild("emailAddress").equalTo(str_emailAddress);
        usrQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //accessing USER table
                    if (ds.exists()){
                        String emailAddress_txt = ds.child("emailAddress").getValue(String.class);
                        String password_txt = ds.child("password").getValue(String.class);
                        String roleType_txt = ds.child("userType").getValue(String.class);
                        String keys = ds.getKey();
                        if (password_txt.equals(str_password) && emailAddress_txt.equals(str_emailAddress) && IsAdmin.equals(str_roles) && roleType_txt.equals(IsAdmin) ){
                            Intent adminIntent = new Intent(MainActivity.this, adminpage.class);
                            adminIntent.putExtra("passInputString", keys);
                            startActivity(adminIntent);
                            //Toast.makeText(MainActivity.this, "Login success admin page is under construction", Toast.LENGTH_LONG).show();

                        }else if(password_txt.equals(str_password) && emailAddress_txt.equals(str_emailAddress) && Iscostumer.equals(str_roles) && roleType_txt.equals(Iscostumer) ){
                            //Toast.makeText(MainActivity.this, "customer page is under construction", Toast.LENGTH_LONG).show();
                            Intent userIntent = new Intent(MainActivity.this, userpage.class);
                            userIntent.putExtra("userInputString", keys);
                            startActivity(userIntent);


                        }else{
                            Toast.makeText(MainActivity.this, "Login is unsuccessful", Toast.LENGTH_LONG).show();

                        }

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Database Error", error.getMessage());

            }
        });







    }
    public void populateRoles(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        Log.e("Database Reference"," Reference " + myRef);

        myRef.child("User_Role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> roleList =  new ArrayList<>();

                for(DataSnapshot Ds : snapshot.getChildren()){

                    String roles = Ds.child("roleName").getValue(String.class);
                    roleList.add(roles);
                    Log.e("Role Error","user roles" + roles);


                }
                Spinner sp_rolesTxt = findViewById(R.id.sp_UserRole);
                //define the Array Adapter
                ArrayAdapter<String> spRoles_Adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, roleList);
                spRoles_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_rolesTxt.setAdapter(spRoles_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.toString());

            }
        });

    }
    public void RegisterUser(){
        Intent usrSignUp = new Intent(MainActivity.this , registerUser.class);
        startActivity(usrSignUp);
    }
}