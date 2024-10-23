package com.example.pharmacyprojects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class admUserAdapter extends ArrayAdapter<admUsrClassListview> {

    private final Context context;

    //create activity cache for storing the activity object...
    public static class ViewHolder {

        ImageView adm_USrprofileImage;
        TextView tv_admUSrID;
        TextView tv_AdmName;
        Button btn_admEdit;
        Button btn_admDelete;

    }

    public admUserAdapter(Context context, ArrayList<admUsrClassListview> admUserList) {
        super(context, R.layout.adminuserlistviewlayoutinterface, admUserList);


        this.context = context;
    }

    //define View class for displaying the detail information of the product...
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        admUsrClassListview admUsrClassDb  = getItem(position);
        //display the viewholder that holds interface objects...
        ViewHolder viewholder;
        if(view == null){
            viewholder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adminuserlistviewlayoutinterface, parent, false);
            viewholder.adm_USrprofileImage= view.findViewById(R.id.adminuserimageview);
            viewholder.tv_admUSrID = view.findViewById(R.id.tv_usrkeyID);
            viewholder.tv_AdmName= view.findViewById(R.id.tv_admUsrFullName);
            viewholder.btn_admEdit = view.findViewById(R.id.btn_AdmEditUsername);
            viewholder.btn_admDelete = view.findViewById(R.id.btn_AdmDeleteUsername);
            view.setTag(viewholder);

        }else{
            viewholder = (ViewHolder) view.getTag();
        }

        //load image from url into the listview imageview object...
        Glide.with(getContext()).load(admUsrClassDb.getFilePath()).into(viewholder.adm_USrprofileImage);

        //Picasso.get().load(admUsrClassDb.getFilePath()).into(viewholder.adm_USrprofileImage);
        viewholder.tv_admUSrID.setText(admUsrClassDb.getUsrIDkey());//user ID
        viewholder.tv_AdmName.setText(admUsrClassDb.getFullName());//get user full name
        //Edit Button
        viewholder.btn_admEdit.setOnClickListener(v -> {

            Toast.makeText(context, "Position of Edit button" + position + "Edit Name:" + viewholder.tv_AdmName.getText().toString().trim() + "user ID:" + viewholder.tv_admUSrID.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            AdminUserEdit(viewholder.tv_admUSrID.getText().toString().trim());


        });
        //Delete Button
        viewholder.btn_admDelete.setOnClickListener(v -> {
            Toast.makeText(context, "Position of Delete button" + position + "Edit Name:" + viewholder.tv_AdmName.getText().toString().trim() + "user ID:" + viewholder.tv_admUSrID.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        });







    return view;
    }
    public void AdminUserEdit(String mkeys){
        Intent userAdminIntent = new Intent(getContext(), admineditprofile.class);
        userAdminIntent.putExtra("userInputString", mkeys);
        getContext().startActivity(userAdminIntent);
    }
}