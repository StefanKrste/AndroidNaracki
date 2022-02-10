package com.example.apnarackirestoran;

public class Objekt {

        private Integer Sifra;
        private String NazivNaobjekt;
        private Integer NegativnaKol;
        private Integer BrojMasi;
        private String PovrzanMag;

        public Objekt(){

        }

        public Objekt(int Sifra,String Naziv,int Negativna,int brojMasi, String povrzanMag){
            this.Sifra=Sifra;
            this.NazivNaobjekt = Naziv;
            this.NegativnaKol = Negativna;
            this.BrojMasi=brojMasi;
            this.PovrzanMag=povrzanMag;
        }

    public Integer getSifra() {
        return Sifra;
    }

    public void setSifra(Integer sifra) {
        Sifra = sifra;
    }

    public String getNazivNaobjekt() {
        return NazivNaobjekt;
    }

    public void setNazivNaobjekt(String nazivNaobjekt) {
        NazivNaobjekt = nazivNaobjekt;
    }

    public Integer getNegativnaKol() {
        return NegativnaKol;
    }

    public void setNegativnaKol(Integer negativnaKol) {
        NegativnaKol = negativnaKol;
    }

    public Integer getBrojMasi() {
        return BrojMasi;
    }

    public void setBrojMasi(Integer brojMasi) {
        BrojMasi = brojMasi;
    }

    public String getPovrzanMag() {
        return PovrzanMag;
    }

    public void setPovrzanMag(String povrzanMag) {
        PovrzanMag = povrzanMag;
    }
}
