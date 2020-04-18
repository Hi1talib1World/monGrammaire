package com.example.mongrammaire.horisontal_cardv.categories.subordonnes;

public class subs {
    private String subName,desc;

    public subs(String subName, String desc) {
        this.subName = subName;
        this.desc = desc;
    }

    public String getCallerName() {
        return subName;
    }

    public String getCallTime() {
        return desc;
    }
}
