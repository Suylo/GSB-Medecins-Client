package fr.suylo.gsbmedecins.controllers.country;

import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;

public class EditPaysController {

    @FXML
    public TextField countryName;
    @FXML
    public Button buttonSave;
    @FXML
    public Label countryID;

    private Integer id;

    public void loadData(Integer index) {
        this.setId(index);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void loadEditPays() {

        Pays unPays = APIAccess.getPaysById(this.getId());

        countryID.setText("Modification du pays N°" + unPays.getId().toString());
        countryName.setText(unPays.getNom());

        buttonSave.setOnAction(event -> {
            Pane paysEdited = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pays.fxml"));
            try {
                paysEdited = loader.load();
                paysEdited.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Liste des pays");
            stage.changeScene(paysEdited);


            APIAccess.updatePays(this.getId(), countryName.getText());

            PaysController paysController = loader.getController();
            paysController.reload();
        });
    }

}
