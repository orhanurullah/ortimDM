module com.ortimdm.ortimdm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.web;
    requires java.logging;
    opens com.ortimdm.ortimdm to javafx.fxml;
    exports com.ortimdm.ortimdm;
    exports com.ortimdm.ortimdm.controller;
    opens com.ortimdm.ortimdm.controller to javafx.fxml;
}