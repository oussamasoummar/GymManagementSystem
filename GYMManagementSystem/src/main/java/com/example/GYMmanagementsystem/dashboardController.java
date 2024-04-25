package com.example.GYMmanagementsystem;

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
import javafx.scene.chart.XYChart;
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
import java.time.Year;
import java.util.*;

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
    private final int  PRICE = 100;
    @FXML
    private Label dash_tg;
    //Today's new clients placeholder
    @FXML
    private Label dash_tnc;
    //Today's quit clients placeholder
    @FXML
    private Label dash_tcq;
    //Dashboard chart
    @FXML
    private AreaChart dash_chart;
    @FXML
    private ComboBox years_list;
    @FXML
    private ComboBox criterias_list;
    //Get criterias list
    public void criteriasList(){
        String criterias []= {"Income","New Clients","Quit Clients"};
        List<String> criteriasList = new ArrayList<>();
        for(String data: criterias){
            criteriasList.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(criteriasList);
        criterias_list.setItems(listData);
    }
    //Get years list
    public void yearsList() {
        List<Integer> yearsList = new ArrayList<>();
        int currentYear = Year.now().getValue(); // Get the current year
        for (int i = 0; i < 5; i++) {
            yearsList.add(currentYear - i); // Add the current year and the 4 previous years
        }

        ObservableList listData = FXCollections.observableArrayList(yearsList);
        years_list.setItems(listData);
    }

    //Start Overview
    // Today's Gain
    public void dashboardTG() {
        String sql = "SELECT COUNT(*) AS todayPayNum FROM payment WHERE PaymentDate = CURDATE();";
        connect = Database.connectDB();
        int todaysGain = 0; // Change data type to int
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                todaysGain = result.getInt("todayPayNum") * PRICE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dash_tg.setText(String.valueOf(todaysGain));
    }

    // Today's New Clients
    public void dashboardTNC() {
        String sql = "SELECT COUNT(*) AS todaysNewClients FROM client WHERE StartDate = CURDATE();";
        connect = Database.connectDB();
        int todaysNewClients = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                todaysNewClients = result.getInt("todaysNewClients");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dash_tnc.setText(String.valueOf(todaysNewClients));
    }

    // Today's Clients That Quit
    public void dashboardTCQ() {
        String sql = "SELECT COUNT(*) AS todaysClientsQuit FROM client WHERE QuitDate = CURDATE();";
        connect = Database.connectDB();
        int todaysClientsQuit = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                todaysClientsQuit = result.getInt("todaysClientsQuit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dash_tcq.setText(String.valueOf(todaysClientsQuit));
    }
    //End overview

    //Start dashboard chart
    public String toCamelCase(String text){
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }
    public void updateChart() {
        dash_chart.getData().clear();
        String selectedYear = years_list.getSelectionModel().getSelectedItem().toString();
        String selectedCriteria = criterias_list.getSelectionModel().getSelectedItem().toString();
        if(selectedCriteria == null || selectedYear == null){
            return;
        }
        dash_chart.setTitle(selectedCriteria +" Chart");
        String sql = "";

        sql = switch (selectedCriteria) {
            case "Income" -> "SELECT YEAR(paymentDate) AS year, MONTH(paymentDate) AS month, COUNT(*) AS numOfPay " +
                    "FROM payment " +
                    "WHERE paymentDate BETWEEN ? AND ? " +
                    "GROUP BY YEAR(paymentDate), MONTH(paymentDate) " +
                    "ORDER BY year DESC, month ASC";
            case "New Clients" -> "SELECT YEAR(StartDate) AS year, MONTH(StartDate) AS month, COUNT(*) AS newClients " +
                    "FROM client " +
                    "WHERE StartDate BETWEEN ? AND ? " +
                    "GROUP BY YEAR(StartDate), MONTH(StartDate) " +
                    "ORDER BY year, month";
            case "Quit Clients" -> "SELECT YEAR(QuitDate) AS year, MONTH(QuitDate) AS month, COUNT(*) AS quitClients " +
                    "FROM client " +
                    "WHERE QuitDate BETWEEN ? AND ? " +
                    "GROUP BY YEAR(QuitDate), MONTH(QuitDate) " +
                    "ORDER BY year, month";
            default -> null;
        };

        connect = Database.connectDB();
        XYChart.Series series = new XYChart.Series<>();
        try {
            prepare = connect.prepareStatement(sql);
            // Replace placeholders with the selected year
            prepare.setString(1, selectedYear + "-01-01"); // Start date of the selected year
            prepare.setString(2, selectedYear + "-12-31"); // End date of the selected year
            result = prepare.executeQuery();

            while (result.next()) {
                String date = result.getString("year") + "-" + result.getString("month");
                int value = 0;
                if(selectedCriteria.equals("Income")) {
                    value = result.getInt("numOfPay") * PRICE;
                } else {
                    value = result.getInt(toCamelCase(selectedCriteria));
                }
                series.getData().add(new XYChart.Data(date, value));
            }

            dash_chart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //End dashboard chart

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
                        result.getDate("startDate"));
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
                    String sql = "INSERT INTO client (ClientID, FirstName, LastName, Phone, Gmail, Cin, Gender,StartDate)"
                            + "VALUES(?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, FS_ClientId_tf.getText());
                    prepare.setString(2, FS_FirstName_tf.getText());
                    prepare.setString(3, FS_LastName_tf.getText());
                    prepare.setString(4, FS_Phone_tf.getText());
                    prepare.setString(5, FS_Gmail_tf.getText());
                    prepare.setString(6, FS_Cin_tf.getText());
                    prepare.setString(7, (String) FS_Gender_combobox.getSelectionModel().getSelectedItem());
                    prepare.setString(8,String.valueOf(sqldate));
                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    FollowSubsShowListData();
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
                alert.setContentText("Are you sure you want to DELETE Client ID: " + FS_ClientId_tf.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK) ){
                    Date date = new Date();
                    java.sql.Date sqldate = new java.sql.Date(date.getTime());
                    String sqlUpdate = " DELETE FROM client WHERE ClientId = '"+FS_ClientId_tf.getText()+"'";
                    prepare = connect.prepareStatement(sqlUpdate);

                    prepare.executeUpdate(sqlUpdate);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    FollowSubsShowListData();
                    FollowSubsReset();
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
            dashboardTG();
            //Today's new clients
            dashboardTNC();
            //Today's quit clients
            dashboardTCQ();
            //End Overview

            //2- Start Dashboard chart
            criteriasList();
            yearsList();
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
        //I- Dashboard
        //1- Start Overview
        //Today's gain
        dashboardTG();
        //Today's new clients
        dashboardTNC();
        //Today's quit clients
        dashboardTCQ();
        //2- Dashboard chart
        criteriasList();
        yearsList();
        dash_chart.setTitle("");
        FollowSubsSelect();
        FollowSubsShowListData();
        FollowSubsGenderList();
    }
}
