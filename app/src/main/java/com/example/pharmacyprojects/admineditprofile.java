package com.example.pharmacyprojects;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class admineditprofile extends AppCompatActivity {
    Uri imgUri;
    ImageView imageProfile;
    String filepath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admineditprofile);
        Intent keyIntent = getIntent();
        String vKey = keyIntent.getStringExtra("userInputString");
        getUser(vKey);
        Log.e("Admin Key Edit", "Admin Key"+ vKey);
        //getUser(getAdmKeyString);
        //Gender...
        populateGender();
        //country...
        populateCountry();
        //Roles...
        populateRoles();

        imageProfile = findViewById(R.id.admEdit_imageProfile);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //My update Button
        Button  btnUpdate = findViewById(R.id.btnEdt_admEditUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAdminUserProfile(vKey);

            }
        });

        //
    }

    public void getUser(String mkeys){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = database.getReference();
        //initialize View Variables
        TextView TvKey_Str = findViewById(R.id.AdminuserTextKey);//key TextView

        TextView TvIDstr = findViewById(R.id.userTextViewID);//ID TextView

        TextView TvImageStr = findViewById(R.id.imageTextView_adminUser);//Image String

        //Image View
        imageProfile = findViewById(R.id.admEdit_imageProfile);
        //Email Address
        EditText tvEmail = findViewById(R.id.tv_admEditEmailAddress);
        //Password
        EditText tvPassword = findViewById(R.id.admUser_edtPassword);
        //Full Name
        EditText tvFullName = findViewById(R.id.Edttv_admEditfullname);
        //Gender
        EditText tvGender= findViewById(R.id.Edttv_admEditgender);
        //Spinner gender
        Spinner spGender = findViewById(R.id.spEdt_admEditGender);
        //Date of Birth
        EditText TvDob = findViewById(R.id.Edttv_admEditDob);
        //country
        EditText TvCountry = findViewById(R.id.EdTtv_admEditcountry);
        Spinner spCountry = findViewById(R.id.spEdt_admEditCountry);

        //State
        EditText TvState = findViewById(R.id.EdTtv_admEditState);
        //Address
        EditText TvAddress = findViewById(R.id.EdTtv_admEditAddress);
        //Phone number
        EditText TvPhone = findViewById(R.id.admUser_edtPhone);

        //User Roles
        EditText TvusrTypes = findViewById(R.id.EdTtv_admEditRole);
        Spinner SpRoles = findViewById(R.id.spEdt_admEditRoles);


        DatabaseReference queryLocation = MyRef.child("USER");
        Query qRef = queryLocation.orderByKey().equalTo(mkeys);

        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot Ds : snapshot.getChildren()) {

                    String ID = Ds.child("userID").getValue(String.class);//ID
                    Log.e("Admin Edit ID", "User ID:  \n  " + ID);
                    TvIDstr.setText(ID);

                    String keyString = Ds.getKey().toString();//key
                    Log.e("Admin Edit Key", "User Key:  \n" + keyString);
                    TvKey_Str.setText(keyString);

                    //image
                    String Imagelink = Ds.child("filepath").getValue(String.class);
                    Log.e("Admin Edit link", "FilePath Uri:  \n" + Imagelink);
                    TvImageStr.setText(Imagelink);
                    //Picasso.get().load(Imagelink).into(imageProfile);
                    Glide.with(getApplicationContext()).load(Imagelink).into(imageProfile);


                    //initialize the email
                    String EmailAddress = Ds.child("emailAddress").getValue(String.class);
                    Log.e("Admin Edit email", "EmailAddress:  \n" + EmailAddress);
                    tvEmail.setText(EmailAddress);

                    String Password = Ds.child("password").getValue(String.class);
                    tvPassword.setText(Password);

                    //initialize the full name
                    String Fname = Ds.child("fullName").getValue(String.class);
                    Log.e("Admin Edit fullname", "Full Name:  \n" + Fname);
                    tvFullName.setText(Fname);

                    //initialize the gender
                    String TvGender = Ds.child("gender").getValue(String.class);
                    Log.e("Admin Edit Gender", "Admin Edit Gender:  \n" + TvGender);
                    tvGender.setText(TvGender);

                    //initialize the Date of Birth
                    String strDob = Ds.child("dateOfBirth").getValue(String.class);
                    Log.e("Admin Edit DoB", "Admin Edit Dob: \n" + strDob);
                    TvDob.setText(strDob);

                    //initialize the Country
                    String strCountry = Ds.child("country").getValue(String.class);
                    Log.e("Admin Edit Country", "Admin Edit country:  \n" + strCountry);
                    TvCountry.setText(strCountry);

                    //initialize the State
                    String strState = Ds.child("state").getValue(String.class);
                    Log.e("Admin Edit State", "Admin Edit State: \n" + strState);
                    TvState.setText(strState);

                    //initialize the Address
                    String strAddress = Ds.child("residential_Address").getValue(String.class);
                    Log.e("Admin Edit Address", "Admin Edit Address: \n " + strAddress);
                    TvAddress.setText(strAddress);

                    //Phone Number
                    String phoneNumber  = Ds.child("phone").getValue(String.class);
                    Log.e("Admin Edit Phone", "Admin Edit Phone: \n " + phoneNumber);
                    TvPhone.setText(phoneNumber);

                    //user role
                    String strUserRoles = Ds.child("userType").getValue(String.class);
                    Log.e("Admin Edit Roles", "Admin Edit Roles:  \n" + strUserRoles);
                    TvusrTypes.setText(strUserRoles);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //image Picker
    //lets get some drug images
    private static final int PICK_IMAGE = 100;

    void pickImage(){

        Intent imagePicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imagePicker, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null ){
            imgUri = data.getData();
            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
            Cursor cur = managedQuery(imgUri, orientationColumn, null, null, null);
            int orientation = -1;
            if (cur != null && cur.moveToFirst()) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }

            /*
            drugImage.setImageURI(imgUri);
             filepath = imgUri.getPath().toString();
            */


            try {
                InputStream imageStream = getContentResolver().openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                switch (orientation){
                    case 90:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                    case 180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case 270:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                    default:
                        break;
                }
                imageProfile.setImageBitmap(bitmap);
                Log.e("Image Error","Image Path: "+ imgUri.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
//rotate Image
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //populate the Gender ...
    public void populateGender(){

        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_Gender = findViewById(R.id.spEdt_admEditGender);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(admineditprofile.this, android.R.layout.simple_spinner_item, deliveryData.Gender);
        drugDeliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Gender.setAdapter(drugDeliveryAdapter);


    }

    //populate country...
    public void populateCountry(){
        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_Country = findViewById(R.id.spEdt_admEditCountry);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(admineditprofile.this, android.R.layout.simple_spinner_item, deliveryData.country);
        drugDeliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Country.setAdapter(drugDeliveryAdapter);


    }


    //populate Roles...
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
                Spinner sp_rolesTxt = findViewById(R.id.spEdt_admEditRoles);
                //define the Array Adapter
                ArrayAdapter<String> spRoles_Adapter = new ArrayAdapter<>(admineditprofile.this, android.R.layout.simple_spinner_item, roleList);
                spRoles_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_rolesTxt.setAdapter(spRoles_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.toString());

            }
        });

    }
    public void UpdateAdminUserProfile(String AdminUserPassKey){
        //initialize the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Dbref= database.getReference("USER").child(AdminUserPassKey);
        //initialize View Variables
        TextView TvKey_Str = findViewById(R.id.AdminuserTextKey);//key TextView
        String StrKey = TvKey_Str.getText().toString().trim();

        TextView TvIDstr = findViewById(R.id.userTextViewID);//ID TextView
        String StrID = TvIDstr.getText().toString().trim();

        TextView TvImageStr = findViewById(R.id.imageTextView_adminUser);//Image String
        String imageStr = TvImageStr.getText().toString().trim();

        //Image View
        imageProfile = findViewById(R.id.admEdit_imageProfile);
        //Email Address
        EditText tvEmail = findViewById(R.id.tv_admEditEmailAddress);
        String strEmail = tvEmail.getText().toString().trim();

        //Password
        EditText tvPassword = findViewById(R.id.admUser_edtPassword);
        String StrPassword = tvPassword.getText().toString().trim();

        //Full Name
        EditText tvFullName = findViewById(R.id.Edttv_admEditfullname);
        String strFullName  = tvFullName.getText().toString().trim();
        //tvFullName.setText(strFullName);

        //Gender
        EditText tvGender= findViewById(R.id.Edttv_admEditgender);
        Spinner spGender = findViewById(R.id.spEdt_admEditGender);
        String strgender = spGender.getSelectedItem().toString().toString();
        tvGender.setText(strgender);

        //Date of Birth
        EditText TvDob = findViewById(R.id.Edttv_admEditDob);
        String strTvDob = TvDob.getText().toString().trim();

        //country
        EditText TvCountry = findViewById(R.id.EdTtv_admEditcountry);
        Spinner spCountry = findViewById(R.id.spEdt_admEditCountry);
        String strCountry = spCountry.getSelectedItem().toString().trim();
        TvCountry.setText(strCountry);

        //State
        EditText TvState = findViewById(R.id.EdTtv_admEditState);
        String strState = TvState.getText().toString().trim();

        //Address
        EditText TvAddress = findViewById(R.id.EdTtv_admEditAddress);
        String StrAddress = TvAddress.getText().toString().trim();

        //Phone number
        EditText TvPhone = findViewById(R.id.admUser_edtPhone);
        String StrPhone = TvPhone.getText().toString().trim();

        //User Roles
        EditText TvusrTypes = findViewById(R.id.EdTtv_admEditRole);
        Spinner SpRoles = findViewById(R.id.spEdt_admEditRoles);
        String strRoles = SpRoles.getSelectedItem().toString().trim();
        TvusrTypes.setText(strRoles);

        if(imgUri != null){
            File file = new File(imgUri.getPath());
            filepath = file.getPath().toString();
            Log.e("Image Error","image path from class"+ file.getAbsoluteFile().toString());
            final ProgressDialog prg_Dialog = new ProgressDialog(this);
            prg_Dialog.setTitle("Uploading");
            prg_Dialog.show();



            String fullname = strFullName.replaceAll(" ","");
            String mimeType = getContentResolver().getType(imgUri);
            String[] extension = mimeType.split("/");
            //creating a storage reference...
            FirebaseStorage dbStorage = FirebaseStorage.getInstance();
            StorageReference stoRef = dbStorage.getReference(fullname+StrID+"/profile"+"."+mimeType);

            //Upload Task
            UploadTask uploadTask = stoRef.putFile(imgUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        prg_Dialog.dismiss();
                        throw Objects.requireNonNull(task.getException());
                    }
                    return  stoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Map<String, Object> UserMap = new HashMap<>();
                        UserMap.put("userID", StrID);
                        UserMap.put("fullName", strFullName);
                        UserMap.put("gender", strgender);
                        UserMap.put("dateOfBirth", strTvDob);
                        UserMap.put("country", strCountry);
                        UserMap.put("state", strState);
                        UserMap.put("residential_Address", StrAddress);
                        UserMap.put("emailAddress", strEmail );
                        UserMap.put("password", StrPassword );
                        UserMap.put("phone", StrPhone );
                        Uri downloadUri = task.getResult();
                        UserMap.put("filepath", downloadUri.toString());//File Path
                        UserMap.put("userType", strRoles);
                        Dbref.updateChildren(UserMap);
                        Toast.makeText(getApplicationContext(), "Your Data has been update", Toast.LENGTH_LONG).show();
                        Log.e("Download Uri", "Download Uri: \n" + downloadUri.toString());
                        prg_Dialog.dismiss();





                    }
                }
            });



        }else{

            filepath = TvImageStr.getText().toString().trim();
            final ProgressDialog prg_Dialog = new ProgressDialog(this);
            prg_Dialog.setTitle("Uploading");
            prg_Dialog.show();


            Map<String, Object> UserMap = new HashMap<>();
            UserMap.put("userID", StrID);
            UserMap.put("fullName", strFullName);
            UserMap.put("gender", strgender);
            UserMap.put("dateOfBirth", strTvDob);
            UserMap.put("country", strCountry);
            UserMap.put("state", strState);
            UserMap.put("residential_Address", StrAddress);
            UserMap.put("emailAddress", strEmail );
            UserMap.put("password", StrPassword );
            UserMap.put("phone", StrPhone );
            UserMap.put("filepath", filepath);//File Path
            UserMap.put("userType", strRoles);
            Dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Dbref.updateChildren(UserMap);
                    Toast.makeText(getApplicationContext(), "Your Data has been update", Toast.LENGTH_LONG).show();
                    Log.e("Download Uri", "Download Uri: \n" + filepath.toString());
                    prg_Dialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

    public void DeleteUser(String keys){
        //Define the Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DbRef = database.getReference("USER").child(keys);
        DbRef.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Drug Deleted..", Toast.LENGTH_SHORT).show();



    }
}

