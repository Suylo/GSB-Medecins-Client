package fr.suylo.gsbmedecins.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.profile.EditProfileController;
import fr.suylo.gsbmedecins.controllers.profile.ProfileController;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Medecin;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedecinController implements Initializable {

    @FXML
    public TableView<Medecin> myTable;
    @FXML
    public TableColumn<Medecin, Integer> id;
    @FXML
    public TableColumn<Medecin, String> nom, prenom, adresse, spe;
    @FXML
    public TableColumn<Medecin, Medecin> action = new TableColumn<>("Action");

    @FXML
    public Label titleMedecins;
    public final Button addMedecin = new Button("Ajouter un médecin");
    @FXML
    public Label titleBlank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addMedecin.getStyleClass().add("button-see");
        if(UserSession.getUserLoggedOn()){
            titleBlank.setGraphic(addMedecin);
            addMedecin.setAlignment(Pos.BOTTOM_CENTER);
            addMedecin.setOnAction(event -> {
                Pane addDoctor = null;
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addDoctorInfo.fxml"));
                try {
                    addDoctor = loader.load();
                    addDoctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CustomStage stage = ((CustomStage) addMedecin.getScene().getWindow());
                stage.setTitle("GSB - Ajout d'un médecin");
                stage.changeScene(addDoctor);
            });
        }

        id.setVisible(false);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        spe.setCellValueFactory(new PropertyValueFactory<>("spe"));
        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            public final Button seeButton = new Button();

            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);

                seeButton.setOnAction(event -> {
                    Pane doctor = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorInfo.fxml"));
                    try {
                        doctor = loader.load();
                        doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ProfileController profileController = loader.getController();
                    profileController.loadData(id.getCellData(getTableRow().getIndex()));
                    profileController.loadProfile();
                    CustomStage stage = ((CustomStage) seeButton.getScene().getWindow());
                    stage.setTitle("GSB - Profil d'un médecin ");
                    stage.changeScene(doctor);
                });
            }
        });

        reload();
        myTable.setPlaceholder(new Label("Erreur de chargement des médecins !"));
    }

    public void reload() {
        myTable.getItems().clear();
        myTable.getItems().addAll(APIAccess.getAllMedecins());
    }
}
