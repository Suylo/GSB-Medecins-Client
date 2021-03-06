package fr.suylo.gsbmedecins.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.profile.EditProfileController;
import fr.suylo.gsbmedecins.controllers.profile.ProfileController;
import fr.suylo.gsbmedecins.models.APIAccess;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.util.Optional;

public class CRUDController {

    private static String speValue;

    public static void onDelete(Button btn, TableView myTable, Integer index, String spe, String nom, String prenom) {
        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppresion d'un médecin");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le médecin N°" + index + " ?");

            if (spe == null){
                speValue = "//";
            } else {
                speValue = spe;
            }

            Label label = new Label();
            label.setText("- Nom : " + nom
                    + "\n- Prénom : " + prenom
                    + "\n- Spécialité : " + speValue
            );
            label.setStyle("-fx-text-fill: black;-fx-font-family: 'Roboto Light';-fx-font-size: 17px;");

            VBox vBox = new VBox();
            vBox.getChildren().addAll(label);

            alert.getDialogPane().setContent(vBox);
            alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-background-color:#202940 ;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
            alert.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("-fx-background-color: #202940;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
            Stage stage;
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    APIAccess.deleteMedecinByID(index);
                    System.out.println("Médecin numero : supprimé :: " + index);
                    myTable.getItems().clear();
                    myTable.setPlaceholder(new Label("Le médecin a bien été supprimé, veuillez reeffectuer la recherche !"));
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void onEdit(Button editBtn, Integer index) {
        editBtn.setOnAction(event -> {
            Pane doctor = null;
            FXMLLoader loader = new FXMLLoader(CRUDController.class.getClassLoader().getResource("editDoctorInfo.fxml"));
            try {
                doctor = loader.load();
                doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            EditProfileController editProfileController = loader.getController();
            editProfileController.loadData(index);
            editProfileController.loadEditProfile();
            CustomStage stage = ((CustomStage) editBtn.getScene().getWindow());
            stage.setTitle("GSB - Modification d'un médecin ");
            stage.changeScene(doctor);
        });
    }

    public static void onRead(Button readBtn, Integer index) {
        readBtn.setOnAction(event -> {
            Pane doctor = null;
            FXMLLoader loader = new FXMLLoader(CRUDController.class.getClassLoader().getResource("doctorInfo.fxml"));
            try {
                doctor = loader.load();
                doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ProfileController profileController = loader.getController();
            profileController.loadData(index);
            profileController.loadProfile();
            CustomStage stage = ((CustomStage) readBtn.getScene().getWindow());
            stage.setTitle("GSB - Profil d'un médecin ");
            stage.changeScene(doctor);
        });
    }
}
