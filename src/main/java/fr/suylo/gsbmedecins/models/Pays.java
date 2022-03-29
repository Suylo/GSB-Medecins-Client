package fr.suylo.gsbmedecins.models;

import java.util.List;

public class Pays {

    private Long id;
    private String nom;
    private List<Departement> departements;

    public Pays(Long id, String nom, List<Departement> departements) {
        this.id = id;
        this.nom = nom;
        this.departements = departements;
    }

    public Pays(String nom) {
        this.nom = nom;
    }

    public Pays(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Departement> getDepartements() {
        return departements;
    }

    public void setDepartements(List<Departement> departements) {
        this.departements = departements;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", departements=" + departements +
                '}';
    }
}
