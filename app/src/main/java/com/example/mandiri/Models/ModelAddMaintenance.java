package com.example.mandiri.Models;

public class ModelAddMaintenance {

    String jenisEdit, merkEdit, lokasiEdit, inventarisEdit, dateedit, uid,uEmail,uName,pId;

    public ModelAddMaintenance() {
    }

    public ModelAddMaintenance(String jenisEdit, String merkEdit, String lokasiEdit, String inventarisEdit, String dateedit, String uid, String uEmail, String uName, String pId) {
        this.jenisEdit = jenisEdit;
        this.merkEdit = merkEdit;
        this.lokasiEdit = lokasiEdit;
        this.inventarisEdit = inventarisEdit;
        this.dateedit = dateedit;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uName = uName;
        this.pId = pId;
    }

    public String getJenisEdit() {
        return jenisEdit;
    }

    public void setJenisEdit(String jenisEdit) {
        this.jenisEdit = jenisEdit;
    }

    public String getMerkEdit() {
        return merkEdit;
    }

    public void setMerkEdit(String merkEdit) {
        this.merkEdit = merkEdit;
    }

    public String getLokasiEdit() {
        return lokasiEdit;
    }

    public void setLokasiEdit(String lokasiEdit) {
        this.lokasiEdit = lokasiEdit;
    }

    public String getInventarisEdit() {
        return inventarisEdit;
    }

    public void setInventarisEdit(String inventarisEdit) {
        this.inventarisEdit = inventarisEdit;
    }

    public String getDateedit() {
        return dateedit;
    }

    public void setDateedit(String dateedit) {
        this.dateedit = dateedit;
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
