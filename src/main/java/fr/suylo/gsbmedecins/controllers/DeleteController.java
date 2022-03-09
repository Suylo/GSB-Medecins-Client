package fr.suylo.gsbmedecins.controllers;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class DeleteController {

    public static void onDelete(Button btn, TableView myTable, Integer index){
        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppresion d'un médecin");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le médecin N°" +  index + " ?");
            Stage stage;
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    Unirest.delete("http://localhost:8080/api/v1/medecins/delete/" + index).asJson();
                    System.out.println("Médecin numero : supprimé :: " + index);
                    myTable.getItems().clear();
                    myTable.setPlaceholder(new Label("Le médecin a bien été supprimé, veuillez reeffectuer la recherche !"));
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
