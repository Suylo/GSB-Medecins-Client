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
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        spe.setCellValueFactory(new PropertyValueFactory<>("spe"));
        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            public final Button seeButton = new Button();
            public final Button editButton = new Button();
            public final Button removeButton = new Button();
            public final HBox listButtons = new HBox(seeButton, editButton, removeButton);
            final SVGPath svgEdit = new SVGPath();
            final SVGPath svgSee = new SVGPath();
            final SVGPath svgRemove = new SVGPath();

            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);

                svgEdit.setContent("M12 21h9c0.552 0 1-0.448 1-1s-0.448-1-1-1h-9c-0.552 0-1 0.448-1 1s0.448 1 1 1zM15.793 2.793l-12.5 12.5c-0.122 0.121-0.217 0.28-0.263 0.465l-1 4c-0.039 0.15-0.042 0.318 0 0.485 0.134 0.536 0.677 0.862 1.213 0.728l4-1c0.167-0.041 0.33-0.129 0.465-0.263l12.5-12.5c0.609-0.609 0.914-1.41 0.914-2.207s-0.305-1.598-0.914-2.207-1.411-0.915-2.208-0.915-1.598 0.305-2.207 0.914zM17.207 4.207c0.219-0.219 0.504-0.328 0.793-0.328s0.574 0.109 0.793 0.328 0.328 0.504 0.328 0.793-0.109 0.574-0.328 0.793l-12.304 12.304-2.115 0.529 0.529-2.115z");
                svgEdit.setFill(Color.WHITE);
                svgSee.setContent("M0.106 11.553c-0.136 0.274-0.146 0.603 0 0.894 0 0 0.396 0.789 1.12 1.843 0.451 0.656 1.038 1.432 1.757 2.218 0.894 0.979 2.004 1.987 3.319 2.8 1.595 0.986 3.506 1.692 5.698 1.692s4.103-0.706 5.698-1.692c1.315-0.813 2.425-1.821 3.319-2.8 0.718-0.786 1.306-1.562 1.757-2.218 0.724-1.054 1.12-1.843 1.12-1.843 0.136-0.274 0.146-0.603 0-0.894 0 0-0.396-0.789-1.12-1.843-0.451-0.656-1.038-1.432-1.757-2.218-0.894-0.979-2.004-1.987-3.319-2.8-1.595-0.986-3.506-1.692-5.698-1.692s-4.103 0.706-5.698 1.692c-1.315 0.813-2.425 1.821-3.319 2.8-0.719 0.786-1.306 1.561-1.757 2.218-0.724 1.054-1.12 1.843-1.12 1.843zM2.14 12c0.163-0.281 0.407-0.681 0.734-1.158 0.41-0.596 0.94-1.296 1.585-2.001 0.805-0.881 1.775-1.756 2.894-2.448 1.35-0.834 2.901-1.393 4.647-1.393s3.297 0.559 4.646 1.393c1.119 0.692 2.089 1.567 2.894 2.448 0.644 0.705 1.175 1.405 1.585 2.001 0.328 0.477 0.572 0.876 0.734 1.158-0.163 0.281-0.407 0.681-0.734 1.158-0.41 0.596-0.94 1.296-1.585 2.001-0.805 0.881-1.775 1.756-2.894 2.448-1.349 0.834-2.9 1.393-4.646 1.393s-3.297-0.559-4.646-1.393c-1.119-0.692-2.089-1.567-2.894-2.448-0.644-0.705-1.175-1.405-1.585-2.001-0.328-0.477-0.572-0.877-0.735-1.158zM16 12c0-1.104-0.449-2.106-1.172-2.828s-1.724-1.172-2.828-1.172-2.106 0.449-2.828 1.172-1.172 1.724-1.172 2.828 0.449 2.106 1.172 2.828 1.724 1.172 2.828 1.172 2.106-0.449 2.828-1.172 1.172-1.724 1.172-2.828zM14 12c0 0.553-0.223 1.051-0.586 1.414s-0.861 0.586-1.414 0.586-1.051-0.223-1.414-0.586-0.586-0.861-0.586-1.414 0.223-1.051 0.586-1.414 0.861-0.586 1.414-0.586 1.051 0.223 1.414 0.586 0.586 0.861 0.586 1.414z");
                svgSee.setFill(Color.WHITE);
                svgRemove.setContent("M18 7v13c0 0.276-0.111 0.525-0.293 0.707s-0.431 0.293-0.707 0.293h-10c-0.276 0-0.525-0.111-0.707-0.293s-0.293-0.431-0.293-0.707v-13zM17 5v-1c0-0.828-0.337-1.58-0.879-2.121s-1.293-0.879-2.121-0.879h-4c-0.828 0-1.58 0.337-2.121 0.879s-0.879 1.293-0.879 2.121v1h-4c-0.552 0-1 0.448-1 1s0.448 1 1 1h1v13c0 0.828 0.337 1.58 0.879 2.121s1.293 0.879 2.121 0.879h10c0.828 0 1.58-0.337 2.121-0.879s0.879-1.293 0.879-2.121v-13h1c0.552 0 1-0.448 1-1s-0.448-1-1-1zM9 5v-1c0-0.276 0.111-0.525 0.293-0.707s0.431-0.293 0.707-0.293h4c0.276 0 0.525 0.111 0.707 0.293s0.293 0.431 0.293 0.707v1zM9 11v6c0 0.552 0.448 1 1 1s1-0.448 1-1v-6c0-0.552-0.448-1-1-1s-1 0.448-1 1zM13 11v6c0 0.552 0.448 1 1 1s1-0.448 1-1v-6c0-0.552-0.448-1-1-1s-1 0.448-1 1z");
                svgRemove.setFill(Color.WHITE);

                seeButton.setGraphic(svgSee);
                editButton.setGraphic(svgEdit);
                removeButton.setGraphic(svgRemove);

                if (UserSession.getUserLoggedOn()) {
                    listButtons.setAlignment(Pos.CENTER);
                    setGraphic(empty ? null : listButtons);
                    seeButton.getStyleClass().add("button-see-admin");
                } else {
                    setGraphic(empty ? null : seeButton);
                    seeButton.getStyleClass().add("button-see");
                    action.setMaxWidth(3000.0);
                }

                editButton.getStyleClass().add("button-edit");
                removeButton.getStyleClass().add("button-remove");
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
                editButton.setOnAction(event -> {
                    Pane doctor = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("editDoctorInfo.fxml"));
                    try {
                        doctor = loader.load();
                        doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EditProfileController editProfileController = loader.getController();
                    editProfileController.loadData(id.getCellData(getTableRow().getIndex()));
                    editProfileController.loadEditProfile();
                    CustomStage stage = ((CustomStage) editButton.getScene().getWindow());
                    stage.setTitle("GSB - Profil d'un médecin ");
                    stage.changeScene(doctor);
                });
                removeButton.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppresion d'un médecin");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le médecin N°" + id.getCellData(getTableRow().getIndex()) + " ?");
                    Stage stage;
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        try {
                            APIAccess.deleteMedecinByID(id.getCellData(getTableRow().getIndex()));
                        } catch (UnirestException e) {
                            e.printStackTrace();
                        }
                        myTable.getItems().clear();
                        myTable.getItems().addAll(APIAccess.getAllMedecins());
                    }
                });
            }
        });

        /*

        myTable.refresh();
        myTable.getItems().addAll(APIAccess.getAllMedecins());
         */
        reload();
        myTable.setPlaceholder(new Label("Erreur de chargement des médecins !"));
    }


    public void reload() {
        myTable.getItems().clear();
        myTable.getItems().addAll(APIAccess.getAllMedecins());
    }
}
