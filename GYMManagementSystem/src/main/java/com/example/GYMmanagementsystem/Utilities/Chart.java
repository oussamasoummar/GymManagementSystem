package com.example.GYMmanagementsystem.Utilities;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;

import java.sql.ResultSet;

public interface Chart {
    default void updateChart(ResultSet result, String title, AreaChart<String, Number> dash_chart) {
        dash_chart.getData().clear();
        dash_chart.setTitle(title);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            while (result.next()) {
                String date = result.getString("year") + "-" + result.getString("month");
                int value = result.getInt("result");
                series.getData().add(new XYChart.Data<>(date, value));
            }

            dash_chart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
