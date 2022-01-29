package com.entri.pembayaranair.activity;

public class StatusData {

    private String blok;
    private String imageUrl;
    private String periode;

    public StatusData() {
    }

    public StatusData(String blok, String imageUrl, String periode){
        this.blok = blok;
        this.imageUrl = imageUrl;
        this.periode = periode;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
