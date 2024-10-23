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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class registerUser extends AppCompatActivity {
    ImageView imageView;
    public static Uri imgUri;
    //GetData GD = new GetData();
    UserClass User = new UserClass();
    Spinner sp_gender_txt, sp_country_txt, sp_user_role_txt;
    EditText edt_UserID_txt, edt_FullName_txt, edt_Date_txt, edt_State_txt, edt_EmailAddress_txt, edt_PasswordText_txt, edt_phoneNumber_txt, edt_Address_txt, edtUser_role_txt;
    String userID_txt, fullName_txt, Date_txt,  State_txt, EmailAddress_txt, password_txt, phone_txt, gender_txt, userType_txt, country_txt, Address_txt, UsersRole_text;
    @Override


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerationform);
        setUserID();//set the user ID...
        populateRoles();//populate the user roles
        populateGender();//populate the Gender
        populateCountry();//populate the country

        imageView = findViewById(R.id.reg_imagView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        Button btn_AddUsers = findViewById(R.id.btnAdduser);
        btn_AddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adduserToDb();
            }
        });




    }
    //generating userID
    public void setUserID(){
        //initialize the Database


        FirebaseDatabase myDB = FirebaseDatabase.getInstance();
        DatabaseReference UserRef = myDB.getReference();
        //User Reference
        UserRef.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    //here we have to check for the last entered userid and update
                    //initialize a table the holding the User-table
                    DatabaseReference UserTable = UserRef.child("USER");
                    Query Uquery = UserTable.orderByKey().limitToLast(1);
                    Uquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                String nID = ds.child("userID").getValue(String.class);
                                String[] partIDs = nID.split("-");
                                String PartID_1 = partIDs[0];
                                String partID_2 = partIDs[1];
                                int uID = Integer.parseInt(partID_2) + 1;
                                String nuID = PartID_1+"-"+ uID;
                                Log.e("New ID", "that ID: "+ nuID);
                                edt_UserID_txt = findViewById(R.id.reg_edt_userID);
                                edt_UserID_txt.setText(nuID);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.e("Error","yes");
                }else if(!snapshot.exists()){
                    //if not userID exist(), we generate and return ID...
                    DatabaseReference tblID_Ref = UserRef.child("DB_TBL_ID");
                    Query qRef = tblID_Ref.orderByChild("id_text").equalTo(2);
                    qRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                String userID = ds.child("tblid_txt").getValue(String.class);
                                assert userID != null;
                                String[]  newID  = userID.split("-");
                                String part1  = newID[0];
                                String part2  = newID[1];
                                int part12 = Integer.parseInt(part2) + 1;
                                String ID = part1+"-"+ part12;

                                edt_UserID_txt = findViewById(R.id.reg_edt_userID);
                                edt_UserID_txt.setText(ID);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Database Error", "Data not Found", error.toException());

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", "Data not Found", error.toException());


            }
        });



    }
    //populate the Gender ...
    public void populateGender(){

        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_Gender = findViewById(R.id.reg_sp_gender);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(registerUser.this, android.R.layout.simple_spinner_item, deliveryData.Gender);
        drugDeliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Gender.setAdapter(drugDeliveryAdapter);


    }

    //populate country...
    public void populateCountry(){
        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_Country = findViewById(R.id.reg_sp_country);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(registerUser.this, android.R.layout.simple_spinner_item, deliveryData.country);
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
                Spinner sp_rolesTxt = findViewById(R.id.reg_sp_usRoles);
                //define the Array Adapter
                ArrayAdapter<String> spRoles_Adapter = new ArrayAdapter<>(registerUser.this, android.R.layout.simple_spinner_item, roleList);
                spRoles_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_rolesTxt.setAdapter(spRoles_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.toString());

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
                imageView.setImageBitmap(bitmap);
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

//add users

public void adduserToDb(){

        //initialize the view objects...

    //1) get UserID
    edt_UserID_txt = findViewById(R.id.reg_edt_userID);
    userID_txt = edt_UserID_txt.getText().toString().trim();
    Log.e("User ID", "My User ID" + userID_txt);

    //2) Get Full Name
    edt_FullName_txt = findViewById(R.id.reg_edt_Name);
    fullName_txt = edt_FullName_txt.getText().toString().trim();
    Log.e("Full Name", "Full Name" + fullName_txt);

    //3) Date of Birth
    edt_Date_txt = findViewById(R.id.reg_edt_Date);
    Date_txt = edt_Date_txt.getText().toString().trim();
    Log.e("Birth Date", "Some Date " + Date_txt);
    //4) Gender
    sp_gender_txt = findViewById(R.id.reg_sp_gender);
    gender_txt = sp_gender_txt.getSelectedItem().toString();
    Log.e("Gender", "Gender " + gender_txt);


    //6) country
    sp_country_txt = findViewById(R.id.reg_sp_country);
    country_txt = sp_country_txt.getSelectedItem().toString();
    Log.e("Country", "Country"+country_txt);


    //7 Enter State
    edt_State_txt = findViewById(R.id.reg_edt_State);
    State_txt = edt_State_txt.getText().toString().trim();
    Log.e("Country", "Country"+country_txt);

    //8 Residential Address
    edt_Address_txt = findViewById(R.id.reg_edt_address);
    Address_txt =edt_Address_txt.getText().toString().trim();
    Log.e("Address", "Address"+Address_txt);

    //9 phone Number
    edt_phoneNumber_txt = findViewById(R.id.reg_edt_phone);
    phone_txt = edt_phoneNumber_txt.getText().toString().trim();
    Log.e("Phone Number ", "Phone Number "+phone_txt);

    //10 Email Address
    edt_EmailAddress_txt = findViewById(R.id.reg_edt_email);
    EmailAddress_txt = edt_EmailAddress_txt.getText().toString().trim();
    Log.e("Email Address", "Email Address"+EmailAddress_txt);

    //11 Password
    edt_PasswordText_txt = findViewById(R.id.reg_edt_password);
    password_txt = edt_PasswordText_txt.getText().toString().trim();
    Log.e("Password", "Password"+password_txt);

    //12 User Type using Spinners...


    sp_user_role_txt = findViewById(R.id.reg_sp_usRoles);
    UsersRole_text = sp_user_role_txt.getSelectedItem().toString().trim();
    Log.e("User Role", "user Role"+UsersRole_text);





    if (imgUri != null && checkUser()==true){
        File file = new File(imgUri.getPath());

        Log.e("Image Error","image path from class"+ file.getAbsoluteFile().toString());
        //initialize progress Bar Dialog
        final ProgressDialog prg_Dialog = new ProgressDialog(this);
        prg_Dialog.setTitle("Uploading");
        prg_Dialog.show();
        //creating a Database reference...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MyDbRef = database.getReference().child("USER");
        Log.e("Database ", "Dbref "+MyDbRef);


        //creating a storage reference...
        FirebaseStorage dbStorage = FirebaseStorage.getInstance();

        String fullname = fullName_txt.replaceAll(" ","");

        String mimeType = getContentResolver().getType(imgUri);
        String[] extension = mimeType.split("/");

        Log.e("MimType", "Mimetype"+extension[1]);
        //dbRef.child(fullname+userID_txt+"/"+"myProfile"+"."+mimeType);
        StorageReference dbRef = dbStorage.getReference(fullname+userID_txt+"/profile"+"."+mimeType);
        //upload task...
        UploadTask uploadTask = dbRef.putFile(imgUri);
        Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful()){
                prg_Dialog.dismiss();
                throw Objects.requireNonNull(task.getException());
            }
            return dbRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){


                User.setUserID(userID_txt);//1
                User.setFullName(fullName_txt);//2
                User.setGender(gender_txt);//3
                User.setDateOfBirth(Date_txt);//4
                User.setCountry(country_txt);//5
                User.setState(State_txt);//6
                User.setResidential_Address(Address_txt);//7
                User.setPhone(phone_txt);//8
                User.setEmailAddress(EmailAddress_txt);//9
                User.setPassword(password_txt);//10
                User.setUserType(UsersRole_text);//11
                Uri downloadUri = task.getResult();
                User.setFilepath(downloadUri.toString());//12 Note: always save uri as string
                MyDbRef.push().setValue(User);


                Toast.makeText(getApplicationContext(), "You've Registered", Toast.LENGTH_LONG).show();
                Log.e("Download Uri", "Download Uri: " + downloadUri);
                prg_Dialog.dismiss();




            }else{
                Log.e("Database","Data was not inserted");
            }
        }).addOnFailureListener(e -> {
            prg_Dialog.dismiss();
            e.printStackTrace();

        });

    }
}
//check users
    public boolean checkUser(){
        //define the password
        edt_PasswordText_txt = findViewById(R.id.reg_edt_password);
        EditText edt_confirmPassword = findViewById(R.id.edt_confirmPassword);
        if(edt_PasswordText_txt.equals(edt_confirmPassword)){

         return true;
        }else{

            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
        }

        return false;
    }
//clear objects...


}


