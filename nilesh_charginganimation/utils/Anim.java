package com.si_charginganimation.nilesh_charginganimation.utils;

public class Anim {
    public int custom = -1;
    public int jsonRes;
    String uri;

    public Anim(int i, String uri2) {
        this.custom = i;
        this.uri = uri2;
    }

    public Anim(int i) {
        this.jsonRes = i;
    }

    public int getJsonRes() {
        return this.jsonRes;
    }

    public void setJsonRes(int i) {
        this.jsonRes = i;
    }

    public int getCustom() {
        return this.custom;
    }

    public String getUri() {
        return this.uri;
    }
}
