package com.example.pharmacyprojects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class userproductAdapter extends ArrayAdapter<drugDbClass> {
    private final Context context;

    userpage productPage;
    public static class ViewHolder{
        ImageView productImage;
        TextView  drugID;
        TextView  drugName;
        TextView  drugDescription;
        TextView  drugPrice;
        TextView  drugQuanty;
        Spinner  drugQty;
        Button   btn_OrderButton;

    }

    public userproductAdapter(Context context, ArrayList<drugDbClass> productList) {
        super(context, R.layout.userproductlistlayout, productList);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent){
        drugDbClass drug = getItem(position);
        ViewHolder viewholder;
        if (view == null){
            viewholder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.userproductlistlayout, parent, false);
            viewholder.productImage = view.findViewById(R.id.ImageProduct_View);
            viewholder.drugID = view.findViewById(R.id.drugID_textView);
            viewholder.drugName = view.findViewById(R.id.drugName_textView);
            viewholder.drugDescription = view.findViewById(R.id.drugDescription_textView);
            viewholder.drugPrice = view.findViewById(R.id.tv_drugPrice);
            viewholder.drugQuanty = view.findViewById(R.id.drugAvailQty_textView);
            viewholder.drugQty = view.findViewById(R.id.sp_drugQuantity);
            viewholder.btn_OrderButton = view.findViewById(R.id.btn_orderdrug);
            view.setTag(viewholder);
        }else{
           viewholder = (ViewHolder) view.getTag();
        }
        //Picasso.get().load(drug.getDrugImgUrl()).into(viewholder.productImage);
        Glide.with(getContext()).load(drug.getDrugImgUrl()).into(viewholder.productImage);

        viewholder.drugID.setText(drug.getDrugID());
         viewholder.drugName.setText(drug.getDrugName());
         viewholder.drugDescription.setText(drug.getDrugDescription());
         viewholder.drugPrice.setText(String.valueOf(drug.getDrugPrice()));
         int Quantity = drug.getDrugQuantity();
         viewholder.drugQuanty.setText(String.valueOf(Quantity));
         String Xuserkey = productPage.someKeys;
         




         ArrayList<Integer> drugQuantityList = new ArrayList<Integer>();
         drugQuantityList.clear();
         for (int i = 0; i<Quantity; i++){
             drugQuantityList.add(i+1);

         }
         ArrayAdapter<Integer> spQuantity_adapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, drugQuantityList);
         viewholder.drugQty.setAdapter(spQuantity_adapter);

        viewholder.btn_OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database  = FirebaseDatabase.getInstance();
                DatabaseReference Dbref = database.getReference("CartOrder").child(Xuserkey);
                cartorderclass CartClass = new cartorderclass();
                String ID = Dbref.push().getKey();
                CartClass.setCartOrdID(ID);
                CartClass.setDrugID(viewholder.drugID.getText().toString().trim());
                CartClass.setDrugName(viewholder.drugName.getText().toString().trim());
                CartClass.setImageUrl(drug.getDrugImgUrl());
                CartClass.setProductPrice(Integer.parseInt(viewholder.drugPrice.getText().toString().trim()));
                CartClass.setProductQuantity(Integer.parseInt(viewholder.drugQty.getSelectedItem().toString().trim()));
                Dbref.child(ID).setValue(CartClass);
                Toast.makeText(getContext(), "Drug has been added to cart", Toast.LENGTH_LONG).show();




            }
        });






    return view;
    }

}
