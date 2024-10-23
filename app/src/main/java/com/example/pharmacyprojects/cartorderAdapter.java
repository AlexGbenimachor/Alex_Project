package com.example.pharmacyprojects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cartorderAdapter extends ArrayAdapter<cartorderclass> {
    private final Context context;
    int totalQuantity;
    String UserName;
    String userAddress;
    String[] userName_Address;
    userpage productPage;
    //define the activity objects...
    public static class ViewHolder{
        TextView UserID;
        TextView CartOrderID;
        TextView DrugID;
        TextView DrugName;
        ImageView DrugImgUrl;
        TextView DrugPrice;
        TextView DrugQuantity;
        TextView drgQty;
        Button btnRemoveOrder;
        Button btnConfirmOrder;

    }


    public cartorderAdapter(Context context,  ArrayList<cartorderclass> cartlist) {
        super(context,R.layout.userorderlistinterface, cartlist);
        this.context = context;


    }
    public View getView(int position, View view, ViewGroup parent){
        cartorderclass  cart =  getItem(position);
        ViewHolder viewholder;
        if(view == null){
            viewholder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.userorderlistinterface, parent, false);
            //passing variable to activity objects...
            viewholder.CartOrderID = view.findViewById(R.id.orderCartID_tv);
            viewholder.DrugID = view.findViewById(R.id.orderProductID_tv);
            viewholder.DrugImgUrl = view.findViewById(R.id.orderImage_product);
            viewholder.DrugName = view.findViewById(R.id.orderProductName_tv);
            viewholder.DrugPrice = view.findViewById(R.id.orderProductPrice_tv);
            viewholder.DrugQuantity = view.findViewById(R.id.orderProductQty_tv);
            viewholder.drgQty = view.findViewById(R.id.orderProductQunt_tv);
            viewholder.btnRemoveOrder= view.findViewById(R.id.btn_RemoveOrder);
            viewholder.btnConfirmOrder = view.findViewById(R.id.btn_OrderNow);
            view.setTag(viewholder);

        }else{
            viewholder = (ViewHolder) view.getTag();
        }
        int total;
        //Picasso.get().load(cart.getImageUrl()).into(viewholder.DrugImgUrl);
        Glide.with(getContext()).load(cart.getImageUrl()).into(viewholder.DrugImgUrl);
        viewholder.CartOrderID.setText(cart.getCartOrdID());
        viewholder.DrugID.setText(cart.getDrugID());
        viewholder.DrugName.setText(cart.getDrugName());
        viewholder.DrugPrice.setText(String.valueOf(cart.getProductPrice()));
        viewholder.DrugQuantity.setText(String.valueOf(cart.getProductQuantity()));
        total =  cart.getProductPrice() * cart.getProductQuantity();
        viewholder.drgQty.setText("\u20ac"+String.valueOf(total));
        usercartpage cartpage =  new  usercartpage();
        String userKey = productPage.someKeys;
        Log.e("user Key","user key cartpage: "+userKey);
        userName_Address = getAddress(userKey);

        viewholder.btnRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "remove ID"+viewholder.CartOrderID.getText().toString().trim() + viewholder.DrugID.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                deleteCart(userKey, viewholder.CartOrderID.getText().toString().trim());
                Toast.makeText(getContext(), "Drug has been removed from cart!", Toast.LENGTH_LONG).show();




            }
        });

        viewholder.btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Dbref = database.getReference("MyOrder").child(userKey);
                myorderclass orderclass = new myorderclass();



                Toast.makeText(getContext(), "Confirm ID"+viewholder.CartOrderID.getText().toString().trim() + viewholder.DrugID.getText().toString().trim()+" "+ userName_Address[0], Toast.LENGTH_SHORT).show();
                String orderID = Dbref.push().getKey();
                orderclass.setmOrderID(orderID);
                orderclass.setDrugID(viewholder.DrugID.getText().toString().trim());
                orderclass.setDrugName(viewholder.DrugName.getText().toString().trim());
                orderclass.setUserName(userName_Address[1]);
                orderclass.setImageUrl(cart.getImageUrl());
                orderclass.setDrugPrice(Integer.parseInt(viewholder.DrugPrice.getText().toString().trim()));
                orderclass.setDrugQuantity(Integer.parseInt(viewholder.DrugQuantity.getText().toString().trim()));
                orderclass.setStatus(0);//add zero for processing
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                orderclass.setOrderDate(thisDate);//current Date
                int price = Integer.parseInt(viewholder.DrugPrice.getText().toString().trim());
                int quantity = Integer.parseInt(viewholder.DrugQuantity.getText().toString().trim());
                int total =  price * quantity;
                orderclass.setTotal(total);
                orderclass.setDeliverAddress(userName_Address[0]);
                /*Dbref.child(orderID).setValue(orderclass):
                this function create my order table
                and populates the table the cart
                order after confirmation has been made*/
                Dbref.child(orderID).setValue(orderclass);
                Toast.makeText(getContext(), "Order has been added", Toast.LENGTH_LONG).show();

                //notification function
                notification(orderID, userName_Address[1],thisDate);
                /*updateDrugQuantity function:
                this function is responsible for updating the drug that has
                been confirmed for purchase.
              */
                updateDrugQuantity(viewholder.DrugID.getText().toString().trim(),  Integer.parseInt(viewholder.DrugQuantity.getText().toString().trim()));
                /*deleteCart function:
                this function responsible for complete removing order from cart...
                */
                deleteCart(userKey, viewholder.CartOrderID.getText().toString().trim());
                Toast.makeText(getContext(), "Cart has been updated", Toast.LENGTH_LONG).show();





            }
        });


     return view;
    }
    //this function will use to delete cart order
    public void deleteCart(String usrkeys, String ID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Dbref = database.getReference("CartOrder").child(usrkeys);

        Query cartInformation = Dbref.child("cartOrdID").equalTo(ID);
        cartInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot Ds;
                String Name = snapshot.child("drugName").getValue(String.class);
                Log.e("Drug Information", "The Medicine, "+ Name + "has been deleted");
                Dbref.child(ID).setValue(null);
                 }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void updateDrugQuantity(String DrugID, int DrugQuantity){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Dbref  = database.getReference();

        Query dbQuery = Dbref.child("Drug").orderByKey().equalTo(DrugID);

        dbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot Ds : snapshot.getChildren()){
                   int  currentTotal = Ds.child("drugQuantity").getValue(Integer.class);
                   totalQuantity = currentTotal - DrugQuantity;

                   Map<String, Object> drugUpdate = new HashMap<>();
                   drugUpdate.put("drugQuantity", totalQuantity);
                   Dbref.child("Drug").child(DrugID).updateChildren(drugUpdate);
                   Log.e("update Quantity","updated total: "+ totalQuantity);



               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public String[] getAddress(String mkeys){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Dbref = database.getReference();
        Query AddQuery = Dbref.child("USER").orderByKey().equalTo(mkeys);
        String[] text = new String[2];

        AddQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Ds : snapshot.getChildren()){
                    userAddress = Ds.child("residential_Address").getValue(String.class);
                    UserName   = Ds.child("fullName").getValue(String.class);
                    text[0] = userAddress;
                    text[1] = UserName;
                    Log.e("User Name","My Name: "+ UserName);
                    Log.e("Address", "Residential Address: " + userAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return text;

    }
    //Notification..
    public void notification(String orderID,String Uname, String AddedDate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference NotRef = database.getReference("Notification");
        NotificationClass notificationdb = new NotificationClass();

        String NotID = NotRef.push().getKey();
        notificationdb.setNotID(NotID);
        notificationdb.setOrderID(orderID);
        notificationdb.setUserNAme(Uname);
        notificationdb.setStatus(0);
        notificationdb.setmMessage("added a new order. please check the order using the using the order ID");
        notificationdb.setDateAdded(AddedDate);
        NotRef.child(NotID).setValue(notificationdb);
        Toast.makeText(getContext(), "added to Notification", Toast.LENGTH_LONG).show();


    }
}
