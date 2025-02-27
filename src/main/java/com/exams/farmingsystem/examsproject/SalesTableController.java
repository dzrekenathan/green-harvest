package com.exams.farmingsystem.examsproject;


import com.exams.farmingsystem.examsproject.models.Sales;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;



public class SalesTableController implements Initializable {

    @FXML
    private TableView<Sales> allSalesTable;

    @FXML
    private TableColumn<Sales, String> salesProdNameCol;

    @FXML
    private TableColumn<Sales, Integer> salesProdIdCol;

    @FXML
    private TableColumn<Sales, Integer> salesIdCol;

    @FXML
    private TableColumn<Sales, String> salesDateCol;

    @FXML
    private TableColumn<Sales, Integer> salesQuantiyCol;

    @FXML
    private TableColumn<Sales, Double> salesAmountCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind columns to Sales properties
        salesProdNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        salesProdIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        salesIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        salesDateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        salesQuantiyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        salesAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Fetch and display sales data
        ObservableList<Sales> sales = DatabaseClass.fetchAllSales();
        allSalesTable.setItems(sales);
    }
}

//package com.exams.farmingsystem.examsproject;
//
//
//import com.exams.farmingsystem.examsproject.models.Sales;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class SalesTableController implements Initializable {
//
//    @FXML
//    private TableView<Sales> allSalesTable;
//
//    @FXML
//    private TableColumn<Sales, String> salesProdNameCol;
//
//    @FXML
//    private TableColumn<Sales, Integer> salesProdIdCol;
//
//    @FXML
//    private TableColumn<Sales, Integer> salesIdCol;
//
//    @FXML
//    private TableColumn<Sales, String> salesDateCol;
//
//    @FXML
//    private TableColumn<Sales, Integer> salesQuantiyCol;
//
//    @FXML
//    private TableColumn<Sales, Double> salesAmountCol;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // Bind columns to Sales properties
//        salesProdNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
//        salesProdIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
//        salesIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
//        salesDateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
//        salesQuantiyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        salesAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
//
//        // Fetch and display sales data
//        ObservableList<Sales> sales = DatabaseClass.fetchAllSales();
//        allSalesTable.setItems(sales);
//    }
//}