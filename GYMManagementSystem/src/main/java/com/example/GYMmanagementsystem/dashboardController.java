package com.example.GYMmanagementsystem;
import com.example.GYMmanagementsystem.Payment.ExpirationDateComparator;
import com.example.GYMmanagementsystem.Payment.PaymentService;
import com.example.GYMmanagementsystem.Dashboard.Dashboard;
import com.example.GYMmanagementsystem.Dashboard.DashUtils;
import com.example.GYMmanagementsystem.subsriptions.FollowSubsriptionsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.ResultSet;
import java.util.*;
public class dashboardController implements Initializable, DashUtils,AlertDisplay {
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
    //start followsubsriptions
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
    private ResultSet result;
    FollowSubsriptionsController fSubsriptionubsController = new FollowSubsriptionsController(databaseHandler);
    private ObservableList<Client> clientListData;


    public void FollowSubsAdd(){
        try {if(fSubsriptionubsController.isFieldsEmpty(FS_ClientId_tf,  FS_FirstName_tf, FS_LastName_tf,
                FS_Gender_combobox,  FS_Cin_tf,  FS_Gmail_tf,  FS_Phone_tf)) {
                showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");

            }else{
                String check = "SELECT ClientId FROM client WHERE clientId = ?";

                String id =FS_ClientId_tf.getText();
                result = databaseHandler.executeQuery(check,id);

                if (result.next()) {
                    showAlert(Alert.AlertType.ERROR,"Error Message","ClienID : " + id + " was already exist!");

                } else {
                    Date date = new Date();
                    java.sql.Date sqldate = new java.sql.Date(date.getTime());
                    String sql = "INSERT INTO client (ClientID, FirstName, LastName, Phone, Gmail, Cin, Gender,StartDate,ExpirationDate)"
                            + "VALUES(?,?,?,?,?,?,?,?,?)";

                    id = FS_ClientId_tf.getText();
                    String firstName = FS_FirstName_tf.getText();
                    String lastName = FS_LastName_tf.getText();
                    String phone = FS_Phone_tf.getText();
                    String gmail = FS_Gmail_tf.getText();
                    String cin = FS_Cin_tf.getText();
                    String gender=(String) FS_Gender_combobox.getSelectionModel().getSelectedItem();
                    String dateSt = String.valueOf(sqldate);
                    String dateEx=String.valueOf(sqldate);
                    databaseHandler.executeUpdate(sql,id,firstName,lastName,phone,gmail,cin,gender,dateSt,dateEx);
                    showAlert(Alert.AlertType.INFORMATION,"Information Message","Successfully Added!");

                    fSubsriptionubsController.ShowListData(clientListData,FS_Table_View,FS_TableCol_ClientId,FS_TableCol_FirstName,FS_TableCol_LastName,
                            FS_TableCol_Gender,FS_TableCol_Phone,FS_TableCol_Gmail,FS_TableCol_Cin,FS_TableCol_StartDate);
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
        try {
            if (fSubsriptionubsController.isFieldsEmpty(FS_ClientId_tf,  FS_FirstName_tf, FS_LastName_tf,
                    FS_Gender_combobox,  FS_Cin_tf,  FS_Gmail_tf,  FS_Phone_tf)){
                showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            }else{
                Optional<ButtonType> option= showAlertForButtom(Alert.AlertType.CONFIRMATION, "Cofirmation Message", "Are you sure you want to UPDATE Client ID: " + FS_ClientId_tf.getText() + "?");
                if(option.get().equals(ButtonType.OK) ){
                    String sqlUpdate = " UPDATE client SET FirstName = ?, LastName = ?, Phone = ?, Gmail = ?, Cin = ?, Gender = ?"
                            +"WHERE ClientId = ?";
                    String firstName = FS_FirstName_tf.getText();
                    String lastName = FS_LastName_tf.getText();
                    String phone = FS_Phone_tf.getText();
                    String gmail = FS_Gmail_tf.getText();
                    String cin = FS_Cin_tf.getText();
                    String gender=(String) FS_Gender_combobox.getSelectionModel().getSelectedItem();
                    String id = FS_ClientId_tf.getText();

                    databaseHandler.executeUpdate(sqlUpdate,firstName,lastName,phone,gmail,cin,gender,id);
                    showAlert(Alert.AlertType.INFORMATION,"Information Message","Successfully Updated!");
                    fSubsriptionubsController.ShowListData(clientListData,FS_Table_View,FS_TableCol_ClientId,FS_TableCol_FirstName,FS_TableCol_LastName,
                            FS_TableCol_Gender,FS_TableCol_Phone,FS_TableCol_Gmail,FS_TableCol_Cin,FS_TableCol_StartDate);
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

        try {
            if (fSubsriptionubsController.isFieldsEmpty(FS_ClientId_tf,  FS_FirstName_tf, FS_LastName_tf,
                    FS_Gender_combobox,  FS_Cin_tf,  FS_Gmail_tf,  FS_Phone_tf)){
                showAlert(Alert.AlertType.ERROR,"Error Message","Please fill all blank fields");

            }else{
                Optional<ButtonType> option= showAlertForButtom(Alert.AlertType.CONFIRMATION, "Cofirmation Message", "Are you sure you want to Delete Client ID: " + FS_ClientId_tf.getText() + "?");
                if(option.get().equals(ButtonType.OK) ){
                    String insertQuitHistoric = "INSERT INTO quithistoric (id, counter) VALUES (CURDATE(), 1) " +
                            "ON DUPLICATE KEY UPDATE counter = counter + 1";
                    try {
                        databaseHandler.executeUpdate(insertQuitHistoric);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the error
                    }

                    // Delete the client
                    String deleteClient = "DELETE FROM client WHERE ClientId = ?";
                    try {
                        databaseHandler.executeUpdate(deleteClient,FS_ClientId_tf.getText());
                        showAlert(Alert.AlertType.INFORMATION,"Information Message","Successfully Deleted!");
                        fSubsriptionubsController.ShowListData(clientListData,FS_Table_View,FS_TableCol_ClientId,FS_TableCol_FirstName,FS_TableCol_LastName,
                                FS_TableCol_Gender,FS_TableCol_Phone,FS_TableCol_Gmail,FS_TableCol_Cin,FS_TableCol_StartDate);
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
        fSubsriptionubsController.FollowSubsSearch(clientListData,FS_search_tf,FS_Table_View);
    }
    public void FollowSubsSelect(){
        fSubsriptionubsController.SelectItem(FS_Table_View,FS_ClientId_tf,FS_FirstName_tf,FS_LastName_tf,
                FS_Phone_tf,FS_Gmail_tf,FS_Cin_tf);
    }
    public void FollowSubsReset() {
        FS_ClientId_tf.setText("");
        FS_FirstName_tf.setText("");
        FS_LastName_tf.setText("");
        FS_Gender_combobox.getSelectionModel().clearSelection();
        FS_Phone_tf.setText("");
        FS_Gmail_tf.setText("");
        FS_Cin_tf.setText("");
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

            fSubsriptionubsController.ShowListData(clientListData,FS_Table_View,FS_TableCol_ClientId,FS_TableCol_FirstName,FS_TableCol_LastName,
                    FS_TableCol_Gender,FS_TableCol_Phone,FS_TableCol_Gmail,FS_TableCol_Cin,FS_TableCol_StartDate);
            fSubsriptionubsController.setGenderOptions(FS_Gender_combobox);


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
        Optional<ButtonType> option= showAlertForButtom(Alert.AlertType.CONFIRMATION, "Cofirmation Message", "Are you sure you want to logout?");
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

    //Payment
    private PaymentService paymentService = new PaymentService(databaseHandler);
    private ObservableList<Client> PaymentData = FXCollections.observableArrayList();
    //this function for showing the list that contain client data in tableview
    public void PaymentShowListData() {
        PaymentData= fSubsriptionubsController.fetchClientData();
        FXCollections.sort(PaymentData,new ExpirationDateComparator());
        Paiement_col_ClientID.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        Paiement_col_FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Paiement_col_LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Paiement_col_ExpirationDate.setCellValueFactory(new PropertyValueFactory<>("ExpirationDate"));
        Paiement_tableView.setItems(PaymentData);
    }
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
            else{
                showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid payment, Client doesn't exist!");

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
        home_form.setVisible(true);
        follow_subsriptions_form.setVisible(false);
        paiement_form.setVisible(false);

        Home_button.setStyle("-fx-background-color:linear-gradient(to right, #35c41a, #35b121, #349e26, #338b28, #327929);");
        follow_subscriptions_button.setStyle("-fx-background-color:transparent");
        Paiement_button.setStyle("-fx-background-color:transparent");
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
        fSubsriptionubsController.SelectItem(FS_Table_View,FS_ClientId_tf,FS_FirstName_tf,FS_LastName_tf,
                FS_Phone_tf,FS_Gmail_tf,FS_Cin_tf);
        fSubsriptionubsController.ShowListData(clientListData,FS_Table_View,FS_TableCol_ClientId,FS_TableCol_FirstName,FS_TableCol_LastName,
                FS_TableCol_Gender,FS_TableCol_Phone,FS_TableCol_Gmail,FS_TableCol_Cin,FS_TableCol_StartDate);
        //FollowSubsGenderList();
        fSubsriptionubsController.setGenderOptions(FS_Gender_combobox);
        PaymentSelect();
        PaymentShowListData();
    }
}
