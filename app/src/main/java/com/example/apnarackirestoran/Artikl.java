package com.example.apnarackirestoran;

public class Artikl {
    private int ID;
    private String SifraArtikl;
    private String NazivArtikl;
    private String EdinicaMerka;
    private String MaloprodaznaCena;
    private Integer DanocnaTarifa;
    private Integer MKProizvod;
    private Integer Kol;
    private Double MagacinskaCena;
    private boolean Selektirnao;

    public Artikl(){

    }

    public Artikl(int iD ,String sifraArtikl,String nazivArtikl, String edinicaMerka, String maloprodaznaCena, int danocnaTarifa, int mKProizvod, int kol, double magacinskaCena ,boolean selektirano) {
        this.ID = iD;
        this.SifraArtikl = sifraArtikl;
        this.NazivArtikl = nazivArtikl;
        this.EdinicaMerka = edinicaMerka;
        this.MaloprodaznaCena = maloprodaznaCena;
        this.DanocnaTarifa = danocnaTarifa;
        this.MKProizvod = mKProizvod;
        this.Kol = kol;
        this.MagacinskaCena = magacinskaCena;
        this.Selektirnao = selektirano;
    }

    public boolean isSelektirnao() {
        return Selektirnao;
    }

    public void setSelektirnao(boolean selektirnao) {
        Selektirnao = selektirnao;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSifraArtikl() {
        return SifraArtikl;
    }

    public void setSifraArtikl(String sifraArtikl) {
        SifraArtikl = sifraArtikl;
    }

    public String getNazivArtikl() {
        return NazivArtikl;
    }

    public void setNazivArtikl(String nazivArtikl) {
        NazivArtikl = nazivArtikl;
    }

    public String getEdinicaMerka() {
        return EdinicaMerka;
    }

    public void setEdinicaMerka(String edinicaMerka) {
        EdinicaMerka = edinicaMerka;
    }

    public String getMaloprodaznaCena() {
        return MaloprodaznaCena;
    }

    public void setMaloprodaznaCena(String maloprodaznaCena) {
        MaloprodaznaCena = maloprodaznaCena;
    }

    public Integer getDanocnaTarifa() {
        return DanocnaTarifa;
    }

    public void setDanocnaTarifa(Integer danocnaTarifa) {
        DanocnaTarifa = danocnaTarifa;
    }

    public Integer getMKProizvod() {
        return MKProizvod;
    }

    public void setMKProizvod(Integer MKProizvod) {
        this.MKProizvod = MKProizvod;
    }

    public Integer getKol() {
        return Kol;
    }

    public void setKol(Integer kol) {
        Kol = kol;
    }

    public Double getMagacinskaCena() {
        return MagacinskaCena;
    }

    public void setMagacinskaCena(Double magacinskaCena) {
        MagacinskaCena = magacinskaCena;
    }
}
