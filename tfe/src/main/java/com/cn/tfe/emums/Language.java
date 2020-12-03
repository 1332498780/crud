package com.cn.tfe.emums;

public enum Language {
    EN("en",1){
        @Override
        public String regx(){
            return "[^a-z|\\s]*";
        }
    },
    ZH("zh",2){
        @Override
        public String regx(){
            return "[^\\u4e00-\\u9fa5|\\s]*";
        }
    },
    JP("jp",3){
        @Override
        public String regx(){
            return "[^\\u0800-\\u4e00|\\s]*";
        }
    },
    KOR("kor",4){
        @Override
        public String regx() {
            return "[^\\uAC00-\\uD7A3|\\s]*";
        }
    },
    SPA("spa",5){

    }
    ;

    public String val;
    private int code;
    private Language(String val,int code){
        this.val = val;
        this.code = code;
    }
    public String toString(){
        return this.val;
    }

    public int getCode(Language to){
        return this.code*10+to.code;
    }

    public String regx(){
        return "";
    }
}
