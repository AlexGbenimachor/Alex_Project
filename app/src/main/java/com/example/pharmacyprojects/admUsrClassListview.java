package com.example.pharmacyprojects;

public class admUsrClassListview {
    String usrIDkey;
    String filePath;
    String fullName;

    public  admUsrClassListview(){

    }
    public String getUsrIDkey() {
        return usrIDkey;
    }

    public void setUsrIDkey(String usrIDkey) {
        this.usrIDkey = usrIDkey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public admUsrClassListview(String usrID, String ImageUrl, String fullName){
        this.usrIDkey = usrID;
        this.filePath = ImageUrl;
        this.fullName=fullName;

    }

}
