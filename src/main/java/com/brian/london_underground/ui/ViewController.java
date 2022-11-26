package com.brian.london_underground.ui;

import com.brian.london_underground.graph.Path;
import com.brian.london_underground.model.LondonUndergroundManager;
import com.brian.london_underground.model.Station;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.Desktop;

public class ViewController implements Initializable {

    private LondonUndergroundManager controller;

    private ObservableList<Station> list;

    @FXML
    private ImageView logo;

    private Image logoImage;

    @FXML
    private TableView<Station> tableFrom;
    @FXML
    private TableView<Station> tableTo;
    @FXML
    private TableColumn<Station, String> tableColFrom;
    @FXML
    private TableColumn<Station, String> tableColTo;

    @FXML
    private TextField searchBarFrom;

    @FXML
    private TextField searchBarTo;

    @FXML
    private ListView<String> route;

    @FXML
    private TextField travelTime;

    public final String LOGO_URL = "https://assets.stickpng.com/thumbs/5842f074a6515b1e0ad75b16.png";
    public final String MAP_URL = "https://content.tfl.gov.uk/standard-tube-map.pdf";


    public ViewController(){

         list = FXCollections.observableArrayList();
         controller = new LondonUndergroundManager();
         route = new ListView<>();

         logoImage = new Image(LOGO_URL);

         list.addAll(controller.getStations().values());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logo.setImage(logoImage);

        setUpTable(tableFrom, tableColFrom, searchBarFrom);
        setUpTable(tableTo, tableColTo, searchBarTo);


    }

    public void setTableEvents(TableView<Station> table, TextField searchBar){
         table.setOnMouseClicked((event) ->{
             Station selectedItem = table.getSelectionModel().getSelectedItem();
             if(selectedItem != null)
                 searchBar.setText(selectedItem.getName());
         });

    }

    public void setUpTable(TableView<Station> table, TableColumn<Station, String> col, TextField searchBar){

        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        FilteredList<Station> filteredData = new FilteredList<>(list, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(station -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(station.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });


        SortedList<Station> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setTableEvents(table, searchBar);

    }

    public void findShortestPath(){

        String from = searchBarFrom.getText();
        String to = searchBarTo.getText();

        if(to.isEmpty() || from.isEmpty()) return;

        Path<Station> path = controller.findFastestRoute(from, to);

        ObservableList<String> routeList = FXCollections.observableArrayList();

        path.getPath().forEach(station -> routeList.add(station.getName()));

        route.setItems(routeList);

        travelTime.setText(path.getDistance() + " min");

    }

    public void showMap(){
        controller.showMap(MAP_URL);
    }

}