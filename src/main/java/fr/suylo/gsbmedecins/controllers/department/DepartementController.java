package fr.suylo.gsbmedecins.controllers.department;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class DepartementController implements Initializable {

    public final Button addDepartement = new Button("Ajouter un département");
    @FXML
    public TableView<Departement> tableView;
    @FXML
    public TableColumn<Departement, Integer> id;
    @FXML
    public TableColumn<Departement, String> nom;
    @FXML
    public Label titleDepartements, titleBlank;
    @FXML
    public TableColumn<Departement, Departement> action = new TableColumn<>("Action");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDepartement.getStyleClass().add("button-see");
        nom.setText(" ");
        action.setVisible(false);
        id.setVisible(false);

        if (UserSession.getUserLoggedOn()) {
            titleBlank.setGraphic(addDepartement);
            addDepartement.setAlignment(Pos.BOTTOM_CENTER);
            addDepartement.setOnAction(event -> {
                Pane addDepartment = null;
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addDepartement.fxml"));
                try {
                    addDepartment = loader.load();
                    addDepartment.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CustomStage stage = ((CustomStage) addDepartement.getScene().getWindow());
                stage.setTitle("GSB - Ajout d'un département");
                stage.changeScene(addDepartment);
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

            protected void updateItem(Departement item, boolean empty) {
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
                    Pane editDepartmentView = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("editDepartement.fxml"));
                    try {
                        editDepartmentView = loader.load();
                        editDepartmentView.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EditDepartementController editDepartementController = loader.getController();
                    editDepartementController.loadData(id.getCellData(getTableRow().getIndex()));
                    editDepartementController.loadEditProfile();
                    CustomStage stage = ((CustomStage) editButton.getScene().getWindow());
                    stage.setTitle("GSB - Modification d'un département ");
                    stage.changeScene(editDepartmentView);
                });
                removeButton.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppresion d'un département");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le département " + nom.getCellData(getTableRow().getIndex()) + " ?");

                    Label label = new Label("⚠ ATTENTION : Supprimer un département supprimera tous les médecins \n contenus dans celui-ci. \n- Cette action est irréversible. \n");
                    label.setStyle("-fx-text-fill: red;-fx-font-size: 17px;-fx-font-family: 'Calibri Light', sans-serif;");

                    alert.getDialogPane().setContent(label);
                    alert.getDialogPane().getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    Stage stage;
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("fr/suylo/gsbmedecins/img/gsb.png"));
                    alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-background-color:#202940 ;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
                    alert.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("-fx-background-color: #202940;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-family: 'Calibri', sans-serif;");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        try {
                            APIAccess.deleteDepartementByID(id.getCellData(getTableRow().getIndex()));
                            System.out.println("Département numero : supprimé :: " + id.getCellData(getTableRow().getIndex()));
                            tableView.getItems().clear();
                            tableView.getItems().addAll(APIAccess.getAllDepartements());
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
        tableView.getItems().addAll(APIAccess.getAllDepartements());
    }
}
