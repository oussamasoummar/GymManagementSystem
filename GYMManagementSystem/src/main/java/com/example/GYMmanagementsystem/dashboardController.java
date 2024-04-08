package com.example.GYMmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private AnchorPane main_form;
    @FXML
    private Button Logout;
    @FXML
    private Button Home_button;
    @FXML
    private Button follow_subscriptions_button;
    @FXML
    private  Button Planning_button;
    @FXML
    private Button Paiement_button;
    //start home form (this part for youssef) you can whatever you want
    @FXML
    private AnchorPane home_form;
    @FXML
    private Label total_gain_label;
    @FXML
    private Label total_client_label;
    @FXML
    private Label inactive_client_label;
    //end home form

    //start follow subsriptions
    @FXML
    private AnchorPane follow_subsriptions_form;
    @FXML
    private TextField FS_search_tf;
    //table elements
    @FXML
    private TableView<Client> FS_Table_View;
    @FXML
    private TableColumn<Client,String> FS_TableCol_ClientId;
    @FXML
    private TableColumn<Client,String> FS_TableCol_FirstName;
    @FXML
    private TableColumn<Client,String> FS_TableCol_LastName;
    @FXML
    private TableColumn<Client,String> FS_TableCol_Gender;
    @FXML
    private TableColumn<Client,String> FS_TableCol_Phone;
    @FXML
    private TableColumn<Client,String> FS_TableCol_Gmail;
    @FXML
    private TableColumn<Client,String> FS_TableCol_Cin;
    @FXML
    private TableColumn<Client,String> FS_TableCol_StartDate;
    //client information
    @FXML
    private TextField FS_ClientId_tf;
    @FXML
    private TextField FS_FirststName_tf;
    @FXML
    private TextField FS_LastName_tf;
    @FXML
    private TextField FS_Phone_tf;
    @FXML
    private TextField FS_Gmail_tf;
    @FXML
    private TextField FS_Cin_tf;
    @FXML
    private ComboBox<?> FS_Gender_combobox;
    @FXML
    private Button FS_Add_Button;
    @FXML
    private Button FS_Update_Button;
    @FXML
    private Button FS_Delete_Button;
    //and here for planning




    //and here for paiement karim and maroune you can modify whatever you want
    @FXML
    private AnchorPane paiement_form;
    @FXML
    private TextField Paiement_ClientId_tf ;
    @FXML
    private Label Paiement_firstName_label;
    @FXML
    private Label Paiement_lastName_label;
    @FXML
    private Button Paiement_Payer_Button;
    @FXML
    private Button Paiement_Clear_Button;
    @FXML
    private TextField Paiement_Mois_tf;
    @FXML
    private TableView<Client> Paiement_tableView;
    @FXML
    private TableColumn<Client,String> Paiement_col_ClientID;
    @FXML
    private TableColumn<Client,String> Paiement_col_FirstName;
    @FXML
    private TableColumn<Client,String> Paiement_col_LastName;
    @FXML
    private TableColumn<Client,String> Paiement_col_ExpirationDate;

    //data base attributs
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;


    public ObservableList<Client> FollowSubsListData() {

        ObservableList<Client> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            Client client;

            while (result.next()) {
                client = new Client(result.getString("ClientId"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("phone"),
                        result.getString("Gmail"),
                        result.getString("Cin"),
                        result.getString("gender"),
                        result.getDate("startDate"));
                listData.add(client);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    public void switchForm(ActionEvent event) {

        if (event.getSource() == Home_button ) {
            home_form.setVisible(true);
            follow_subsriptions_form.setVisible(false);
            paiement_form.setVisible(false);

            Home_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
            follow_subscriptions_button.setStyle("-fx-background-color:transparent");
            Paiement_button.setStyle("-fx-background-color:transparent");


        } else if (event.getSource() == follow_subscriptions_button) {
            home_form.setVisible(false);
            follow_subsriptions_form.setVisible(true);
            paiement_form.setVisible(false);

            follow_subscriptions_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
            Home_button.setStyle("-fx-background-color:transparent");
            Paiement_button.setStyle("-fx-background-color:transparent");


        } else if (event.getSource() == Paiement_button) {
            home_form.setVisible(false);
            follow_subsriptions_form.setVisible(false);
            paiement_form.setVisible(true);

            Paiement_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
            follow_subscriptions_button.setStyle("-fx-background-color:transparent");
            Home_button.setStyle("-fx-background-color:transparent");
        }

    }

    private double x = 0;
    private double y = 0;
    public void logout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                Logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("LogInPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
