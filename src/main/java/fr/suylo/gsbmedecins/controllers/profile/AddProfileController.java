package fr.suylo.gsbmedecins.controllers.profile;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.MedecinController;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProfileController implements Initializable {

    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public Label formCheckError;
    @FXML
    public ComboBox<String> doctorSpe;
    @FXML
    public ComboBox<Departement> doctorDepartment;
    @FXML
    public Button buttonSave;
    private Integer valueDepartment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formCheckError.setStyle("-fx-text-fill: red;");

        ObservableList<Medecin> lesMedecins = APIAccess.getAllMedecins();
        ObservableList<Departement> lesDepartements = APIAccess.getAllDepartements();

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add("");
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null) {
                uniqueData.add(m.getSpe());
            }
        }
        ObservableList<Departement> departements = FXCollections.observableArrayList();
        for (Departement de : lesDepartements) {
            departements.addAll(new Departement(de.getId(), de.getNom()));
        }
        doctorSpe.getItems().addAll(uniqueData);
        doctorDepartment.getItems().addAll(departements);
        doctorDepartment.setConverter(new StringConverter<Departement>() {
            @Override
            public String toString(Departement object) {
                return object.getNom();
            }

            @Override
            public Departement fromString(String string) {
                return doctorDepartment.getItems().stream().filter(ap -> ap.getNom().equals(string)).findFirst().orElse(null);
            }
        });
        doctorDepartment.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                this.valueDepartment = newval.getId();
            }
        });

        doctorID.setText("Ajout d'un médecin");
        buttonSave.setOnAction(event -> {
            if (doctorLastName.getText().isEmpty() || doctorName.getText().isEmpty() || doctorAddress.getText().isEmpty()
                    || doctorPhone.getText().isEmpty() || doctorSpe.getValue() == null || doctorDepartment.getValue() == null) {
                formCheckError.setText("✕ Veuillez remplir tous les champs !");
            } else {
                // doctorLastName, doctorName, doctorSpe only contains letters a-zA-Z max length 40 no numbers and doctorAdresses can contains numbers and letters max 50 caracters - gh copilot
                if (doctorLastName.getText().matches("[a-zA-Z]{1,20}") && doctorName.getText().matches("[a-zA-Z]{1,20}")
                        && doctorSpe.getValue().matches("[a-zA-Z]{1,40}")
                        && doctorAddress.getText().matches("[a-zA-Z0-9]{1,50}")
                        && doctorPhone.getText().matches("[0-9]{1,10}")) {
                    Pane doctorAdded = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctors.fxml"));
                    try {
                        doctorAdded = loader.load();
                        doctorAdded.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
                    stage.setTitle("GSB - Liste des médecins");
                    stage.changeScene(doctorAdded);

                    // Ajout de médecin
                    APIAccess.addMedecin(doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, this.valueDepartment);
                    MedecinController medecinController = loader.getController();
                    medecinController.reload();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur lors de l'ajout d'un médecin");
                    alert.setHeaderText("Veuillez vérifier les champs");
                    Label label = new Label("✕ Le nom et le prénom ne doivent contenir que des lettres et doit être compris entre 3 et 20 caractères");
                    label.setStyle("-fx-text-fill: red;-fx-font-family: 'Roboto Light';-fx-font-size: 15px;");
                    Label labelAddress = new Label("✕ L'adresse doit être composé de chiffres et de lettres formar (Adresse, VilLe, Code Postal) et doivent être compris entre 10 et 50 caractères");
                    labelAddress.setStyle("-fx-text-fill: red;-fx-font-family: 'Roboto Light';-fx-font-size: 15px;");
                    Label labelPhone = new Label("✕ Le numéro de téléphone doit être composé de chiffres et doit être compris entre 10 et 10 caractères");
                    labelPhone.setStyle("-fx-text-fill: red;-fx-font-family: 'Roboto Light';-fx-font-size: 15px;");
                    VBox vbox = new VBox(label, labelAddress, labelPhone);
                    alert.getDialogPane().setContent(vbox);
                    alert.setContentText(vbox.getChildren().toString());
                    Stage stage;
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
                    alert.showAndWait();
                }
            }
        });
    }
}
