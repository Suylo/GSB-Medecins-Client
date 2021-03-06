package fr.suylo.gsbmedecins.models;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Objects;

public class APIAccess {

    // GET

    // GET * Medecins
    public static ObservableList<Medecin> getAllMedecins() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin[] medecinsJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

        ObservableList<Medecin> data = FXCollections.observableArrayList();
        for (Medecin medecin : medecinsJson) {
            data.addAll(
                    new Medecin(
                            medecin.getId(),
                            medecin.getNom(),
                            medecin.getPrenom(),
                            medecin.getAdresse(),
                            medecin.getTel(),
                            medecin.getSpe(),
                            new Departement(
                                    medecin.getDepartement().getId(),
                                    medecin.getDepartement().getNom()
                            )
                    )
            );
        }
        return data;
    }

    public static Medecin getMedecinByID(Integer id) {
        HttpResponse<JsonNode> apiResponseMedecins = null;
        try {
            apiResponseMedecins = Unirest.get("http://localhost:8080/api/v1/medecins/" + id.toString()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseMedecins).getBody().toString()), Medecin.class);
    }

    // GET Pays By Id
    public static Pays getPaysById(Integer id) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/pays/" + id.toString()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Pays.class);
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

    // GET Departement By Nom
    public static ObservableList<Integer> getDepartementByNom(String nom) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/departements/nom?nom=" + nom).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Departement[] lesDepartements = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

        ObservableList<Integer> departementObservableList = FXCollections.observableArrayList();
        for (Departement departement : lesDepartements) {
            departementObservableList.add(departement.getId());
        }
        return departementObservableList;
    }

    // GET * Medecins by Department
    public static Medecin[] getMedecinsByDepartementID(Integer id) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/departements/" + id + "/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);
    }

    // GET * Medecins By Pays
    public static Medecin[] getMedecinsByPays(Long id) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/pays/" + id + "/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);
    }

    // GET Pays By Nom
    public static ObservableList<Integer> getPaysByNom(String nom) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/pays/nom?nom=" + nom).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] tousLesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Pays[].class);

        ObservableList<Integer> data = FXCollections.observableArrayList();
        for (Pays pays : tousLesPays) {
            data.add(Math.toIntExact(pays.getId()));
        }
        return data;
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
        for (Pays unPays : lesPays) {
            data.addAll(new Pays(
                    unPays.getId(),
                    unPays.getNom(),
                    unPays.getDepartements()
            ));
        }
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
        // R??cup??ration au format Json de tous les m??decins
        Departement[] departementJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

        // Cr??ation d'une collection pour les passer en objet
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


    // POST

    // POST Add Medecin
    public static void addMedecin(TextField doctorLastName, TextField doctorName, TextField doctorAddress,
                                  TextField doctorPhone, ComboBox<String> doctorSpe, Integer doctorDepartment) {
        Medecin newMedecin = new Medecin(
                doctorLastName.getText(),
                doctorName.getText(),
                doctorAddress.getText(),
                doctorPhone.getText(),
                doctorSpe.getValue(),
                new Departement(
                        doctorDepartment
                )
        );
        try {
            Unirest.post("http://localhost:8080/api/v1/medecins/")
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(newMedecin)).asJson();
            System.out.println("M??decin bien ajout?? :: " + new Gson().toJson(newMedecin));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    // POST Add Pays
    public static void addPays(TextField countryName) {
        Pays newPays = new Pays(countryName.getText());
        try {
            Unirest.post("http://localhost:8080/api/v1/pays/create")
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(newPays)).asJson();
            System.out.println("Pays bien ajout?? :: " + new Gson().toJson(newPays));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    // POST Add Departement
    public static void addDepartement(String departmentName, ComboBox<Pays> countrySelect) {
        Departement newDepartement =
                new Departement(
                        departmentName,
                        new Pays(
                                countrySelect.getValue().getId()
                        )
                );

        try {
            Unirest.post("http://localhost:8080/api/v1/departements/create")
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(newDepartement)).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    // UDATE
    // UPDATE Medecin
    public static void updateMedecin(Integer id, TextField doctorLastName, TextField doctorName, TextField doctorAddress,
                                     TextField doctorPhone, ComboBox<String> doctorSpe, Integer departmentValue) {
        Medecin updatedMedecin = new Medecin(
                doctorLastName.getText(),
                doctorName.getText(),
                doctorAddress.getText(),
                doctorPhone.getText(),
                doctorSpe.getValue(),
                new Departement(
                        departmentValue
                )
        );
        try {
            Unirest.put("http://localhost:8080/api/v1/medecins/edit/" + id)
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(updatedMedecin)).asJson();
            System.out.println("M??decin mis ?? jour :: " + new Gson().toJson(updatedMedecin));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    // DELETE

    // DELETE Medecin By ID
    public static void deleteMedecinByID(Integer cellData) throws UnirestException {
        Unirest.delete("http://localhost:8080/api/v1/medecins/delete/" + cellData).asJson();
    }

    // DELETE Departement By ID (Cascade)
    public static void deleteDepartementByID(Integer cellData) throws UnirestException {
        Unirest.delete("http://localhost:8080/api/v1/departements/delete/" + cellData).asJson();
    }

    public static Departement getDepartementById(Integer id) {
        HttpResponse<JsonNode> apiResponseDepartements = null;
        try {
            apiResponseDepartements = Unirest.get("http://localhost:8080/api/v1/departements/" + id.toString()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseDepartements).getBody().toString()), Departement.class);
    }


    public static void updateDepartement(Integer id, String departmentName, Long countryValue) {
        Departement updateDepartement = new Departement(
                departmentName,
                new Pays(
                        countryValue
                )
        );
        try {
            Unirest.put("http://localhost:8080/api/v1/departements/put/" + id)
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(updateDepartement)).asJson();
            System.out.println("D??partement mis ?? jour :: " + new Gson().toJson(updateDepartement));
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    public static void updatePays(Integer id, String countryName) {
        Pays updatePays = new Pays(
                countryName
        );
        try {
            Unirest.put("http://localhost:8080/api/v1/pays/put/" + id)
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(updatePays)).asJson();
            System.out.println("Pays mis ?? jour :: " + new Gson().toJson(updatePays));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
