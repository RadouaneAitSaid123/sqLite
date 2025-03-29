package com.example.sqllitetp.entities;

public class Etudiant {

    private int id;
    private String nom;
    private String prenom;
    private String image;
    private String dateNaissance;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Etudiant() {
    }

    public Etudiant(String nom, String prenom, String image, String dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
        this.dateNaissance = dateNaissance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
