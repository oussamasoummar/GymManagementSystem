package com.example.GYMmanagementsystem;

import com.example.GYMmanagementsystem.Payment.ExpirationDateComparator;
import com.example.GYMmanagementsystem.Payment.PaymentService;
import com.example.GYMmanagementsystem.Dashboard.Dashboard;
import com.example.GYMmanagementsystem.Dashboard.DashUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable, DashUtils {
    //Commun attributes
    @FXML
    private AnchorPane main_form;
    @FXML
    private Button Logout;
    @FXML
    private Button Home_button;
    @FXML
    private Button follow_subscriptions_button;
    @FXML
    private Button Paiement_button;
    private final int  PRICE = 100;
    private Database databaseHandler = new Database();
    //Dashboard
    Dashboard dashboard = new Dashboard(databaseHandler);
    @FXML
    ComboBox<Integer> yearsListHolder;
    @FXML
    ComboBox<String> criteriasListHolder;
    @FXML
    private AnchorPane home_form;
    @FXML
    private Label dash_tg;
    @FXML
    private Label dash_tnc;
    @FXML
    private Label dash_tcq;
    @FXML
    private AreaChart dash_chart;
    public void updateChart(){
        dashboard.updateChart(databaseHandler,criteriasListHolder.getSelectionModel().getSelectedItem(),yearsListHolder.getSelectionModel().getSelectedItem(),dash_chart);
    }
    //End dashboard


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
    private TextField FS_FirstName_tf;
    @FXML
    private TextField FS_LastName_tf;
    @FXML
    private TextField FS_Phone_tf;
    @FXML
    private TextField FS_Gmail_tf;
    @FXML
    private TextField FS_Cin_tf;
    @FXML
    private ComboBox<String> FS_Gender_combobox;
    @FXML
    private Button FS_Add_Button;
    @FXML
    private Button FS_Update_Button;
    @FXML
    private Button FS_Delete_Button;
    //and here for planning




    //paiement karim and maroune
    @FXML
    private AnchorPane paiement_form;
    @FXML
    private TextField Paiement_ClientId_tf ;
    @FXML
    private TextField Paiement_firstName;
    @FXML
    private TextField Paiement_lastName;
    @FXML
    private Button Paiement_Payer_Button;
    @FXML
    private Button Paiement_Clear_Button;
    @FXML
    private TextField Paiement_Mois_tf;
    @FXML
    private Label Amount_Label;
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


    public void FollowSubsReset() {
        FS_ClientId_tf.setText("");
        FS_FirstName_tf.setText("");
        FS_LastName_tf.setText("");
        FS_Gender_combobox.getSelectionModel().clearSelection();
        FS_Phone_tf.setText("");
        FS_Gmail_tf.setText("");
        FS_Cin_tf.setText("");
    }
    public void FollowSubsGenderList(){
        ObservableList<String> options = FXCollections.observableArrayList("male", "female");
        FS_Gender_combobox.setItems(options);
    }
    //this function for extracting the client data from the database and store them into list
    public ObservableList<Client> FollowSubsListData() {

        ObservableList<Client> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            Client client;

            while (result.next()) {
                client = new Client(result.getInt("ClientId"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getString("phone"),
                        result.getString("Gmail"),
                        result.getString("Cin"),
                        result.getDate("startDate"),result.getDate("ExpirationDate"));
                listData.add(client);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    //this function for showing the list that contain client data in tableview
    private ObservableList<Client> clientListData;
    public void FollowSubsShowListData() {
        clientListData= FollowSubsListData();
        FS_TableCol_ClientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        FS_TableCol_FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        FS_TableCol_LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        FS_TableCol_Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        FS_TableCol_Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        FS_TableCol_Gmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        FS_TableCol_Cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        FS_TableCol_StartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        FS_Table_View.setItems(clientListData);

    }
    public void FollowSubsSelect() {
        Client clientSelected = FS_Table_View.getSelectionModel().getSelectedItem();

        if (clientSelected == null) {
            return;
        }

        FS_ClientId_tf.setText(String.valueOf(clientSelected.getClientId()));
        FS_FirstName_tf.setText(clientSelected.getFirstName());
        FS_LastName_tf.setText(clientSelected.getLastName());
        FS_Cin_tf.setText(clientSelected.getCin());
        FS_Gmail_tf.setText(clientSelected.getGmail());
        FS_Phone_tf.setText(clientSelected.getPhone());
    }
    //this function for adding new client into database
    public void FollowSubsAdd(){


        connect = Database.connectDB();
        try {
            Alert alert;
            if (FS_ClientId_tf.getText().isEmpty()
                    || FS_FirstName_tf.getText().isEmpty()
                    || FS_LastName_tf.getText().isEmpty()
                    || FS_Gender_combobox.getSelectionModel().getSelectedItem() == null
                    || FS_Cin_tf.getText().isEmpty()
                    || FS_Gmail_tf.getText().isEmpty()
                    || FS_Phone_tf.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                String check = "SELECT ClientId FROM client WHERE clientId = '"
                        + FS_ClientId_tf.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("ClienID : " + FS_ClientId_tf.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    Date date = new Date();
                    java.sql.Date sqldate = new java.sql.Date(date.getTime());
                    String sql = "INSERT INTO client (ClientID, FirstName, LastName, Phone, Gmail, Cin, Gender,StartDate,ExpirationDate)"
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, FS_ClientId_tf.getText());
                    prepare.setString(2, FS_FirstName_tf.getText());
                    prepare.setString(3, FS_LastName_tf.getText());
                    prepare.setString(4, FS_Phone_tf.getText());
                    prepare.setString(5, FS_Gmail_tf.getText());
                    prepare.setString(6, FS_Cin_tf.getText());
                    prepare.setString(7, (String) FS_Gender_combobox.getSelectionModel().getSelectedItem());
                    prepare.setString(8,String.valueOf(sqldate));
                    prepare.setString(9,String.valueOf(sqldate));

                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    FollowSubsShowListData();
                    PaymentShowListData();
                    FollowSubsReset();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //this function for update client information
    public void FollowSubsUpdate(){
        connect = Database.connectDB();
        try {
            Alert alert;
            if (FS_ClientId_tf.getText().isEmpty()
                    || FS_FirstName_tf.getText().isEmpty()
                    || FS_LastName_tf.getText().isEmpty()
                    || FS_Gender_combobox.getSelectionModel().getSelectedItem() == null
                    || FS_Cin_tf.getText().isEmpty()
                    || FS_Gmail_tf.getText().isEmpty()
                    || FS_Phone_tf.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Client ID: " + FS_ClientId_tf.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK) ){
                    String sqlUpdate = " UPDATE client SET FirstName = ?, LastName = ?, Phone = ?, Gmail = ?, Cin = ?, Gender = ?"
                            +"WHERE ClientId ='"+FS_ClientId_tf.getText()+"'";
                    prepare = connect.prepareStatement(sqlUpdate);
                    prepare.setString(1, FS_FirstName_tf.getText());
                    prepare.setString(2, FS_LastName_tf.getText());
                    prepare.setString(3, FS_Phone_tf.getText());
                    prepare.setString(4, FS_Gmail_tf.getText());
                    prepare.setString(5, FS_Cin_tf.getText());
                    prepare.setString(6, (String) FS_Gender_combobox.getSelectionModel().getSelectedItem());


                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                    FollowSubsShowListData();
                    FollowSubsReset();
                    PaymentShowListData();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //this function for deleting client from database
    public void FollowSubsDelete(){
        connect = Database.connectDB();
        try {
            Alert alert;
            if (FS_ClientId_tf.getText().isEmpty()
                    || FS_FirstName_tf.getText().isEmpty()
                    || FS_LastName_tf.getText().isEmpty()
                    || FS_Gender_combobox.getSelectionModel().getSelectedItem() == null
                    || FS_Cin_tf.getText().isEmpty()
                    || FS_Gmail_tf.getText().isEmpty()
                    || FS_Phone_tf.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Client ID: " + FS_ClientId_tf.getText() + " ?");
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK) ){
                    String insertQuitHistoric = "INSERT INTO QuitHistoric (id, counter) VALUES (CURDATE(), 1) " +
                            "ON DUPLICATE KEY UPDATE counter = counter + 1";
                    try {
                        prepare = connect.prepareStatement(insertQuitHistoric);
                        prepare.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the error
                    }

                    // Delete the client
                    String deleteClient = "DELETE FROM client WHERE ClientId = ?";
                    try {
                        prepare = connect.prepareStatement(deleteClient);
                        prepare.setString(1, FS_ClientId_tf.getText());
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Deleted!");
                        alert.showAndWait();

                        FollowSubsShowListData();
                        FollowSubsReset();
                        PaymentShowListData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the error
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //this function for searching by name,id,cin,gender
    public void FollowSubsSearch() {

        //the filter are initially all the element matches because of lamdba expression return true
        FilteredList<Client> filter = new FilteredList<>(clientListData, e -> true);

        FS_search_tf.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateClient -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateClient.getClientId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getPhone().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getCin().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateClient.getStartDate() != null && predicateClient.getStartDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Client> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(FS_Table_View.comparatorProperty());
        FS_Table_View.setItems(sortList);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == Home_button ) {
            home_form.setVisible(true);
            follow_subsriptions_form.setVisible(false);
            paiement_form.setVisible(false);

            Home_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
            follow_subscriptions_button.setStyle("-fx-background-color:transparent");
            Paiement_button.setStyle("-fx-background-color:transparent");

            //1- Start Overview
            //Today's gain
            dashboard.getTodaysGain(dash_tg);
            //Today's new clients
            dashboard.getTodaysNewClients(dash_tnc);
            //Today's quit clients
            dashboard.getTodaysQuitClient(dash_tcq);
            //End Overview

            //2- Start Dashboard chart
            updateChart();
        } else if (event.getSource() == follow_subscriptions_button) {
            home_form.setVisible(false);
            follow_subsriptions_form.setVisible(true);
            paiement_form.setVisible(false);

            follow_subscriptions_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
            Home_button.setStyle("-fx-background-color:transparent");
            Paiement_button.setStyle("-fx-background-color:transparent");

            FollowSubsShowListData();
            FollowSubsGenderList();


        } else if (event.getSource() == Paiement_button) {
            home_form.setVisible(false);
            follow_subsriptions_form.setVisible(false);
            paiement_form.setVisible(true);
            PaymentShowListData();
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

    //com.example.GYMmanagementsystem.Payment
    private PaymentService paymentService = new PaymentService(databaseHandler);
    //this function for showing the list that contain client data in tableview
    private ObservableList<Client> PaymentData = FXCollections.observableArrayList();
    public void PaymentShowListData() {
        PaymentData= FollowSubsListData();
        FXCollections.sort(PaymentData,new ExpirationDateComparator());
        Paiement_col_ClientID.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        Paiement_col_FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Paiement_col_LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Paiement_col_ExpirationDate.setCellValueFactory(new PropertyValueFactory<>("ExpirationDate"));
        Paiement_tableView.setItems(PaymentData);
    }

    //this function for showing the list that contain client data in tableview
    public void PaymentButtonOnAction() {
        if (isFormValid()) {
            String clientId = Paiement_ClientId_tf.getText();
            String firstName = Paiement_firstName.getText();
            String lastName = Paiement_lastName.getText();
            int months = Integer.parseInt(Paiement_Mois_tf.getText());
            int amount =Integer.parseInt(Amount_Label.getText()) ;

            if (paymentService.validatePayment(clientId, firstName, lastName)) {
                Client client = new Client();
                client.setClientId(Integer.parseInt(clientId));
                client.setFirstName(firstName);
                client.setLastName(lastName);
                paymentService.processPayment(client, months, amount);

                showAlert(Alert.AlertType.INFORMATION, "Information Message", "Payment Successfully Added!");
                PaymentShowListData();
                Payment_CancelButton();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
        }
    }

    public void PaymentSelect() {
        Client clientSelected = Paiement_tableView.getSelectionModel().getSelectedItem();
        if (clientSelected != null) {
            Paiement_ClientId_tf.setText(String.valueOf(clientSelected.getClientId()));
            Paiement_firstName.setText(clientSelected.getFirstName());
            Paiement_lastName.setText(clientSelected.getLastName());
        }
    }

    public void Payment_CancelButton() {
        Paiement_ClientId_tf.setText("");
        Paiement_firstName.setText("");
        Paiement_lastName.setText("");
        Paiement_Mois_tf.setText("");
        Amount_Label.setText("");
    }

    private boolean isFormValid() {
        return !Paiement_ClientId_tf.getText().isBlank()
                && !Paiement_firstName.getText().isBlank()
                && !Paiement_lastName.getText().isBlank()
                && !Paiement_Mois_tf.getText().isBlank()
                && !Amount_Label.getText().isBlank();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void paymentAmount(){
        int Mois;
        int Amount;
        Mois=Integer.parseInt(Paiement_Mois_tf.getText());
        if(Mois<=0){
            Paiement_Mois_tf.setText("1");
            Amount_Label.setText(String.valueOf(PRICE));
            Mois=1;
            Amount=PRICE;
        }else {
            Amount=Mois*PRICE;
            Amount_Label.setText(String.valueOf(Amount));
        }
    }

    //fin
    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //I- Dashboard
        //1- Start Overview

        dashboard.populateCreteriasList(criteriasListHolder);
        dashboard.populateYearsList(yearsListHolder);
        //Today's gain
        dashboard.getTodaysGain(dash_tg);
        //Today's new clients
        dashboard.getTodaysNewClients(dash_tnc);
        //Today's quit clients
        dashboard.getTodaysQuitClient(dash_tcq);
        //2- Dashboard chart
        dash_chart.setTitle("");
        FollowSubsSelect();
        FollowSubsShowListData();
        FollowSubsGenderList();
        PaymentSelect();
        PaymentShowListData();
    }
}
