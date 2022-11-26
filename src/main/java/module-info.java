module com.brian.london_underground {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;

    opens com.brian.london_underground.model to javafx.fxml;
    opens com.brian.london_underground.ui to javafx.fxml;
    opens com.brian.london_underground to javafx.fxml;

    exports com.brian.london_underground.ui;
    exports com.brian.london_underground.model;
    exports com.brian.london_underground;
    exports com.brian.london_underground.graph;
}