package com.example.GYMmanagementsystem.Dashboard;

import com.example.GYMmanagementsystem.Utilities.Chart;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dashboard implements DashUtils {
    private List<String> creteriasList;
    private List<Integer> yearsList;
    private final int INTEREST_YEARS = 5;

    public void populateCreteriasList(ComboBox<String> criteriasListHolder) {
        populateComboBox(creteriasList, criteriasListHolder);
    }

    public void populateYearsList(ComboBox<Integer> yearsListHolder) {
        populateComboBox(yearsList, yearsListHolder);
    }

    public void getTodaysGain(Connection connect, Label label) {
        fetchDataAndUpdateLabel(connect, "SELECT SUM(Amount) AS todaysGain FROM payment WHERE PaymentDate = CURDATE()", "todaysGain", label);
    }

    public void getTodaysQuitClient(Connection connect, Label label) {
        fetchDataAndUpdateLabel(connect, "SELECT COUNT(*) AS todaysClientsQuit FROM QuitHistoric WHERE id = CURDATE()", "todaysClientsQuit", label);
    }

    public void getTodaysNewClients(Connection connect, Label label) {
        fetchDataAndUpdateLabel(connect, "SELECT COUNT(*) AS todaysNewClients FROM client WHERE StartDate = CURDATE()", "todaysNewClients", label);
    }

    public void updateChart(Connection connect, String title, int year, AreaChart<String, Number> dash_chart) {
        String tableName = "";
        String logic = "";
        String dateField = "";

        switch (title) {
            case "Income":
                tableName = "payment";
                dateField = "paymentDate";
                logic = "SUM(Amount) AS result";
                break;
            case "New Clients":
                tableName = "client";
                dateField = "StartDate";
                logic = "COUNT(*) AS result";
                break;
            case "Quit Clients":
                tableName = "QuitHistoric";
                dateField = "id";
                logic = "SUM(counter) AS result";
                break;
            default:
                throw new IllegalArgumentException("Invalid title: " + title);
        }

        String sql = String.format(
                "SELECT YEAR(%s) AS year, MONTH(%s) AS month, %s " +
                        "FROM %s WHERE %s BETWEEN ? AND ? " +
                        "GROUP BY YEAR(%s), MONTH(%s) " +
                        "ORDER BY year DESC, month ASC",
                dateField, dateField, logic, tableName, dateField, dateField, dateField, dateField
        );

        try (PreparedStatement prepare = connect.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = LocalDate.of(year, 12, 31);
            prepare.setString(1, startDate.format(formatter)); // Start date of the selected year
            prepare.setString(2, endDate.format(formatter)); // End date of the selected year

            try (ResultSet result = prepare.executeQuery()) {
                updateChart(result, title, dash_chart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dashboard() {
        final int CURRENT_YEAR = java.time.Year.now().getValue();
        creteriasList = List.of("Income", "New Clients", "Quit Clients");
        yearsList = new ArrayList<>();
        for (int i = 0; i < INTEREST_YEARS; i++) {
            yearsList.add(CURRENT_YEAR - i);
        }
    }
}
