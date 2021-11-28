module openmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires zip4j;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires fuzzywuzzy;
    requires toml4j;
    requires java.desktop;
    requires org.apache.commons.lang3;

    exports openmanager.fxml;
    exports openmanager.modpackInsider;
    exports openmanager;
    opens openmanager.fxml to javafx.fxml;
    exports openmanager.datamodels.other;
    exports openmanager.repo;

}