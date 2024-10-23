package com.example.pharmacyprojects;

public class drugModelClass {
    //initialize drug Table variable
    String drugID;
    String drugName;
    String drugDescription;
    String drugFilePath;
    int drugPrice;
    int drugQuantity;
    String drugSupplier;
    String drugTypes;
    String drugMedicineTypes;
    String drugDate;
    String drugExpiryDate;
    String drugBrand;


     public String getDrugID(){return drugID;}
     public void setDrugID(String DrugID){this.drugID = DrugID;}

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDescription() {
        return drugDescription;
    }

    public void setDrugDescription(String drugDescription) {
        this.drugDescription = drugDescription;
    }

    public String getDrugFilePath() {
        return drugFilePath;
    }

    public void setDrugFilePath(String drugFilePath) {
        this.drugFilePath = drugFilePath;
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

    public String getDrugSupplier() {
        return drugSupplier;
    }

    public void setDrugSupplier(String drugSupplier) {
        this.drugSupplier = drugSupplier;
    }

    public String getDrugTypes() {
        return drugTypes;
    }

    public void setDrugTypes(String drugTypes) {
        this.drugTypes = drugTypes;
    }

    public String getDrugMedicineTypes() {
        return drugMedicineTypes;
    }

    public void setDrugMedicineTypes(String drugMedicineTypes) {
        this.drugMedicineTypes = drugMedicineTypes;
    }

    public String getDrugDate() {
        return drugDate;
    }

    public void setDrugDate(String drugDate) {
        this.drugDate = drugDate;
    }

    public String getDrugExpiryDate() {
        return drugExpiryDate;
    }

    public void setDrugExpiryDate(String drugExpiryDate) {
        this.drugExpiryDate = drugExpiryDate;
    }

    public String getDrugBrand() {
        return drugBrand;
    }

    public void setDrugBrand(String drugBrand) {
        this.drugBrand = drugBrand;
    }


    public drugModelClass(){
        //create an empty constructor
    }

    public drugModelClass(String drgID, String drgName, String drgDescription, String ImgUrl, int drgPrice, int drgQuantity, String drgSupplier, String drgTypes, String drgMedicineTypes, String drgDate, String drgExpiryDate, String drgBrand){
        this.drugID = drgID;
        this.drugName = drgName;
        this.drugDescription = drgDescription;
        this.drugFilePath = ImgUrl;
        this.drugPrice = drgPrice;
        this.drugQuantity = drgQuantity;
        this.drugSupplier = drgSupplier;
        this.drugTypes = drgTypes;
        this.drugMedicineTypes = drgMedicineTypes;
        this.drugDate = drgDate;
        this.drugExpiryDate = drgExpiryDate;
        this.drugBrand = drgBrand;


    }

}
