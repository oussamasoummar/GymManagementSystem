package com.example.GYMmanagementsystem.Payment;
import com.example.GYMmanagementsystem.Client;
import com.example.GYMmanagementsystem.Database;
import java.sql.Date;
import java.util.Calendar;
import java.sql.SQLException;
import java.sql.ResultSet;

public class PaymentService {
    private Database databaseHandler;

    public PaymentService(Database databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public void processPayment(Client client, int months, int amount) {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            String query = "INSERT INTO payment (ClientID, PaymentDate, Mois, Amount) VALUES (?, ?, ?, ?)";
            databaseHandler.executeUpdate(query, String.valueOf(client.getClientId()), currentDate.toString(), String.valueOf(months), String.valueOf(amount));

            updateExpirationDate(client.getClientId(), months);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean validatePayment(String clientId, String firstName, String lastName) {
        String query = "SELECT COUNT(1) FROM client WHERE ClientID=? AND FirstName=? AND LastName=?";
        try {
            ResultSet result = databaseHandler.executeQuery(query, clientId, firstName, lastName);
            if (result.next() && result.getInt(1) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateExpirationDate(int clientId, int monthsToAdd) throws SQLException {
        String query = "SELECT ExpirationDate FROM client WHERE ClientID = ?";
        ResultSet result = databaseHandler.executeQuery(query, String.valueOf(clientId));

        if (result.next()) {
            Date expirationDate = result.getDate("ExpirationDate");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expirationDate);
            calendar.add(Calendar.MONTH, monthsToAdd);
            Date newExpirationDate = new Date(calendar.getTimeInMillis());

            String updateQuery = "UPDATE client SET ExpirationDate = ? WHERE ClientID = ?";
            databaseHandler.executeUpdate(updateQuery, newExpirationDate.toString(), String.valueOf(clientId));
        }
    }
}
