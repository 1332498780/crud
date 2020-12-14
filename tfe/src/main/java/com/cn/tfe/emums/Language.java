package com.cn.tfe.emums;

import lombok.val;

public enum Language {
    EN("en",1),
    ZH("zh",2),
    JP("jp",3),
    KOR("kor",4),
    SPA("spa",5),
    FRA("fra",6),
    RU("ru",7),
    IT("it",8),
    DE("de",9);




    public String val;
    public int num;
    private Language(String val,int num){
        this.val = val;
        this.num = num;
    }
    public String toString(){
        return this.val;
    }
}
