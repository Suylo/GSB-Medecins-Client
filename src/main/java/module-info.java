module fr.suylo.gsbmedecins {
    requires javafx.controls;
    requires javafx.fxml;
    requires CustomStage;

    //needed for HTTP
    requires unirest.java;

    //needed for JSON
    requires com.google.gson;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens fr.suylo.gsbmedecins to javafx.fxml;
    exports fr.suylo.gsbmedecins;
    exports fr.suylo.gsbmedecins.models;
    opens fr.suylo.gsbmedecins.models to javafx.fxml;
    exports fr.suylo.gsbmedecins.controllers;
    opens fr.suylo.gsbmedecins.controllers to javafx.fxml;
    exports fr.suylo.gsbmedecins.controllers.search;
    opens fr.suylo.gsbmedecins.controllers.search to javafx.fxml;
    exports fr.suylo.gsbmedecins.controllers.admin;
    opens fr.suylo.gsbmedecins.controllers.admin to javafx.fxml;
    exports fr.suylo.gsbmedecins.controllers.profile;
    opens fr.suylo.gsbmedecins.controllers.profile to javafx.fxml;
    exports fr.suylo.gsbmedecins.controllers.country;
    opens fr.suylo.gsbmedecins.controllers.country to javafx.fxml;
}