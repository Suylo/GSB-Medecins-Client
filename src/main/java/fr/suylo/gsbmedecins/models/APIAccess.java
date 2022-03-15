package fr.suylo.gsbmedecins.models;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class APIAccess {

    // GET

    // GET * Medecins
    public static ObservableList<Medecin> getAllMedecins(){
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin[] medecinsJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

        ObservableList<Medecin> data = FXCollections.observableArrayList();
        for (Medecin medecin : medecinsJson) {
            data.addAll(new Medecin(
                            medecin.getId(),
                            medecin.getNom(),
                            medecin.getPrenom(),
                            medecin.getAdresse(),
                            medecin.getTel(),
                            medecin.getSpe()
                    )
            );
        }
        return data;
    }

    // GET User infos By ID
    public static User getUserInfoByID(String userID) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/users/login?login=" + UserSession.getUserID()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), User.class);
    }

    // GET * Pays
    public static ObservableList<Pays> getAllPays() {
        HttpResponse<JsonNode> apiResponsePays = null;
        try {
            apiResponsePays = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] lesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponsePays).getBody().toString()), Pays[].class);

        ObservableList<Pays> data = FXCollections.observableArrayList();
        for (Pays unPays : lesPays){
            data.addAll(new Pays(
                unPays.getId(),
                    unPays.getNom(),
                    unPays.getDepartements()
            ));
        }
        System.out.println("DATA APIAcces :: Pays :" + data);
        return data;
    }

    // GET Pays Nom
    public static ObservableList<String> getAllPaysNom() {
        HttpResponse<JsonNode> apiResponsePays = null;
        try {
            apiResponsePays = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] lesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponsePays).getBody().toString()), Pays[].class);

        ObservableList<String> data = FXCollections.observableArrayList();
        for (Pays p : lesPays) {
            data.add(p.getNom());
        }
        return data;
    }

    // GET * Departements
    public static ObservableList<Departement> getAllDepartements() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/departements").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération au format Json de tous les médecins
        Departement[] departementJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

        // Création d'une collection pour les passer en objet
        ObservableList<Departement> data = FXCollections.observableArrayList();
        for (Departement departement : departementJson) {
            data.addAll(
                    new Departement(
                            departement.getId(),
                            departement.getNom(),
                            departement.getMedecins()
                    )
            );
        }
        return data;
    }
}
