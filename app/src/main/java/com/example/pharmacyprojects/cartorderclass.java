package com.example.pharmacyprojects;

public class cartorderclass {
    String cartOrdID;
    String drugID;
    String imageUrl;
    String drugName;
    int ProductQuantity;
    int productPrice;



    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCartOrdID() {
        return cartOrdID;
    }

    public void setCartOrdID(String cartOrdID) {
        this.cartOrdID = cartOrdID;
    }

    public String getDrugID() {
        return drugID;
    }

    public void setDrugID(String drugID) {
        this.drugID = drugID;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }


    public cartorderclass(){

    }

    public cartorderclass(String ordID, String drugID,  String drgName,  String imgurl,  int Price, int Quantity){
        this.cartOrdID = ordID;
        this.drugID = drugID;
        this.drugName = drgName;
        this.imageUrl = imgurl;
        this.productPrice = Price;
        this.ProductQuantity = Quantity;

    }

}
