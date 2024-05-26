package com.example.GYMmanagementsystem.Utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public interface ComboBox {
    default  <T> void populateComboBox(List<T> list, javafx.scene.control.ComboBox<T> holder){
        ObservableList<T> listData = FXCollections.observableArrayList(list);
        holder.setItems(listData);
    }
}
