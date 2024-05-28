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
import java.util.Objects;
import java.util.ResourceBundle;

public class LogInController implements Initializable , AlertDisplay{
    @FXML
    private Button button_login;
    @FXML
    private Button button_close;
    @FXML
    private PasswordField tf_password;
    @FXML
    private TextField tf_username;
    //database tools
    private Database DatabaseHandler;
    public LogInController(){
        this.DatabaseHandler=new Database();
    }
    private double x = 0;
    private double y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void LogIn(){
       String sql = "SELECT username,password FROM admin WHERE username=? AND password=?";
        try {
            if(tf_password.getText().isEmpty() || tf_username.getText().isEmpty()){
                showAlert(Alert.AlertType.ERROR,"Error Message","Please fill all blank fields");
            }
            else{
                ResultSet result = DatabaseHandler.executeQuery(sql, tf_username.getText(), tf_password.getText());
                if(result.next()){
                    showAlert(Alert.AlertType.INFORMATION,"Information Message","Successfully Login");
                button_login.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
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
                    showAlert(Alert.AlertType.ERROR,"Error Message","Wrong Username/Password");

            }


        }}catch(Exception e){e.printStackTrace();}
    }
    public void Close(){
        System.exit(0);
    }   
}
