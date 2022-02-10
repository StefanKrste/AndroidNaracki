package com.example.apnarackirestoran;

public class Akaunt {

    private String Username;
    private String Password;
    private String Mail;
    private String Ime;
    private String Prezime;
    private String Tel;
    private String IdFirma;
    private String PosledenObjekt;
    private String OdMasa;
    private String DoMasa;

    public Akaunt(){

    }

    public Akaunt(String username, String password, String mail, String ime, String prezime, String tel , String idFirma, String posledenObjekt,String odMasa,String doMasa) {
        this.Username = username;
        this.Password = password;
        this.Mail = mail;
        this.Ime = ime;
        this.Prezime = prezime;
        this.Tel = tel;
        this.IdFirma = idFirma;
        this.PosledenObjekt = posledenObjekt;
        this.OdMasa = odMasa;
        this.DoMasa = doMasa;
    }

    public String getOdMasa() {
        return OdMasa;
    }

    public void setOdMasa(String odMasa) {
        OdMasa = odMasa;
    }

    public String getDoMasa() {
        return DoMasa;
    }

    public void setDoMasa(String doMasa) {
        DoMasa = doMasa;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getIme() {
        return Ime;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public String getPrezime() {
        return Prezime;
    }

    public void setPrezime(String prezime) {
        Prezime = prezime;
    }

    public String getIdFirma() {
        return IdFirma;
    }

    public void setIdFirma(String idFirma) {
        IdFirma = idFirma;
    }

    public String getPosledenObjekt() {
        return PosledenObjekt;
    }

    public void setPosledenObjekt(String posledenObjekt) {
        PosledenObjekt = posledenObjekt;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}
