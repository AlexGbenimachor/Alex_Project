package com.example.pharmacyprojects;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class drugAdapter extends ArrayAdapter<drugDbClass>{



    //create a view holder for activity objects
    private static class ViewHolder{
        ImageView drugImage;
        TextView drugID;
        TextView drugName;
        TextView drugPrice;
        TextView drugQuantity;
        TextView drugDescription;

    }

    //the Custom Adapter class for the listView...
    public drugAdapter(Context context, ArrayList<drugDbClass> drugList) {
        super(context, R.layout.drugviewinterface, drugList);

    }

    //define View class for displaying the detail information of the product...
    @Override
    public View getView(int position, View view, ViewGroup parent){
        //we get Item of our stored data given their position...
        drugDbClass drug = getItem(position);
        //call of viewholder class
        ViewHolder viewholder;
        if(view == null){
            viewholder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.drugviewinterface, parent, false);
            viewholder.drugID = view.findViewById(R.id.tv_productID);
            viewholder.drugImage = view.findViewById(R.id.productImage);
            viewholder.drugName  = view.findViewById(R.id.tv_productName);
            viewholder.drugDescription = view.findViewById(R.id.tv_productDescription);
            viewholder.drugPrice = view.findViewById(R.id.tv_productPrice);
            viewholder.drugQuantity = view.findViewById(R.id.tv_productQuantity);
            view.setTag(viewholder);
            

        }else{
            viewholder = (ViewHolder) view.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        String euro = "\u20ac";
        String pound = "\u00a3";
        //Picasso.get().load(drug.getDrugImgUrl()).into(viewholder.drugImage);
        Glide.with(getContext()).load(drug.getDrugImgUrl()).into(viewholder.drugImage);
        viewholder.drugID.setText(drug.getDrugID());
        viewholder.drugName.setText(drug.getDrugName());
        viewholder.drugDescription.setText(drug.getDrugDescription());
        viewholder.drugPrice.setText(euro+String.valueOf(drug.getDrugPrice()));
        viewholder.drugQuantity.setText(String.valueOf(drug.getDrugQuantity()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Drug ID:"+viewholder.drugID.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                editDrug( viewholder.drugID.getText().toString().trim());

            }
        });

        return view;

    }
    public void editDrug(String mkeys){
        Intent AdminDrugEditIntent = new Intent(getContext(), admineditDrug.class);
        AdminDrugEditIntent.putExtra("drugKeys", mkeys);
        getContext().startActivity(AdminDrugEditIntent);
    }

}
