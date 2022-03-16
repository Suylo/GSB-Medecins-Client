package fr.suylo.gsbmedecins.controllers.admin;

import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.User;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    public Label userLastName, userFirstName, userAddress, userEmbauche, userTitle, userID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String userLogged = UserSession.getUserID();
        User unAdmin = APIAccess.getUserInfoByID(userLogged);

        userID.setText(String.valueOf(unAdmin.getLogin()));
        userLastName.setText(unAdmin.getNom());
        userFirstName.setText(unAdmin.getPrenom());
        userTitle.setText("Bienvenue " + unAdmin.getNom() + " " + unAdmin.getPrenom() + " !");
        userAddress.setText(unAdmin.getAdresse());
        userEmbauche.setText(unAdmin.getEmbauche());
    }

}
