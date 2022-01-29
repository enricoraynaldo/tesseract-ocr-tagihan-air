package com.entri.pembayaranair.activity;

public class UpdatePHelperClass {
    String blok, name, mawal, makhir, totalm, tarifkub, badmin, tagihan, pot, totalbiaya, periodeterakhir, status;

    public UpdatePHelperClass() {

    }

    public UpdatePHelperClass(String blok, String name, String mawal, String makhir, String totalm, String tarifkub, String badmin, String tagihan, String pot, String totalbiaya, String periodeterakhir, String status) {
        this.blok = blok;
        this.name = name;
        this.mawal = mawal;
        this.makhir = makhir;
        this.totalm = totalm;
        this.tarifkub = tarifkub;
        this.badmin = badmin;
        this.tagihan = tagihan;
        this.pot = pot;
        this.totalbiaya = totalbiaya;
        this.periodeterakhir = periodeterakhir;
        this.status = status;
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

    public String getTotalm() {
        return totalm;
    }

    public void setTotalm(String totalm) {
        this.totalm = totalm;
    }

    public String getTarifkub() {
        return tarifkub;
    }

    public void setTarifkub(String tarifkub) {
        this.tarifkub = tarifkub;
    }

    public String getBadmin() {
        return badmin;
    }

    public void setBadmin(String badmin) {
        this.badmin = badmin;
    }

    public String getTagihan() {
        return tagihan;
    }

    public void setTagihan(String tagihan) {
        this.tagihan = tagihan;
    }

    public String getPot() {
        return pot;
    }

    public void setPot(String pot) {
        this.pot = pot;
    }

    public String getTotalbiaya() {
        return totalbiaya;
    }

    public void setTotalbiaya(String totalbiaya) {
        this.totalbiaya = totalbiaya;
    }

    public String getPeriodeterakhir() {
        return periodeterakhir;
    }

    public void setPeriodeterakhir(String periodeterakhir) {
        this.periodeterakhir = periodeterakhir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
