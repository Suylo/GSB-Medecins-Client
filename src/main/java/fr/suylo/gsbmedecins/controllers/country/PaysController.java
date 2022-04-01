package fr.suylo.gsbmedecins.controllers.country;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Pays;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class PaysController implements Initializable {

    public final Button addPays = new Button("Ajouter un pays");
    @FXML
    public TableView<Pays> tableView;
    @FXML
    public TableColumn<Pays, String> nom;
    @FXML
    public TableColumn<Pays, Long> id;
    @FXML
    public TableColumn<Pays, Pays> action = new TableColumn<>("Action");
    public Label titlePays, titleBlank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nom.setText(" ");
        action.setVisible(false);
        id.setVisible(false);
        addPays.getStyleClass().add("button-see");
        if (UserSession.getUserLoggedOn()) {
            titleBlank.setGraphic(addPays);
            addPays.setAlignment(Pos.BOTTOM_CENTER);
            addPays.setOnAction(event -> {
                Pane addCountry = null;
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addPays.fxml"));
                try {
                    addCountry = loader.load();
                    addCountry.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CustomStage stage = ((CustomStage) addPays.getScene().getWindow());
                stage.setTitle("GSB - Ajout d'un pays");
                stage.changeScene(addCountry);
            });
            action.setVisible(true);
            nom.setText("Nom");
        }

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            public final Button editButton = new Button();
            public final Button removeButton = new Button();
            public final HBox listButtons = new HBox(editButton, removeButton);
            final SVGPath svgEdit = new SVGPath();
            final SVGPath svgRemove = new SVGPath();

            protected void updateItem(Pays item, boolean empty) {
                super.updateItem(item, empty);

                svgEdit.setContent("M12 21h9c0.552 0 1-0.448 1-1s-0.448-1-1-1h-9c-0.552 0-1 0.448-1 1s0.448 1 1 1zM15.793 2.793l-12.5 12.5c-0.122 0.121-0.217 0.28-0.263 0.465l-1 4c-0.039 0.15-0.042 0.318 0 0.485 0.134 0.536 0.677 0.862 1.213 0.728l4-1c0.167-0.041 0.33-0.129 0.465-0.263l12.5-12.5c0.609-0.609 0.914-1.41 0.914-2.207s-0.305-1.598-0.914-2.207-1.411-0.915-2.208-0.915-1.598 0.305-2.207 0.914zM17.207 4.207c0.219-0.219 0.504-0.328 0.793-0.328s0.574 0.109 0.793 0.328 0.328 0.504 0.328 0.793-0.109 0.574-0.328 0.793l-12.304 12.304-2.115 0.529 0.529-2.115z");
                svgEdit.setFill(Color.WHITE);
                svgRemove.setContent("M18 7v13c0 0.276-0.111 0.525-0.293 0.707s-0.431 0.293-0.707 0.293h-10c-0.276 0-0.525-0.111-0.707-0.293s-0.293-0.431-0.293-0.707v-13zM17 5v-1c0-0.828-0.337-1.58-0.879-2.121s-1.293-0.879-2.121-0.879h-4c-0.828 0-1.58 0.337-2.121 0.879s-0.879 1.293-0.879 2.121v1h-4c-0.552 0-1 0.448-1 1s0.448 1 1 1h1v13c0 0.828 0.337 1.58 0.879 2.121s1.293 0.879 2.121 0.879h10c0.828 0 1.58-0.337 2.121-0.879s0.879-1.293 0.879-2.121v-13h1c0.552 0 1-0.448 1-1s-0.448-1-1-1zM9 5v-1c0-0.276 0.111-0.525 0.293-0.707s0.431-0.293 0.707-0.293h4c0.276 0 0.525 0.111 0.707 0.293s0.293 0.431 0.293 0.707v1zM9 11v6c0 0.552 0.448 1 1 1s1-0.448 1-1v-6c0-0.552-0.448-1-1-1s-1 0.448-1 1zM13 11v6c0 0.552 0.448 1 1 1s1-0.448 1-1v-6c0-0.552-0.448-1-1-1s-1 0.448-1 1z");
                svgRemove.setFill(Color.WHITE);

                editButton.setGraphic(svgEdit);
                removeButton.setGraphic(svgRemove);

                if (UserSession.getUserLoggedOn()) {
                    listButtons.setAlignment(Pos.CENTER);
                    setGraphic(empty ? null : listButtons);
                }

                editButton.getStyleClass().add("button-edit");
                removeButton.getStyleClass().add("button-remove");
                editButton.setOnAction(event -> {
                    Pane editCountryView = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("editPays.fxml"));
                    try {
                        editCountryView = loader.load();
                        editCountryView.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EditPaysController editPaysController = loader.getController();
                    editPaysController.loadData(Math.toIntExact(id.getCellData(getTableRow().getIndex())));
                    editPaysController.loadEditPays();
                    CustomStage stage = ((CustomStage) editButton.getScene().getWindow());
                    stage.setTitle("GSB - Profil d'un médecin ");
                    stage.changeScene(editCountryView);
                });
                removeButton.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppresion d'un pays");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le pays " + nom.getCellData(getTableRow().getIndex()) + " ?");

                    Label label = new Label("⚠ ATTENTION : Supprimer un pays supprimera tous les départements \n de celui-ci ainsi que tous ses médecins !\n- Cette action est irréversible. \n");
                    label.setStyle("-fx-text-fill: red;-fx-font-size: 17px;-fx-font-family: 'Calibri Light', sans-serif;");

                    alert.getDialogPane().setContent(label);
                    alert.getDialogPane().getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-background-color:#202940 ;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
                    alert.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("-fx-background-color: #202940;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
                    Stage stage;
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        try {
                            Unirest.delete("http://localhost:8080/api/v1/pays/delete/" + id.getCellData(getTableRow().getIndex())).asJson();
                            System.out.println("Pays numero : supprimé :: " + id.getCellData(getTableRow().getIndex()));
                            tableView.getItems().clear();
                            tableView.getItems().addAll(
                                    APIAccess.getAllPays()
                            );
                        } catch (UnirestException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
        reload();
    }

    public void reload() {
        tableView.getItems().clear();
        tableView.getItems().addAll(
                APIAccess.getAllPays()
        );
    }
}
