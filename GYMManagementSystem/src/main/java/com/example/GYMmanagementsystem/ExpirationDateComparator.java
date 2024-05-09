package com.example.GYMmanagementsystem;
import java.util.Comparator;
import java.util.Date;

public class ExpirationDateComparator implements Comparator<Client> {
    public int compare(Client c1, Client c2) {
        Date expDate1 = c1.getExpirationDate();
        Date expDate2 = c2.getExpirationDate();
        return expDate1.compareTo(expDate2);
    }
}