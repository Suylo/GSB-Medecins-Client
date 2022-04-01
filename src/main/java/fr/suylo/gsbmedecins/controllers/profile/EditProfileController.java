package fr.suylo.gsbmedecins.controllers.profile;

import fr.suylo.gsbmedecins.controllers.MedecinController;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Medecin;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;

public class EditProfileController {

    private final String speIsNull = "// Aucune spécialité complémentaire.";
    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public ComboBox<String> doctorSpe;
    @FXML
    public ComboBox<Departement> doctorDepartment;
    @FXML
    public Button buttonSave;
    private Integer id;
    private Integer valueDepartment;

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

        ObservableList<Departement> lesDepartements = APIAccess.getAllDepartements();
        ObservableList<Medecin> lesMedecins = APIAccess.getAllMedecins();
        Medecin unMedecin = APIAccess.getMedecinByID(getId());

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add(this.speIsNull);
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null) {
                uniqueData.add(m.getSpe());
            }
        }

        ObservableList<Departement> departements = FXCollections.observableArrayList();
        for (Departement de : lesDepartements) {
            departements.addAll(new Departement(de.getId(), de.getNom()));
        }

        doctorID.setText("Modification du profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorSpe.getItems().addAll(uniqueData);
        doctorDepartment.setValue(unMedecin.getDepartement());
        if (unMedecin.getSpe() == null) {
            doctorSpe.setValue(this.speIsNull);
        } else {
            doctorSpe.setValue(unMedecin.getSpe());
        }
        doctorDepartment.getItems().addAll(departements);
        doctorDepartment.setConverter(new StringConverter<>() {
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

        buttonSave.setOnAction(event -> {
            if (doctorLastName.getText().isEmpty() || doctorName.getText().isEmpty() || doctorAddress.getText().isEmpty()
                    || doctorPhone.getText().isEmpty() || doctorSpe.getValue() == null || doctorDepartment.getValue() == null) {
                Label error = new Label("❌ Tous les champs sont obligatoires !");
                error.setStyle("-fx-text-fill: red;-fx-font-family: 'Roboto Light';-fx-font-size: 17px;");
                VBox vBox = new VBox(error);

                UserSession.errorAlert("Erreur", "Erreur lors de l'ajout d'un médecin ❓", vBox);
            } else {
                // doctorLastName, doctorName, doctorSpe only contains letters a-zA-Z max length 40 no numbers and doctorAdresses can contains numbers and letters max 50 caracters - gh copilot
                if (doctorLastName.getText().matches("[a-zA-Z\\s]{2,20}")
                        && doctorName.getText().matches("[a-zA-Z\\s]{2,20}")
                        && doctorAddress.getText().matches("^[a-zA-Z0-9\\sàâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ,]{10,50}$")
                        && doctorPhone.getText().matches("^[0-9+]{1,14}")) {

                    Pane doctorEditedNewList = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctors.fxml"));
                    try {
                        doctorEditedNewList = loader.load();
                        doctorEditedNewList.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
                    stage.setTitle("GSB - Liste des médecins");
                    stage.changeScene(doctorEditedNewList);
                    if (doctorSpe.getValue().equals(this.speIsNull)) {
                        doctorSpe.setValue(null);
                    }

                    // Récupération de l'id si le département n'a pas été modifié - a améliorer
                    ObservableList<Integer> valueID = APIAccess.getDepartementByNom(doctorDepartment.getValue().getNom());

                    // Update d'un médecin
                    APIAccess.updateMedecin(unMedecin.getId(), doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, valueID.get(0));

                    MedecinController medecinController = loader.getController();
                    medecinController.reload();
                }
                else {
                    Label label = new Label("✕ Le nom et le prénom ne doivent contenir que des lettres. (Min. 3 caractères ; Max. 20)"
                            + "\n✕ L'adresse doit être composé de chiffres et de lettres au format (Adresse, VilLe, Code Postal) (Min. 10 caractères ; Max. 50)"
                            + "\n✕ Le numéro de téléphone doit être uniquement composé de chiffres. (Ne pas indiquer le code du pays)");
                    label.setStyle("-fx-text-fill: red;-fx-font-family: 'Roboto Light';-fx-font-size: 17px;");
                    VBox vbox = new VBox(label);

                    UserSession.errorAlert("Errreur lors de l'ajout d'un médecin", "Veuillez vérifier les champs!", vbox);
                }
            }
        });
    }

}
