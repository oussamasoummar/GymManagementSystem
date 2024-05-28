package com.example.GYMmanagementsystem.subsriptions;

import com.example.GYMmanagementsystem.Client;
import com.example.GYMmanagementsystem.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class FollowSubsriptionsController {
    private Database databasehandler;

    public FollowSubsriptionsController(Database databaseHandler) {
        this.databasehandler = databaseHandler;
    }





    public void setGenderOptions(ComboBox<String> genderComboBox) {
        ObservableList<String> options = FXCollections.observableArrayList("male", "female");
        genderComboBox.setItems(options);
    }
    public ObservableList<Client> fetchClientData() {
        ObservableList<Client> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client";

        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                Client client = new Client(result.getInt("ClientId"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getString("phone"),
                        result.getString("Gmail"),
                        result.getString("Cin"),
                        result.getDate("startDate"),
                        result.getDate("ExpirationDate"));
                listData.add(client);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void FollowSubsSearch(ObservableList<Client> clientListData, TextField FS_search_tf, TableView<Client> FS_Table_View) {


        //the filter are initially all the element matches because of lamdba expression return true
        clientListData = this.fetchClientData();
        if( clientListData != null) {

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
    }
    public void ShowListData(ObservableList<Client> clientListData,
                                          TableView<Client> tableView,
                                          TableColumn<Client, String> colClientId,
                                          TableColumn<Client, String> colFirstName,
                                          TableColumn<Client, String> colLastName,
                                          TableColumn<Client, String> colGender,
                                          TableColumn<Client, String> colPhone,
                                          TableColumn<Client, String> colGmail,
                                          TableColumn<Client, String> colCin,
                                          TableColumn<Client, String> colStartDate
    ){
        clientListData = this.fetchClientData();
        colClientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        tableView.setItems(clientListData);

    }
    public void SelectItem(TableView<Client> tableView,
                           TextField clientId,
                           TextField firstName,
                           TextField lastName,
                           TextField phone,
                           TextField gmail,
                           TextField cin
                           ) {
        Client clientSelected = tableView.getSelectionModel().getSelectedItem();

        if (clientSelected == null) {
            return;
        }

        clientId.setText(String.valueOf(clientSelected.getClientId()));
        firstName.setText(clientSelected.getFirstName());
        lastName.setText(clientSelected.getLastName());
        cin.setText(clientSelected.getCin());
        gmail.setText(clientSelected.getGmail());
        phone.setText(clientSelected.getPhone());
    }
    public  boolean isFieldsEmpty(TextField clientId, TextField firstName, TextField lastName,
                                        ComboBox<String> genderComboBox, TextField cin, TextField gmail, TextField phone) {
        return clientId.getText().isEmpty()
                || firstName.getText().isEmpty()
                || lastName.getText().isEmpty()
                || genderComboBox.getSelectionModel().getSelectedItem() == null
                || cin.getText().isEmpty()
                || gmail.getText().isEmpty()
                || phone.getText().isEmpty();
    }



}
