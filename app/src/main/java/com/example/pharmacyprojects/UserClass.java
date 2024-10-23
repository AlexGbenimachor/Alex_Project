package com.example.pharmacyprojects;

public class UserClass {
    String userID;
    String fullName;
    String DateOfBirth;
    String Gender;
    String Country;
    String State;
    String Residential_Address;
    String Phone;
    String EmailAddress;
    String Password;
    String userType;
    String filepath;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getResidential_Address() {
        return Residential_Address;
    }

    public void setResidential_Address(String residential_Address) {
        Residential_Address = residential_Address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public  UserClass() {//create an Empty Constructor
    }


    public UserClass(String userid, String userName, String userGender, String userDob, String userPhoneNumber, String userCountry, String userState, String userRes_Address, String userEmailAddress,  String userPassword, String userRoles, String userImage ) {
        this.userID = userid;//1
        this.fullName = userName;//2
        this.Gender = userGender;//3
        this.DateOfBirth = userDob;//4
        this.Country = userCountry;//5
        this.State=userState;//6
        this.Residential_Address = userRes_Address;//7
        this.Phone = userPhoneNumber;//8
        this.EmailAddress = userEmailAddress;//9
        this.Password = userPassword;//10
        this.userType = userRoles;//11
        this.filepath = userImage;//12



    }

}
