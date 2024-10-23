package com.example.pharmacyprojects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationClass> {
    private final Context context;
    public static class viewHolder{
        TextView userNameTxt;
        TextView NotMessageText;
        TextView OrderIDText;


    }

    public NotificationAdapter(Context context, ArrayList<NotificationClass> notList) {
        super(context, R.layout.notificationlayout, notList);
        this.context = context;

    }
    public View getView(int position, View view, ViewGroup parent){
        NotificationClass notify = getItem(position);
        viewHolder viewholder;
        if(view ==null) {
            viewholder = new viewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.notificationlayout, parent, false);
            viewholder.userNameTxt = view.findViewById(R.id.notUserName);
            viewholder.NotMessageText = view.findViewById(R.id.notSystemMessage);
            viewholder.OrderIDText = view.findViewById(R.id.notUserOrderID);
            view.setTag(viewholder);
        }else{
            viewholder = (viewHolder) view.getTag();
        }

        viewholder.OrderIDText.setText(notify.getOrderID());
        viewholder.userNameTxt.setText(notify.getUserNAme());
        viewholder.NotMessageText.setText(notify.getmMessage());
        viewholder.OrderIDText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "order ID:  "+viewholder.OrderIDText.getText().toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    public void moveAdminOrder(String ordrKeys){

        


    }
}
