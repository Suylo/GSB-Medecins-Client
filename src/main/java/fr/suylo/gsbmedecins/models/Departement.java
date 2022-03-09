package fr.suylo.gsbmedecins.models;

import java.util.List;

public class Departement {

    private Integer id;
    private String nom;
    private List<Medecin> medecins;

    public Departement(Integer id, String nom, List<Medecin> medecins) {
        this.id = id;
        this.nom = nom;
        this.medecins = medecins;
    }

    public Departement(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Medecin> getMedecins() {
        return medecins;
    }

    public void setMedecins(List<Medecin> medecins) {
        this.medecins = medecins;
    }

    @Override
    public String toString() {
        return "Departement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", medecins=" + medecins +
                '}';
    }
}
