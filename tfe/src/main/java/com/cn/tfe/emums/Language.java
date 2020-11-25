package com.cn.tfe.emums;

public enum Language {
    EN("en"),
    ZH("zh");

    public String val;
    private Language(String val){
        this.val = val;
    }
    public String toString(){
        return this.val;
    }
}
