package com.example.mandiri.Models;

public class ModelUpdateMaintenance {

    String uid, uName,uEmail, noView,tindakanView, keteranganView;
    Long tanggalView;

    public ModelUpdateMaintenance() {
    }

    public ModelUpdateMaintenance(String uid,String uEmail,String uName, String noView, Long tanggalView, String tindakanView, String keteranganView) {
        this.uid = uid;
        this.uEmail = uEmail;
        this.uName = uName;
        this.noView = noView;
        this.tanggalView = tanggalView;
        this.tindakanView = tindakanView;
        this.keteranganView = keteranganView;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getNoView() {
        return noView;
    }

    public void setNoView(String noView) {
        this.noView = noView;
    }

    public Long getTanggalView() {
        return tanggalView;
    }

    public void setTanggalView(Long tanggalView) {
        this.tanggalView = tanggalView;
    }

    public String getTindakanView() {
        return tindakanView;
    }

    public void setTindakanView(String tindakanView) {
        this.tindakanView = tindakanView;
    }

    public String getKeteranganView() {
        return keteranganView;
    }

    public void setKeteranganView(String keteranganView) {
        this.keteranganView = keteranganView;
    }
}
