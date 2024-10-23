package com.example.pharmacyprojects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class adminorderpageAdapter extends ArrayAdapter<myorderclass> {
    private final Context context;

    public static class ViewHolder{
        ImageView orderImage_View;
        TextView UserID;
        TextView OrderID;
        TextView DrugID;
        TextView DrugName;
        TextView DrugStatus;
        TextView DrugPrice;
        TextView DrugQuantity;
        TextView total;
        TextView Address;
        TextView orderedDate;



    }

    public adminorderpageAdapter(Context context,  ArrayList<myorderclass> myadminorderlist) {
        super(context,R.layout.adminorderlistlayout,myadminorderlist);
        this.context = context;

    }
    public View getView(int position, View view, ViewGroup parent){
        myorderclass order = getItem(position);
        orderAdapter.ViewHolder viewholder;
        if(view == null){


            viewholder = new orderAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adminorderlistlayout,  parent, false);
            viewholder.orderImage_View = view.findViewById(R.id.myadmin_orderImage_product);
            viewholder.OrderID = view.findViewById(R.id.Admin_myOrderID_tvText);
            viewholder.UserID = view.findViewById(R.id.Admin_myOrderUsrID_tvText);
            viewholder.DrugID = view.findViewById(R.id.Admin_myOrderDrugID_tvText);
            viewholder.DrugName = view.findViewById(R.id.Admin_myOrder_DrugNameText);
            viewholder.DrugStatus = view.findViewById(R.id.Admin_myOrderStatusID_tvText);
            viewholder.DrugPrice = view.findViewById(R.id.Admin_myOrderPriceID_tvText);
            viewholder.DrugQuantity = view.findViewById(R.id.Admin_myOrderQtyID_tvText);
            viewholder.total = view.findViewById(R.id.Admin_myOrdertotalID_tvText);
            viewholder.Address = view.findViewById(R.id.Admin_myOrderdestination_tvText);
            viewholder.orderedDate = view.findViewById(R.id.Admin_myOrderDate_tvText);

            view.setTag(viewholder);

        }else{
            viewholder = (orderAdapter.ViewHolder) view.getTag();
        }
        //Picasso.get().load(order.getImageUrl()).into(viewholder.orderImage_View);
        Glide.with(getContext()).load(order.getImageUrl()).into(viewholder.orderImage_View);

        viewholder.OrderID.setText("Order ID: "+order.getmOrderID());
        viewholder.DrugID.setText(order.getDrugID());
        viewholder.UserID.setText(order.getUserID());
        viewholder.DrugName.setText("Product Name: "+order.getDrugName());
        if (order.getStatus() == 0){
            viewholder.DrugStatus.setText("Status: "+ "In Processing");
        }else if(order.getStatus() == 1){
            viewholder.DrugStatus.setText("Status: "+"In Transition");

        }else if(order.getStatus() == 2){
            viewholder.DrugStatus.setText("Status: "+"Delivered");
        }else if(order.getStatus() == -1){
            viewholder.DrugStatus.setText("Status: "+"Order Cancelled");

        }

        viewholder.DrugPrice.setText("Price: "+"\u20ac"+String.valueOf(order.getDrugPrice()));
        viewholder.DrugQuantity.setText("Product Quantity: "+String.valueOf(order.getDrugQuantity()));
        viewholder.total.setText("Total Price: "+"\u20ac"+String.valueOf(order.getTotal()));
        viewholder.Address.setText("Order Destination: "+order.getDeliverAddress());
        viewholder.orderedDate.setText("Date Ordered: "+order.getOrderDate());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "order ID:  "+ viewholder.OrderID.getText().toString().trim() +"  "+"User ID: "+ viewholder.UserID.getText().toString().trim(), Toast.LENGTH_LONG).show();


            }
        });

        return view;
    }
}
