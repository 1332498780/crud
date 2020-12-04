package com.cn.tfe.emums;

import org.springframework.util.StringUtils;

public enum Language {
    EN("en",1,"a-z"){
    },
    ZH("zh",2,"\\u4e00-\\u9fa5"){
    },
    JP("jp",3,"\\u0800-\\u4e00"){
    },
    KOR("kor",4,"\\uAC00-\\uD7A3"){
    },
    SPA("spa",5,""){
    }
    ;

    private String val;
    private int code;
    private String regx;

    public static final Language DEFAULT_FROM = Language.EN;
    public static final Language DEFAULT_TO = Language.ZH;

    private Language(String val,int code,String regx){
        this.val = val;
        this.code = code;
        this.regx = regx;
    }
    public String toString(){
        return this.val;
    }

    public int getCode(){
        return this.code;
    }

    public static boolean existsLanguage(String languageStr){
        for(Language lan:Language.values()){
            if(languageStr.equalsIgnoreCase(lan.val)){
                return true;
            }
        }
        return false;
    }

    public static Language fromVal(String val){
        for(Language lan:Language.values()){
            if(val.equalsIgnoreCase(lan.val)){
                return lan;
            }
        }
        return null;
    }

    public String lowAndReplace(String word){
        if(StringUtils.isEmpty(this.regx)){
            return word;
        }
        return word.toLowerCase().replaceAll(getReplaceRegx(this.regx),"");
    }
    public boolean isValid(String word){
        if(StringUtils.isEmpty(word)){
            return false;
        }else if(StringUtils.isEmpty(this.regx)){
            return true;
        }
        return word.matches(getValidRegx(this.regx));
    }

    /***
     * e.g. 英文: [^a-z|\s]*
     * @param regx
     * @return
     */
    static String getReplaceRegx(String regx){
        return "[^"+regx+"|\\s]*";
    }

    /***
     * e.g. 英文：^[a-z][a-z|\s]*[a-z]$
     * @param regx:当前语种的正则匹配规则
     * @return
     */
    static String getValidRegx(String regx){
        return "^["+regx+"]["+regx+"|\\s]*["+regx+"]$";
    }
}
