package fr.suylo.gsbmedecins;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.vivoxalabs.customstage.CustomStage;
import lk.vivoxalabs.customstage.CustomStageBuilder;
import lk.vivoxalabs.customstage.tools.NavigationType;
import lk.vivoxalabs.customstage.tools.Style;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent login = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("home.fxml")));
        VBox container = new VBox(login);
        StackPane rootPane = new StackPane();
        rootPane.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
        rootPane.getChildren().add(container);

        AnchorPane navigationPane = null;
        navigationPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("dashboards/dashboard-black-theme.fxml")));
        navigationPane.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
        navigationPane.setEffect(null);

        // Construction de la fenêtre
        CustomStageBuilder builder = new CustomStageBuilder();
        builder.setIcon("fr/suylo/gsbmedecins/img/gsb.png");

        builder.setActionIcons(new Image("fr/suylo/gsbmedecins/img/icons/x.png"),
                new Image("fr/suylo/gsbmedecins/img/icons/minus.png"),
                new Image("fr/suylo/gsbmedecins/img/icons/maximize.png"),
                new Image("fr/suylo/gsbmedecins/img/icons/minimize.png"));
        builder.setNavigationPane(Style.DYNAMIC, NavigationType.LEFT, navigationPane, 0, 0, false);
        builder.setWindowColor("#202940");
        builder.setTitleColor("#ffffff");
        builder.setButtonHoverColor("#323a4f");
        builder.setDimensions(1350, 850, 1350, 850);
        CustomStage stage = builder.build();
        stage.show();
        stage.setResizable(false);
        stage.setMaxHeight(850);
        stage.setMaxWidth(1350);
        stage.setTitle("Galaxy Swiss Bourdin !");

        // Default Scene
        stage.changeScene(rootPane);
    }
}