package com.project.cuasa.models;

public class data_transaksi {
    //deklarasi variabel
    public Integer nominalPemasukan;
    public String catatan;
    public String tanggal;
    public Integer nominalPengeluaran;

    private String key;


    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getNominalPemasukan() {
        return nominalPemasukan;
    }

    public void setNominalPemasukan(Integer nominalPemasukan) {
        this.nominalPemasukan = nominalPemasukan;
    }

    public Integer getNominalPengeluaran() {
        return nominalPengeluaran;
    }

    public void setNominalPengeluaran(Integer nominalPengeluaran) {
        this.nominalPengeluaran = nominalPengeluaran;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal= tanggal;
    }


    //membuat construcktor kosong untuk membaca data snapshot
    public data_transaksi() {
    }


    //konstruktor dengan beberapa parameter, untuk mendapatkan input data dari User
    public data_transaksi(Integer nominalPemasukan, Integer nominalPengeluaran, String catatan, String tanggal) {

        this.nominalPengeluaran = nominalPengeluaran;
        this.nominalPemasukan = nominalPemasukan;
        this.catatan = catatan;
        this.tanggal = tanggal;

    }



}


