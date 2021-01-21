package com.example.mandiri.Models;

public class ModelUpdateMaintenance {

    String uid, pId, uName, noText, noView, tanggalText, tindakanText, tindakanView, ketText, ketView;
    Long tanggalView;

    public ModelUpdateMaintenance() {
    }

    public ModelUpdateMaintenance(String uid, String pId, String uName, String noText, String noView, String tanggalText, String tindakanText, String tindakanView, String ketText, String ketView, Long tanggalView) {
        this.uid = uid;
        this.pId = pId;
        this.uName = uName;
        this.noText = noText;
        this.noView = noView;
        this.tanggalText = tanggalText;
        this.tindakanText = tindakanText;
        this.tindakanView = tindakanView;
        this.ketText = ketText;
        this.ketView = ketView;
        this.tanggalView = tanggalView;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getNoText() {
        return noText;
    }

    public void setNoText(String noText) {
        this.noText = noText;
    }

    public String getNoView() {
        return noView;
    }

    public void setNoView(String noView) {
        this.noView = noView;
    }

    public String getTanggalText() {
        return tanggalText;
    }

    public void setTanggalText(String tanggalText) {
        this.tanggalText = tanggalText;
    }

    public String getTindakanText() {
        return tindakanText;
    }

    public void setTindakanText(String tindakanText) {
        this.tindakanText = tindakanText;
    }

    public String getTindakanView() {
        return tindakanView;
    }

    public void setTindakanView(String tindakanView) {
        this.tindakanView = tindakanView;
    }

    public String getKetText() {
        return ketText;
    }

    public void setKetText(String ketText) {
        this.ketText = ketText;
    }

    public String getKetView() {
        return ketView;
    }

    public void setKetView(String ketView) {
        this.ketView = ketView;
    }

    public Long getTanggalView() {
        return tanggalView;
    }

    public void setTanggalView(Long tanggalView) {
        this.tanggalView = tanggalView;
    }
}
