package com.example.pharmacyprojects;

public class NotificationClass {

    String NotID;
    String UserID;
    String UserNAme;
    String mMessage;
    String OrderID;
    int Status;
    String DateAdded;

    public String getUserNAme() {
        return UserNAme;
    }

    public void setUserNAme(String userNAme) {
        UserNAme = userNAme;
    }

    public String getNotID() {
        return NotID;
    }

    public void setNotID(String notID) {
        NotID = notID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String dateAdded) {
        DateAdded = dateAdded;
    }

    public NotificationClass(){

    }

    public NotificationClass(String NotID, String Uname, String orderID,  String Message, int Status, String DateAdded){
              this.NotID = NotID;
              this.UserID = UserID;
              this.UserNAme = Uname;
              this.OrderID = orderID;
              this.mMessage = Message;
              this.Status = Status;
              this.DateAdded = DateAdded;

    }

}