package com.example.i043113.myapplication;

/**
 * Created by I024106 on 25-Jan-16.
 */
public class AP {
    String mac;
    String ssid;
    int x;
    int y;

    AP(String mac, int x, int y) {
        this.mac = mac;
        this.x = x;
        this.y = y;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return mac;
    }
}
