package com.example.GYMmanagementsystem;


import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static double x = 0;
    private static double y = 0;

    public static  Connection connectDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginjava","root","Ouss@m@soummar123");
            return connection;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}
