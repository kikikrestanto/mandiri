package com.example.mandiri.Models;

public class ModelUpdateMaintenance {
    String keterangan, nomor, tanggal, tindakan, uEmail, uid;

    public ModelUpdateMaintenance() {
        // Empty constructor
    }

    public ModelUpdateMaintenance(String keterangan, String nomor, String tanggal, String tindakan, String uEmail, String uid) {
        this.keterangan = keterangan;
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.tindakan = tindakan;
        this.uEmail = uEmail;
        this.uid = uid;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTindakan() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        this.tindakan = tindakan;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
