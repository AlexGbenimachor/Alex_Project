package com.example.pharmacyprojects;

public class drugDbClass {
    //define class variables

    String drugID;
    String drugName;
    String drugImgUrl;
    int drugPrice;
    int drugQuantity;
    String drugDescription;
    //empty constructor...
    public drugDbClass() {
    }

    //getter and setters
    public String getDrugID() {
        return drugID;
    }

    public void setDrugID(String drugID) {
        this.drugID = drugID;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugImgUrl() {
        return drugImgUrl;
    }

    public void setDrugImgUrl(String drugImgUrl) {
        this.drugImgUrl = drugImgUrl;
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

    public String getDrugDescription() {
        return drugDescription;
    }

    public void setDrugDescription(String drugDescription) {
        this.drugDescription = drugDescription;
    }
    //pass argument to  class constructor...
    public drugDbClass(String DrugID, String DrugName, String imageUrl , int DrugPrice, int DrugQuantity, String DrugDescription){
        this.drugID = DrugID;
        this.drugName = DrugName;
        this.drugImgUrl = imageUrl;
        this.drugPrice = DrugPrice;
        this.drugQuantity = DrugQuantity;
        this.drugDescription = DrugDescription;

    }
}
