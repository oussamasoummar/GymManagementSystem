package com.example.GYMmanagementsystem;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection connect;

    public Database() {
        this.connect = connectDB();
    }
    public  static Connection connectDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/loginjava","youssef","YOUSSEF@123ou");
            return connection;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public ResultSet executeQuery(String query, String... params) throws SQLException {
        PreparedStatement prepare = connect.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            prepare.setString(i + 1, params[i]);
        }
        return prepare.executeQuery();
    }

    public void executeUpdate(String query, String... params) throws SQLException {
        PreparedStatement prepare = connect.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            prepare.setString(i + 1, params[i]);
        }
        prepare.executeUpdate();
    }

}
