package com.example.GYMmanagementsystem.Utilities;

import com.example.GYMmanagementsystem.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface Label {
    default void fetchDataAndUpdateLabel(Database database, java.lang.String sql, java.lang.String fieldName, javafx.scene.control.Label label) {
        int resultValue = 0;
        try {
            ResultSet result = database.executeQuery(sql);
            if(result.next())
                resultValue = result.getInt(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        label.setText(java.lang.String.valueOf(resultValue));
    }
}
