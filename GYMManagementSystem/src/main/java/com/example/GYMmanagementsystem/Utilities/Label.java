package com.example.GYMmanagementsystem.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface Label {

    default void fetchDataAndUpdateLabel(Connection connect, java.lang.String sql, java.lang.String fieldName, javafx.scene.control.Label label) {
        int resultValue = 0;
        try {
            PreparedStatement prepare;
            ResultSet result;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                resultValue = result.getInt(fieldName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        label.setText(java.lang.String.valueOf(resultValue));
    }
}
