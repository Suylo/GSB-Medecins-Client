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
        navigationPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("dashboard-black-theme.fxml")));
        navigationPane.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

        // Construction de la fenêtre
        CustomStageBuilder builder = new CustomStageBuilder();
        builder.setIcon("fr/suylo/gsbmedecins/img/images.png");

        builder.setActionIcons(new Image("fr/suylo/gsbmedecins/img/close-white.png"),
                new Image("fr/suylo/gsbmedecins/img/minus-white.png"),
                new Image("fr/suylo/gsbmedecins/img/maximize-white.png"),
                new Image("fr/suylo/gsbmedecins/img/minimize-white.png"));
        builder.setNavigationPane(Style.DYNAMIC, NavigationType.LEFT, navigationPane, 0, 0, false);
        builder.setWindowColor("#202940");
        builder.setButtonHoverColor("#42506AF4");
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