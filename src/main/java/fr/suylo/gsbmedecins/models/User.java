package fr.suylo.gsbmedecins.models;


public class User {


    private Integer id;
    private String login;
    private String nom;
    private String prenom;
    private String motdepasse;
    private String adresse;
    private String embauche;

    public User(Integer id, String login, String nom, String prenom, String motdepasse, String adresse, String embauche) {
        this.id = id;
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.motdepasse = motdepasse;
        this.adresse = adresse;
        this.embauche = embauche;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmbauche() {
        return this.embauche;
    }

    public void setEmbauche(String embauche) {
        this.embauche = embauche;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", mdp='" + motdepasse + '\'' +
                ", adresse='" + adresse + '\'' +
                ", embauche='" + embauche + '\'' +
                '}';
    }
}
