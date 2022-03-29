package fr.suylo.gsbmedecins.controllers.department;

import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;

public class EditDepartementController {

    @FXML
    public ComboBox<Pays> countrySelect;
    @FXML
    public TextField departmentName;
    @FXML
    public Button buttonSave;
    @FXML
    public Label departmentID;

    private Integer id;
    private Long countryValue;

    public void loadData(Integer index) {
        this.setId(index);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void loadEditProfile() {

        ObservableList<Pays> lesPays = APIAccess.getAllPays();
        Departement unDepartement = APIAccess.getDepartementById(getId());
        ObservableList<Pays> pays = FXCollections.observableArrayList();
        pays.addAll(lesPays);

        departmentID.setText("Modification du département N°" + unDepartement.getId().toString());
        departmentName.setText(unDepartement.getNom());
        countrySelect.getItems().addAll(pays);
        countrySelect.setConverter(new StringConverter<>() {
            @Override
            public String toString(Pays object) {
                return object.getNom();
            }

            @Override
            public Pays fromString(String string) {
                return countrySelect.getItems().stream().filter(ap -> ap.getNom().equals(string)).findFirst().orElse(null);
            }
        });
        countrySelect.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                this.countryValue = newval.getId();
            }
        });
        countrySelect.setValue(unDepartement.getPays());

        buttonSave.setOnAction(event -> {
            Pane departmentEditedNewList = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("departements.fxml"));
            try {
                departmentEditedNewList = loader.load();
                departmentEditedNewList.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Liste des départements");
            stage.changeScene(departmentEditedNewList);

            ObservableList<Integer> valueID = APIAccess.getPaysByNom(countrySelect.getValue().getNom());
            APIAccess.updateDepartement(unDepartement.getId(), departmentName.getText(), valueID.get(0).longValue());

            DepartementController departementController = loader.getController();
            departementController.reload();
        });
    }

}
