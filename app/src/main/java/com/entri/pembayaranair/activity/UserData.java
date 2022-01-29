package com.entri.pembayaranair.activity;

public class UserData {
    private String blok;
    private String name;
    private String username;


    public UserData() {
    }

    public UserData(String blok, String name, String username) {
        this.blok = blok;
        this.name = name;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}