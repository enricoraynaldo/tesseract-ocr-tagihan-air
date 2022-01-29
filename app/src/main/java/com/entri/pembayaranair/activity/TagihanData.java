package com.entri.pembayaranair.activity;

public class TagihanData {
    private String blok;
    private String name;
    private String mawal;
    private String makhir;
    private String periode;
    private String total_tagihan;

    public TagihanData() {
    }

    public TagihanData(String blok, String name, String mawal, String makhir, String periode, String total_tagihan) {
        this.blok = blok;
        this.name = name;
        this.mawal = mawal;
        this.makhir = makhir;
        this.periode = periode;
        this.total_tagihan = total_tagihan;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMawal() {
        return mawal;
    }

    public void setMawal(String mawal) {
        this.mawal = mawal;
    }

    public String getMakhir() {
        return makhir;
    }

    public void setMakhir(String makhir) {
        this.makhir = makhir;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getTotal_tagihan() {
        return total_tagihan;
    }

    public void setTotal_tagihan(String total_tagihan) {
        this.total_tagihan = total_tagihan;
    }

}

    