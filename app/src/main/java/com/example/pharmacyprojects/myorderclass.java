package com.example.pharmacyprojects;

public class myorderclass {

    String mOrderID;
    String DrugID;
    String UserID;
    String userName;
    String imageUrl;
    String drugName;
    int  Status;
    int drugPrice;
    int drugQuantity;
    int total;
    String DeliverAddress;
    String OrderDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDeliverAddress() {
        return DeliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        DeliverAddress = deliverAddress;
    }

    public String getmOrderID() {
        return mOrderID;
    }

    public void setmOrderID(String mOrderID) {
        this.mOrderID = mOrderID;
    }

    public String getDrugID() {
        return DrugID;
    }

    public void setDrugID(String drugID) {
        DrugID = drugID;
    }

    public int getDrugPrice() {
        return drugPrice;
    }

    public void setDrugPrice(int drugPrice) {
        this.drugPrice = drugPrice;
    }

    public int getDrugQuantity() {
        return drugQuantity;
    }

    public void setDrugQuantity(int drugQuantity) {
        this.drugQuantity = drugQuantity;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public myorderclass(){

    }

    public myorderclass(String orderID, String drugID, String UserID, String UserName,  String drugImage, String drugName, int drugPrice, int drugQuantity,int Status,int totals,  String Address, String orderDate){
           this.mOrderID = orderID;
           this.DrugID = drugID;
           this.UserID = UserID;
           this.userName = UserName;
           this.imageUrl = drugImage;
           this.drugName = drugName;
           this.Status = Status;
           this.drugPrice = drugPrice;
           this.drugQuantity = drugQuantity;
           this.total = totals;
           this.OrderDate = orderDate;
           this.DeliverAddress = Address;


    }


}
