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

public class drugform  extends AppCompatActivity {
    ImageView imageView;
    public static Uri imgUri;
    //Initialize the Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference MyDbRef = database.getReference();
    //define Drug Model
    drugModelClass drugDb = new drugModelClass();
    //Spinner
    Spinner sp_DrugTypes_txt;
    Spinner sp_BrandTxt;
    Spinner sp_DeliverTypesTxt;
    Spinner sp_SupplierTxt;

    //EditText variables
    EditText edt_DrugID_txt;
    EditText edt_DrugNameTxt;
    EditText edt_DrugDescription;
    EditText edt_QuantityStockTxt;
    EditText edt_PricePerStockTxt;
    EditText edt_ManDateTxt;
    EditText edt_ExpiryDateTxt;

    //String
    String str_DrugIDText;
    String str_DrugNametxt;
    String str_DrugDescription;
    String str_DrugTypes;
    String str_BrandTxt;
    int str_QuantityStockTxt;
    int str_PricePerStocktxt;
    String str_ManDate;
    String str_ExpiryDateTxt;
    String str_SuppliersTxt;
    String str_DeliverTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugform);
        setDrugID();//set drug ID
        populateBrand();//populate the Brand spinner
        populateDrugDelivery();//populate the Drug Delivery Type(Medicine Type)
        populateDrugType();//Drug types
        populateSupplier();//populate of the SUpplier

        imageView = findViewById(R.id.drugImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        Button btn_AddDrugs  = findViewById(R.id.btn_AddDrugs);
        btn_AddDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDrugData();
            }
        });


    }//this will generate the DrugID
    public void setDrugID(){
        //initialize the Database


        FirebaseDatabase myDB = FirebaseDatabase.getInstance();
        DatabaseReference UserRef = myDB.getReference();
        //User Reference
        UserRef.child("Drug").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    //here we have to check for the last entered userid and update
                    //initialize a table the holding the User-table
                    DatabaseReference UserTable = UserRef.child("Drug");
                    Query Uquery = UserTable.orderByKey().limitToLast(1);
                    Uquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                String nID = ds.child("drugID").getValue(String.class);
                                String[] partIDs = nID.split("-");
                                String PartID_1 = partIDs[0];
                                String partID_2 = partIDs[1];
                                int uID = Integer.parseInt(partID_2) + 1;
                                String nuID = PartID_1+"-"+ uID;
                                Log.e("New ID", "that ID: "+ nuID);
                                TextView tv_DrugID_txt = findViewById(R.id.admAddDrug_edt_drugID);
                                tv_DrugID_txt.setText(nuID);

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
                    Query qRef = tblID_Ref.orderByChild("id_text").equalTo(1);
                    ((Query) qRef).addValueEventListener(new ValueEventListener() {
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

                               TextView tv_DrugID_txt = findViewById(R.id.admAddDrug_edt_drugID);
                                tv_DrugID_txt.setText(ID);

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
    public void populateBrand(){
        List<String> brandList  = new ArrayList<>();
        MyDbRef.child("Brand").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot brandName: snapshot.getChildren()){
                    String brands = brandName.child("brandName").getValue(String.class);
                    //Log.e("Brand Name Error","Brand Name: "+brands);
                    if(brands != null){
                        brandList.add(brands);
                    }


                }
                Spinner sp_BrandTxt = findViewById(R.id.admAddDrug_sp_brandText);
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(drugform.this, android.R.layout.simple_spinner_item, brandList);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_BrandTxt.setAdapter(brandAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

            }
        });

    }
    //populate the Medicine delivery type
    public void populateDrugDelivery(){
        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_DeliveryDrug = findViewById(R.id.admAddDrug_sp_DrugDeliveryType);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(drugform.this, android.R.layout.simple_spinner_item, deliveryData.DrugDeliveryType);
        drugDeliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_DeliveryDrug.setAdapter(drugDeliveryAdapter);


    }
    //populate drug  Types
    public  void populateDrugType(){

        List<String> D_Types = new ArrayList<>();
        MyDbRef.child("DrugType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dtype : snapshot.getChildren()){
                    String drugTypes = dtype.child("drugType").getValue(String.class);
                    if(drugTypes != null){
                        D_Types.add(drugTypes);
                    }

                }
                //sp_Adm_Edit_DrugDeliveryTypes
                Spinner  sp_DrugTypes_txt = findViewById(R.id.admDrugAdd_sp_DrugTypes);
                ArrayAdapter<String> drugTypeAdapter = new ArrayAdapter<>(drugform.this, android.R.layout.simple_spinner_item, D_Types);
                drugTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_DrugTypes_txt.setAdapter(drugTypeAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

            }
        });
    }

    //populate the Supplier Spinners...
    public void populateSupplier(){
        List<String> supplierList = new ArrayList<>();
        MyDbRef.child("Supplier").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot supSnap : snapshot.getChildren()){
                    String supplierName = supSnap.child("supplierName_str").getValue(String.class);
                    if (supplierName != null){
                        //Log.e("Supplier Name Error","Supplier Name: "+ supplierName);
                        supplierList.add(supplierName);


                    }

                }
                sp_SupplierTxt = findViewById(R.id.admAddDrug_sp_SuppliersPop);
                ArrayAdapter<String> supAdapter = new ArrayAdapter<>(drugform.this, android.R.layout.simple_spinner_item, supplierList);
                supAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_SupplierTxt.setAdapter(supAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

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
    //Add Drug Class
    public void addDrugData(){
        //initialize drug variables
        //Drug ID
        TextView tv_DrugID_txt = findViewById(R.id.admAddDrug_edt_drugID);
        String str_DrugIDTextView = tv_DrugID_txt.getText().toString().trim();
        Log.e("Drug ID:","String Drug ID:" + str_DrugIDTextView);

        //Drug Name
        edt_DrugNameTxt = findViewById(R.id.admAddDrug_edt_drugName);
        str_DrugNametxt = edt_DrugNameTxt.getText().toString().trim();
        Log.e("Drug Name:","String Drug Name:" + str_DrugNametxt);

        //Drug Description
        edt_DrugDescription = findViewById(R.id.admAddDrug_edt_drugDescription);
        String str_DrugDescription = edt_DrugDescription.getText().toString().trim();
        Log.e("Drug Description:","String Drug Description:" + str_DrugDescription);

        //Drug Types
        sp_DrugTypes_txt = findViewById(R.id.admDrugAdd_sp_DrugTypes);
        str_DrugTypes =  sp_DrugTypes_txt.getSelectedItem().toString().trim();
        Log.e("Drug Types:","String Drug Types:" + str_DrugTypes);

        //Drug Brands
        sp_BrandTxt = findViewById(R.id.admAddDrug_sp_brandText);
        str_BrandTxt = sp_BrandTxt.getSelectedItem().toString().trim();
        Log.e("Drug Brand:","String Drug Brand:" +  str_BrandTxt);

        //Drug Quantity
        edt_QuantityStockTxt = findViewById(R.id.admAddDrug_edt_quantityStock);
        str_QuantityStockTxt = Integer.parseInt(edt_QuantityStockTxt.getText().toString().trim());
        Log.e("Drug Quantity:","String Drug Quantity: " +  str_QuantityStockTxt);

        //Drug Price
        edt_PricePerStockTxt = findViewById(R.id.admAddDrug_edt_priceMedicine);
        str_PricePerStocktxt = Integer.parseInt(edt_PricePerStockTxt.getText().toString().trim());
        Log.e("Drug Price:","String Drug Price: " +  str_PricePerStocktxt);

        //Drug  Delivery types
        sp_DeliverTypesTxt = findViewById(R.id.admAddDrug_sp_DrugDeliveryType);
        str_DeliverTypes = sp_DeliverTypesTxt.getSelectedItem().toString().trim();
        Log.e("Drug Delivery types:","String Drug Delivery types: " +  str_DeliverTypes);

        //Manufacture Date
        edt_ManDateTxt = findViewById(R.id.admAddDrug_edt_ManufactureDate);
        str_ManDate = edt_ManDateTxt.getText().toString().trim();
        Log.e("Drug Manufacture Date:","Manufacture Date: " + str_ManDate);



        //Expiry Date
        edt_ExpiryDateTxt = findViewById(R.id.admAddDrug_edt_ExpiryDate);
        str_ExpiryDateTxt = edt_ExpiryDateTxt.getText().toString().trim();
        Log.e("Drug Expiry Date:","Expiry Date: " + str_ExpiryDateTxt);

        //Drug supplier
        sp_SupplierTxt = findViewById(R.id.admAddDrug_sp_SuppliersPop);
        str_SuppliersTxt = sp_SupplierTxt.getSelectedItem().toString().trim();
        Log.e("Drug Supplier:","Supplier: " + str_SuppliersTxt);


        if(imgUri != null){
            File file = new File(imgUri.getPath());
            Log.e("Show My Drug ID", "Drug ID is: " + str_DrugIDTextView);

            Log.e("Image Error","image path from class"+ file.getAbsoluteFile().toString());

            //initialize progress Bar Dialog
            final ProgressDialog prg_Dialog = new ProgressDialog(drugform.this);
            prg_Dialog.setTitle("Uploading");
            prg_Dialog.show();
            //creating a Database reference...
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference MyDbRef = database.getReference().child("Drug");
            Log.e("Database ", "Dbref "+MyDbRef);

            String FOLDERNAME="DRUGIMAGES/";



            //creating a storage reference...
            FirebaseStorage dbStorage = FirebaseStorage.getInstance();
            String fullname = str_DrugNametxt.replaceAll(" ","");

            String mimeType = getContentResolver().getType(imgUri);
            String[] extension = mimeType.split("/");

            Log.e("MimType", "Mimetype"+extension[1]);
            //dbRef.child(fullname+userID_txt+"/"+"myProfile"+"."+mimeType);
            StorageReference dbRef = dbStorage.getReference(FOLDERNAME+fullname+"."+mimeType);
            //upload task...
            UploadTask uploadTask = dbRef.putFile(imgUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        prg_Dialog.dismiss();
                        throw Objects.requireNonNull(task.getException());

                    }
                    return dbRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Log.e("on complete error","this is my ID: " +str_DrugIDTextView );
                        drugDb.setDrugID(str_DrugIDTextView);
                        drugDb.setDrugName(str_DrugNametxt);
                        drugDb.setDrugDescription(str_DrugDescription);
                        drugDb.setDrugTypes(str_DrugTypes);
                        drugDb.setDrugBrand(str_BrandTxt);
                        drugDb.setDrugQuantity(str_QuantityStockTxt);
                        drugDb.setDrugPrice(str_PricePerStocktxt);
                        drugDb.setDrugMedicineTypes(str_DeliverTypes);
                        drugDb.setDrugDate(str_ManDate);
                        drugDb.setDrugExpiryDate(str_ExpiryDateTxt);
                        drugDb.setDrugSupplier(str_SuppliersTxt);
                        Uri downloadUri = task.getResult();
                        drugDb.setDrugFilePath(downloadUri.toString());
                        MyDbRef.push().setValue(drugDb);
                        Toast.makeText(getApplicationContext(), "file has been uploaded", Toast.LENGTH_LONG).show();
                        Log.e("Download Uri", "Download Uri: " + downloadUri.toString());
                        prg_Dialog.dismiss();



                    }
                }
            });



        }





    }


}
