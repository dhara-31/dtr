package com.test.testing12345.adsclass;

import android.text.Html;

/* loaded from: classes2.dex */
public class StylishFontCkModel {
    String[] cap;
    int fontIndexCode;
    String fontStyleName;
    boolean isSupportedForLower;
    String[] lower;
    String[] number;

    public StylishFontCkModel(String[] strArr, String[] strArr2, String[] strArr3, String str, int i) {
        this.cap = new String[26];
        this.lower = new String[26];
        this.number = new String[10];
        this.cap = strArr;
        this.lower = strArr2;
        this.number = strArr3;
        this.fontStyleName = str;
        this.fontIndexCode = i;
    }

    public boolean isSupportedForLower() {
        return this.isSupportedForLower;
    }

    public void setSupportedForLower(boolean z) {
        this.isSupportedForLower = z;
    }

    public String getFontStyleName() {
        return this.fontStyleName;
    }

    public void setFontStyleName(String str) {
        this.fontStyleName = str;
    }

    public int getFontIndexCode() {
        return this.fontIndexCode;
    }

    public void setFontIndexCode(int i) {
        this.fontIndexCode = i;
    }

    public String[] getCap() {
        return this.cap;
    }

    public void setCap(String[] strArr) {
        this.cap = strArr;
    }

    public String[] getLower() {
        return this.lower;
    }

    public void setLower(String[] strArr) {
        this.lower = strArr;
    }

    public String[] getNumber() {
        return this.number;
    }

    public void setNumber(String[] strArr) {
        this.number = strArr;
    }

    public String getStyledCharacter(char c) {
        if (c >= 'a' && c <= 'z') {
            return Html.fromHtml(this.lower[c - 'a']).toString();
        }
        if (c >= 'A' && c <= 'Z') {
            return Html.fromHtml(this.cap[c - 'A']).toString();
        }
        if (c < '0' || c > '9') {
            return String.valueOf(c);
        }
        return Html.fromHtml(this.number[c - '0']).toString();
    }

    public String getStyledNumber(int i) {
        return Html.fromHtml(this.number[i]).toString();
    }

    public String getStyledString(String str) {
         String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            str2 = str2 + getStyledCharacter(str.charAt(i));
        }
        return str2;
    }
}
