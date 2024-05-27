package Payment;
import com.example.GYMmanagementsystem.Database;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.sql.ResultSet;


public class PaymentValidator {
    private Database databaseHandler;

    public PaymentValidator(Database databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public boolean validatePayment(String clientId, String firstName, String lastName) {
        String query = "SELECT COUNT(1) FROM client WHERE ClientID=? AND FirstName=? AND LastName=?";
        try {
            ResultSet result = databaseHandler.executeQuery(query, clientId, firstName, lastName);
            if (result.next() && result.getInt(1) == 1) {
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid payment, Client doesn't exist!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



