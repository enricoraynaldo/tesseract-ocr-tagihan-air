package com.entri.pembayaranair.model;

public class TagihanModel {

    private int id_tagihan;
    private int totair;
    private int tt;
    private String noair;

    public TagihanModel(int id_tagihan, int totair, int tt, String noair) {
        this.id_tagihan = id_tagihan;
        this.totair = totair;
        this.tt = tt;
        this.noair = noair;
    }

    public int getId_tagihan() {
        return id_tagihan;
    }

    public void setId_tagihan(int id_tagihan) {
        this.id_tagihan = id_tagihan;
    }

    public int getTotair() {
        return totair;
    }

    public void setTotair(int totair) {
        this.totair = totair;
    }

    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public String getNoair() {
        return noair;
    }

    public void setNoair(String noair) {
        this.noair = noair;
    }
}