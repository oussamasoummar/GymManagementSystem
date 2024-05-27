package com.example.GYMmanagementsystem;

import java.sql.Date;

public class Client {
    private Integer clientId;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String gmail;
    private String cin;
    private Date startDate;
    private Date ExpirationDate;

    public Client(Integer clientId, String firstName, String lastName, String gender, String phone,String gmail,String cin, Date startDate,Date ExpirationDate){
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gmail = gmail;
        this.cin = cin;
        this.gender = gender;
        this.startDate = startDate;
        this.ExpirationDate=ExpirationDate;
    }
    public Client(){}

    public Integer getClientId(){
        return clientId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getGender(){
        return gender;
    }
    public String getPhone(){
        return phone;
    }

    public Date getStartDate(){
        return startDate;
    }
    public String getCin() {
        return cin;
    }

    public String getGmail() {
        return gmail;
    }
    public Date getExpirationDate(){
        return ExpirationDate;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setExpirationDate(Date expirationDate) {
        ExpirationDate = expirationDate;
    }
}
