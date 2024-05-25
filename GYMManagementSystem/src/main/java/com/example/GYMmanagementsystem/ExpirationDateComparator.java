package com.example.GYMmanagementsystem;
import java.util.Comparator;
import java.util.Date;

public class ExpirationDateComparator implements Comparator<Client> {
    public int compare(Client c1, Client c2) {
        if (c1.getExpirationDate().getYear() != c2.getExpirationDate().getYear()) {
            return (c1.getExpirationDate().getYear() - c2.getExpirationDate().getYear());
        } else if (c1.getExpirationDate().getMonth() != c2.getExpirationDate().getMonth()) {
            return (c1.getExpirationDate().getMonth() - c2.getExpirationDate().getMonth());
        } else if (c1.getExpirationDate().getDay() != c2.getExpirationDate().getDay()) {
            return (c1.getExpirationDate().getDay() - c2.getExpirationDate().getDay());
        } else
           return c1.getLastName().length()-c2.getLastName().length();
    }
}
