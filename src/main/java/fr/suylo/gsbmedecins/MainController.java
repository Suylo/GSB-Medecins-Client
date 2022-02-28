package fr.suylo.gsbmedecins;

import fr.suylo.gsbmedecins.models.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Admins UI buttons
    @FXML
    public Button navHomeAdmin, navSearchAdmin, navDoctorsAdmin, navAdminLogout;
    // Not connected UI buttons
    @FXML
    Button navHome, navSearch, navDoctors, navIndex, navCountry;
    private Pane homeAdmin, searchAdmin, doctorsAdmin;
    private Pane home, search, doctors, login, country;

    public void initialize(URL location, ResourceBundle resources) {

        try {
            // Basic
            home = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("home.fxml")));
            home.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

            doctors = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("doctors.fxml")));
            doctors.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

            search = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/search.fxml")));
            search.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

            login = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login2.fxml")));
            login.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

            country = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("pays.fxml")));
            country.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
/*            FXMLLoader loaderLogin = new FXMLLoader(Main.class.getResource("login2.fxml"));
            LoginController loginController = new LoginController();
            loaderLogin.setController(loginController);*/

            // Admin pages
            homeAdmin = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("home-admin.fxml")));
            homeAdmin.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (UserSession.getUserID() == null && UserSession.getUserPassword() == null) {
            navHome.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navHome.getScene().getWindow());
                stage.setTitle("GSB - Accueil");
                stage.changeScene(home);
            });

            navDoctors.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navDoctors.getScene().getWindow());
                stage.setTitle("GSB - Liste des médecins");
                stage.changeScene(doctors);
            });

            navSearch.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navSearch.getScene().getWindow());
                stage.setTitle("GSB - Recherche d'un médecin");
                stage.changeScene(search);
            });

            navIndex.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navIndex.getScene().getWindow());
                stage.setTitle("GSB - Connexion à l'interface d'administration");
                stage.changeScene(login);
            });

            navCountry.setOnAction(event -> {
                CustomStage stage = (CustomStage) navCountry.getScene().getWindow();
                stage.setTitle("GSB - Liste des pays");
                stage.changeScene(country);
            });
        } else {
            navHomeAdmin.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navHomeAdmin.getScene().getWindow());
                stage.setTitle("GSB - Accueil");
                stage.changeScene(homeAdmin);
            });

            navDoctorsAdmin.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navDoctorsAdmin.getScene().getWindow());
                stage.setTitle("GSB - Liste des médecins");
                stage.changeScene(doctors);
            });

            navSearchAdmin.setOnAction(event -> {
                CustomStage stage = ((CustomStage) navSearchAdmin.getScene().getWindow());
                stage.setTitle("GSB - Recherche d'un médecin");
                stage.changeScene(search);
            });

            navAdminLogout.setOnAction(event -> {
                UserSession.cleanUserSession();
            });
        }
    }
}
