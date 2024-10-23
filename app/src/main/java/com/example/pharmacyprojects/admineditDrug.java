package com.example.pharmacyprojects;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class admineditDrug extends AppCompatActivity {

    //admineditDrug admEdit;

    //Initialize the Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference MyDbRef = database.getReference();
    //define Drug Model
    drugModelClass drugModel = new drugModelClass();

    //initialize the object activity
    ImageView drugImage;
    TextView tv_Drugkeys, tv_DrugID, tv_ImageDrug;
    EditText edt_DrugName_txt, edt_DrugPrice_txt, edt_DrugQuantity_txt, edt_DrugDescription_txt, edt_ManDate, edt_ExpiryDate;
    Spinner sp_DrugBrandtxt , sp_DrugDeliveryType, sp_MedicineTypes, sp_DrugSupplier;

    String filepath;
    Uri imgUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admdrugedit);


        //pass string arguments
        Intent Intkeys = getIntent();
        String someKeys = Intkeys.getStringExtra("drugKeys");
       // EditText drugName = findViewById(R.id.edt_Edit_DrugName);
       // populateEditForm(someKeys);

        //Load the suppliers to Supplier Spinner :))
        populateSupplier();

        //Load Brand into the Brand Spinner
        populateBrand();

        //Load Medicine Types...
        populateDrugType();
        //Load Drug Delivery Types
        populateDrugDelivery();


        populateEditForm(someKeys);


        //get image
        drugImage = findViewById(R.id.IV_drugImage);
        drugImage.setOnClickListener(v -> pickImage());

        //update button
        Button btn_Update = findViewById(R.id.btn_AdminEditButton);
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDrug(someKeys);
            }
        });

        //delete button
        Button btn_Delete = findViewById(R.id.btn_AdminDeleteButton);
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDrug(someKeys);
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
                Spinner sp_SupplierTxt = findViewById(R.id.sp_Adm_Edit_Supplier);
                ArrayAdapter<String> supAdapter = new ArrayAdapter<>(admineditDrug.this, android.R.layout.simple_spinner_item, supplierList);
                supAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_SupplierTxt.setAdapter(supAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

            }
        });
    }
    //populate the Brand Spinner
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
                Spinner sp_BrandTxt = findViewById(R.id.sp_Edit_DrugBrand);
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(admineditDrug.this, android.R.layout.simple_spinner_item, brandList);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_BrandTxt.setAdapter(brandAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

            }
        });

    }

    public void populateDrugDelivery(){
        drugDeliveryData deliveryData = new drugDeliveryData();
        Spinner sp_DeliveryDrug = findViewById(R.id.sp_Adm_Edit_DrugDeliveryTypes);
        ArrayAdapter<String> drugDeliveryAdapter = new ArrayAdapter<>(admineditDrug.this, android.R.layout.simple_spinner_item, deliveryData.DrugDeliveryType);
        drugDeliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_DeliveryDrug.setAdapter(drugDeliveryAdapter);


    }
    //populate Medicine Types
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
                Spinner  sp_DrugTypes_txt = findViewById(R.id.sp_Adm_Edit_MedicineTypes);
                ArrayAdapter<String> drugTypeAdapter = new ArrayAdapter<>(admineditDrug.this, android.R.layout.simple_spinner_item, D_Types);
                drugTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_DrugTypes_txt.setAdapter(drugTypeAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());

            }
        });
    }
    //we will load the data to their respective textboxes and spinners
    public void populateEditForm(String Keys){

        //Image view
        drugImage = findViewById(R.id.IV_drugImage);

        //Textview Objects for the Admin Edit block,  this holds the key and ID of the Drug Data we want to edit...
        tv_DrugID = findViewById(R.id.tv_drugID);
        tv_Drugkeys = findViewById(R.id.tv_drugKey);
        tv_ImageDrug = findViewById(R.id.tv_drugImage);
        //EditText Objects for the Admin Edit block,  this holds the drugName, Description, price, quantity of the Drug...
        edt_DrugName_txt = findViewById(R.id.edt_Edit_DrugName);
        edt_DrugDescription_txt = findViewById(R.id.edt_drugDescription);
        edt_DrugPrice_txt = findViewById(R.id.edt_drugDPrice);
        edt_DrugQuantity_txt = findViewById(R.id.edt_drugQuantity);
        edt_ManDate = findViewById(R.id.edt_drugDate);
        edt_ExpiryDate = findViewById(R.id.edt_drugExpiryDate);
        //Spinner Objects for the Admin Spinner block, this holds  for the medicineTypes, Supplier, Brand;
        sp_DrugBrandtxt = findViewById(R.id.sp_Edit_DrugBrand);
        sp_MedicineTypes = findViewById(R.id.sp_Adm_Edit_MedicineTypes);
        sp_DrugSupplier  = findViewById(R.id.sp_Adm_Edit_Supplier);
        sp_DrugDeliveryType = findViewById(R.id.sp_Adm_Edit_DrugDeliveryTypes);







        //create a query
        DatabaseReference queryLocation = MyDbRef.child("Drug");
        Query drugQuery = queryLocation.orderByKey().equalTo(Keys);
        drugQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Ds : snapshot.getChildren()){

                    filepath = Ds.child("drugFilePath").getValue(String.class);
                    tv_ImageDrug.setText(filepath);
                    //Picasso.get().load(filepath).into(drugImage);
                    Glide.with(getApplicationContext()).load(filepath).into(drugImage);

                    String xfilepath = tv_ImageDrug.getText().toString().trim();


                    Log.e("Get filepath","File Path: " + xfilepath);

                    //TextView
                    String drugkeys = Ds.getKey();
                    tv_Drugkeys.setText(drugkeys);
                    String DrugID   = Ds.child("drugID").getValue(String.class);
                    tv_DrugID.setText(DrugID);
                    String DrugName = Ds.child("drugName").getValue(String.class);

                    //EditText
                    edt_DrugName_txt.setText(DrugName);
                    String DrugDescription = Ds.child("drugDescription").getValue(String.class);
                    edt_DrugDescription_txt.setText(DrugDescription);
                    int DrugPrice = Ds.child("drugPrice").getValue(int.class);
                    edt_DrugPrice_txt.setText(String.valueOf(DrugPrice));
                    int DrugQuantity = Ds.child("drugQuantity").getValue(int.class);
                    edt_DrugQuantity_txt.setText(String.valueOf(DrugQuantity));
                    String edt_manDate = Ds.child("drugDate").getValue(String.class);
                    edt_ManDate.setText(edt_manDate);
                    String edt_ExpiryDrugDate = Ds.child("drugExpiryDate").getValue(String.class);
                    edt_ExpiryDate.setText(edt_ExpiryDrugDate);



                    //Spinner

                    String selectedBrand = Ds.child("drugBrand").getValue(String.class);
                    sp_DrugBrandtxt = findViewById(R.id.sp_Edit_DrugBrand);
                    //tv_BrandLabel
                    TextView tv_brandLabel = findViewById(R.id.tv_BrandLabel);
                    tv_brandLabel.setText(selectedBrand);
                    setSpinnerText(sp_DrugBrandtxt, selectedBrand);
                    Log.e("Selected Brand","this is the Selected: " + selectedBrand);

                    String selectedDrugDelivery = Ds.child("drugMedicineTypes").getValue(String.class);
                    sp_DrugDeliveryType = findViewById(R.id.sp_Adm_Edit_DrugDeliveryTypes);
                    TextView tv_DrugDelivery = findViewById(R.id.tv_DrugDeliveryTypeLabel);
                    tv_DrugDelivery.setText(selectedDrugDelivery);
                    //setSpinnerText(sp_DrugDeliveryType, selectedDrugDelivery);


                    String selectedSupplier = Ds.child("drugSupplier").getValue(String.class);
                    sp_DrugSupplier = findViewById(R.id.sp_Adm_Edit_Supplier);
                    //tv_SupplierLabel
                    TextView tv_supplier = findViewById(R.id.tv_SupplierLabel);
                    tv_supplier.setText(selectedSupplier);
                    setSpinnerText(sp_DrugSupplier, selectedSupplier);
                    Log.e("Selected Supplier","this is the Selected: " + selectedSupplier);

                    String selectedMedicineTypes = Ds.child("drugTypes").getValue(String.class);
                    sp_MedicineTypes = findViewById(R.id.sp_Adm_Edit_MedicineTypes);
                    //tv_MedicineTypeLabel
                    TextView tv_MedicineType = findViewById(R.id.tv_MedicineTypeLabel);
                    tv_MedicineType.setText(selectedMedicineTypes);
                    setSpinnerText(sp_MedicineTypes, selectedMedicineTypes);
                    Log.e("Selected Medicine Types","this is the Selected: " + selectedMedicineTypes);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setSpinnerText(Spinner spin, String text)
    {
        int spinPos = 0;
        if (text == null || spin.getCount()==0){
            spinPos = 0;


        }else{
            for(int i = 0; i<spin.getCount(); i++){
                spinPos = i;

            }
        }
        spin.setSelection(spinPos);

        }


        public void updateDrug(String someKeys) {
            //Define the Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference DbRef = database.getReference("Drug").child(someKeys);

            //describe drug Edit view objects
            //initialize drug variables
            //Drug ID
            TextView tv_DrugID_txt = findViewById(R.id.tv_drugID);
            String str_DrugIDText = tv_DrugID_txt.getText().toString().trim();

            //Drug Name
            EditText edt_DrugNameTxt = findViewById(R.id.edt_Edit_DrugName);
            String str_DrugNametxt = edt_DrugNameTxt.getText().toString().trim();

            //Drug Description
            EditText edt_DrugDescriptionTxt = findViewById(R.id.edt_drugDescription);
            String str_DrugDesctxt = edt_DrugDescriptionTxt.getText().toString().trim();

            //Medicine Types
            Spinner sp_DrugTypes_txt = findViewById(R.id.sp_Adm_Edit_MedicineTypes);
            String str_DrugTypes = sp_DrugTypes_txt.getSelectedItem().toString().trim();

            //Drug Brands
            Spinner sp_BrandTxt = findViewById(R.id.sp_Edit_DrugBrand);
            String str_BrandTxt = sp_BrandTxt.getSelectedItem().toString().trim();

            //Drug Quantity
            EditText edt_QuantityStockTxt = findViewById(R.id.edt_drugQuantity);
            int str_QuantityStockTxt = Integer.parseInt(edt_QuantityStockTxt.getText().toString().trim());

            //Drug Price
            EditText edt_PricePerStockTxt = findViewById(R.id.edt_drugDPrice);
            int str_PricePerStocktxt = Integer.parseInt(edt_PricePerStockTxt.getText().toString().trim());

            //Drug  Delivery types
            Spinner sp_DeliverTypesTxt = findViewById(R.id.sp_Adm_Edit_DrugDeliveryTypes);
            String str_DeliverTypes = sp_DeliverTypesTxt.getSelectedItem().toString().trim();

            //Manufacture Date
            EditText edt_ManDateTxt = findViewById(R.id.edt_drugDate);
            String str_ManDate = edt_ManDateTxt.getText().toString().trim();

            //Expiry Date
            EditText edt_ExpiryDateTxt = findViewById(R.id.edt_drugExpiryDate);
            String str_ExpiryDateTxt = edt_ExpiryDateTxt.getText().toString().trim();

            //Drug supplier
            Spinner sp_SupplierTxt = findViewById(R.id.sp_Adm_Edit_Supplier);
            String str_SuppliersTxt = sp_SupplierTxt.getSelectedItem().toString().trim();

            //drug Image textview
            TextView tv_drugImage = findViewById(R.id.tv_drugImage);

            //get image  uri
            if (imgUri != null) {
                File file = new File(imgUri.getPath());
                filepath = file.getPath().toString();
                //initialize progress Bar Dialog
                final ProgressDialog prg_Dialog = new ProgressDialog(admineditDrug.this);
                prg_Dialog.setTitle("Uploading");
                prg_Dialog.show();

                String FOLDERNAME="DRUGIMAGES/";
                String fullname = str_DrugNametxt.replaceAll(" ", "");
                String mimeType = getContentResolver().getType(imgUri);
                String[] extension = mimeType.split("/");

                Log.e("MimType", "Mimetype"+extension[1]);
                //dbRef.child(fullname+userID_txt+"/"+"myProfile"+"."+mimeType);
                //creating a storage reference...
                FirebaseStorage dbStorage = FirebaseStorage.getInstance();
                StorageReference stoRef = dbStorage.getReference(FOLDERNAME+fullname+"."+mimeType);

                //Upload Task
                UploadTask uploadTask = stoRef.putFile(imgUri);
                Task<Uri> uriTask =  uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            prg_Dialog.dismiss();
                            throw Objects.requireNonNull(task.getException());

                        }
                        return stoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            //we are creating map to pass the key and their value pair
                            Map<String, Object> drugMap = new HashMap<>();
                            drugMap.put("drugID", str_DrugIDText);//id
                            drugMap.put("drugName", str_DrugNametxt);//drug Name
                            drugMap.put("drugDescription", str_DrugDesctxt);//drug Description
                            Uri downloadUri = task.getResult();
                            drugMap.put("drugFilePath", downloadUri.toString());//drug File Path
                            drugMap.put("drugPrice", str_PricePerStocktxt);//drug Price
                            drugMap.put("drugQuantity", str_QuantityStockTxt);//drug Quantity
                            drugMap.put("drugSupplier", str_SuppliersTxt );//drug Supplier
                            drugMap.put("drugMedicineTypes", str_DrugTypes);//drug Delivery Types
                            drugMap.put("drugTypes", str_DeliverTypes);//Medicine Types
                            drugMap.put("drugBrand", str_BrandTxt);//Brand
                            drugMap.put("drugDate", str_ManDate);//drug Manufacture Date
                            drugMap.put("drugExpiryDate", str_ExpiryDateTxt);//drug Expiry Date
                            DbRef.updateChildren(drugMap);
                            Toast.makeText(getApplicationContext(), "drug information has been updated", Toast.LENGTH_LONG).show();
                            Log.e("Download Uri", "Download Uri: " + downloadUri);
                            prg_Dialog.dismiss();


                        }
                    }
                });

            }else{
                //
                filepath = tv_drugImage.getText().toString().trim();
                //initialize progress Bar Dialog
                final ProgressDialog prg_Dialog = new ProgressDialog(admineditDrug.this);
                prg_Dialog.setTitle("Uploading");
                prg_Dialog.show();

                Map<String, Object> drugMap = new HashMap<>();
                drugMap.put("drugID", str_DrugIDText);//id
                drugMap.put("drugName", str_DrugNametxt);//drug Name
                drugMap.put("drugDescription", str_DrugDesctxt);//drug Description
                drugMap.put("drugFilePath", filepath);//drug File Path
                drugMap.put("drugPrice", str_PricePerStocktxt);//drug Price
                drugMap.put("drugQuantity", str_QuantityStockTxt);//drug Quantity
                drugMap.put("drugSupplier", str_SuppliersTxt );//drug Supplier
                drugMap.put("drugMedicineTypes", str_DrugTypes);//drug Delivery Types
                drugMap.put("drugTypes", str_DeliverTypes);//Medicine Types
                drugMap.put("drugBrand", str_BrandTxt);//Brand
                drugMap.put("drugDate", str_ManDate);//drug Manufacture Date
                drugMap.put("drugExpiryDate", str_ExpiryDateTxt);//drug Expiry Date
                DbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        prg_Dialog.dismiss();
                        DbRef.updateChildren(drugMap);
                        Toast.makeText(admineditDrug.this, "drug Updated..", Toast.LENGTH_SHORT).show();
                        Log.e("Download Uri", "Download Uri: " + filepath);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(admineditDrug.this, "fail to be Updated..", Toast.LENGTH_SHORT).show();


                    }
                });




            }



        }
        public void DeleteDrug(String keys){
            //Define the Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference DbRef = database.getReference("Drug").child(keys);
            DbRef.removeValue();
            // displaying a toast message on below line.
            Toast.makeText(this, "Drug Deleted..", Toast.LENGTH_SHORT).show();



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
                drugImage.setImageBitmap(bitmap);
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

    public void setFilePath(String path){
        this.filepath = path;

    }

public String getFilepath(){
        return filepath;
}

}
