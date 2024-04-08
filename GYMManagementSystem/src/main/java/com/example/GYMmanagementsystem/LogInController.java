package com.example.GYMmanagementsystem;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button button_login;
    @FXML
    private Button button_close;
    @FXML
    private PasswordField tf_password;
    @FXML
    private TextField tf_username;
    //database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result ;
    private double x = 0;
    private double y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void LogIn(){
       String sql = "SELECT username,password FROM admin WHERE username=? AND password=?";
        connect = Database.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1,tf_username.getText());
            prepare.setString(2,tf_password.getText());
            result = prepare.executeQuery();
            Alert alert;
            if(tf_password.getText().isEmpty() || tf_username.getText().isEmpty()){
                 alert = new Alert(AlertType.ERROR);
                 alert.setTitle("Error Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Please fill all blank fields");
                 alert.showAndWait();

            }
            else{
                if(result.next()){
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Login");
                alert.showAndWait();

                button_login.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();

            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Username/Password");
                alert.showAndWait();
            }


        }}catch(Exception e){e.printStackTrace();}
    }
    public void Close(){
        System.exit(0);
    }   
}
