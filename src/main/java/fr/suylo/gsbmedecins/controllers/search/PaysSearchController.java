package fr.suylo.gsbmedecins.controllers.search;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.CRUDController;
import fr.suylo.gsbmedecins.controllers.profile.ProfileController;
import fr.suylo.gsbmedecins.models.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaysSearchController implements Initializable {


    public TableView<Medecin> myTable;
    @FXML
    public Button searchEnter;
    @FXML
    public TableColumn<Medecin, Integer> id;
    @FXML
    public TableColumn<Medecin, String> nomCol, prenomCol, spe;
    @FXML
    public TableColumn<Medecin, Medecin> action = new TableColumn<>("Action");
    @FXML
    public TextField textField;
    @FXML
    public Button searchByCountry, searchByDepartment, searchBySpeciality, searchByFLName;

    @FXML
    public ComboBox<Pays> selectCountries;
    private String listCountries;
    private Long valueCountry;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCountries();
        searchCountries();
        searchByDepartment.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/departement-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByDepartment.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par département");
            stage.changeScene(searchStage);
        });
        searchBySpeciality.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/specialite-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchBySpeciality.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par pays");
            stage.changeScene(searchStage);
        });
        searchByFLName.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/nomprenom-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByFLName.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin");
            stage.changeScene(searchStage);
        });
    }

    private void loadCountries() {
        ObservableList<Pays> lesMedecinsDunPays = APIAccess.getAllPays();

        for (Pays unPays : lesMedecinsDunPays) {
            selectCountries.getItems().add(unPays);
        }
        selectCountries.setConverter(new StringConverter<Pays>() {
            @Override
            public String toString(Pays object) {
                return object.getNom();
            }

            @Override
            public Pays fromString(String string) {
                return selectCountries.getItems().stream().filter(ap -> ap.getNom().equals(string)).findFirst().orElse(null);
            }
        });
        selectCountries.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                this.valueCountry = newval.getId();
            }
        });
    }

    private void searchCountries() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        spe.setCellValueFactory(new PropertyValueFactory<>("spe"));
        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            final SVGPath svgEdit = new SVGPath();
            final SVGPath svgSee = new SVGPath();
            final SVGPath svgRemove = new SVGPath();
            private final Button seeButton = new Button();
            private final Button editButton = new Button();
            private final Button removeButton = new Button();
            private final HBox listButtons = new HBox(seeButton, editButton, removeButton);

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

                CRUDController.onRead(seeButton, id.getCellData(getTableRow().getIndex()));
                CRUDController.onEdit(editButton, id.getCellData(getTableRow().getIndex()));
                CRUDController.onDelete(removeButton, myTable, id.getCellData(getTableRow().getIndex()));
            }
        });

        searchEnter.setOnAction(event -> {
            if (selectCountries.getValue() == null){
                myTable.setPlaceholder(new Label("Veuillez choisir un pays avant de débuter la recherche"));
            } else {
                Medecin[] medecins = APIAccess.getMedecinsByPays(this.valueCountry);

                myTable.getItems().clear();
                myTable.getItems().addAll(medecins);

                if (medecins.length == 0){
                    myTable.setPlaceholder(new Label("Aucun médecin trouvé pour ce pays"));
                }
            }
        });
        myTable.setPlaceholder(new Label("Veuillez commencer votre recherche !"));
    }

}
