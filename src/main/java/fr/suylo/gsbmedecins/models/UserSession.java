package fr.suylo.gsbmedecins.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lk.vivoxalabs.customstage.CustomStage;
import lk.vivoxalabs.customstage.CustomStageBuilder;
import lk.vivoxalabs.customstage.tools.NavigationType;
import lk.vivoxalabs.customstage.tools.Style;

import java.io.IOException;
import java.util.Objects;

public class UserSession {

    private static UserSession instance;

    private static String userID;
    private static String userPassword;

    private UserSession(String id, String passwd) {
        UserSession.userID = id;
        UserSession.userPassword = passwd;
    }

    public static boolean getUserLoggedOn() {
        return !userID.isEmpty() || !userPassword.isEmpty();
    }

    public static String getUserID() {
        return userID;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static void loadUi(Double x, Double y) throws IOException {
        // Récupération de la page de base : home-admin
        Parent login = FXMLLoader.load(Objects.requireNonNull(UserSession.class.getClassLoader().getResource("home-admin.fxml")));
        VBox container = new VBox(login);
        StackPane rootPane = new StackPane();

        // Ajout de design sur la page
        rootPane.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
        rootPane.getChildren().add(container);

        // Création d'une navbar fix
        AnchorPane navigationPane = null;
        navigationPane = FXMLLoader.load(Objects.requireNonNull(UserSession.class.getClassLoader().getResource("dashboards/dashboard-admin.fxml")));
        navigationPane.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");

        // Construction de la fenêtre avec la lib CustomStage
        CustomStageBuilder builder = new CustomStageBuilder();
        builder.setIcon("fr/suylo/gsbmedecins/img/images.png");

        builder.setActionIcons(new Image("fr/suylo/gsbmedecins/img/close-white.png"),
                new Image("fr/suylo/gsbmedecins/img/minus-white.png"),
                new Image("fr/suylo/gsbmedecins/img/maximize-white.png"),
                new Image("fr/suylo/gsbmedecins/img/minimize-white.png"));
        builder.setNavigationPane(Style.DYNAMIC, NavigationType.LEFT, navigationPane, 0, 0, false);
        builder.setWindowColor("#0f3e52");
        builder.setButtonHoverColor("#42506AF4");
        builder.setDimensions(1350, 850, 1350, 850);
        CustomStage stage = builder.build();

        stage.show();
        // On a récupéré le X et Y en paramètre de l'ancienne scène, on les ajoutes
        stage.setX(x);
        stage.setY(y);
        stage.setResizable(false);
        stage.setMaxHeight(850);
        stage.setMaxWidth(1350);
        stage.setTitle("Galaxy Swiss Bourdin ! - Administration");

        // On charge la page de base
        stage.changeScene(rootPane);
    }

    public static UserSession getInstace(String userId, String userPasswd) {
        if (instance == null) {
            instance = new UserSession(userId, userPasswd);
        }
        return instance;
    }

    public static void cleanUserSession() {
        userID = null;
        userPassword = null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userID='" + userID + '\'' +
                "userPassword = '" + userPassword + '\'' +
                '}';
    }

}