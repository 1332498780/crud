package com.cn.tfe.commons;

import com.cn.tfe.emums.Language;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class TransferLanguage {

    public static final Language DEFAULT_FROM = Language.EN;

    public static final Language DEFAULT_TO = Language.ZH;

    private Language from;

    private Language to;

    /***
     * 判断当且仅当from或to为null/"" 时返回false
     * @param from
     * @param to
     * @return
     */
    public static boolean tryBuild(String from,String to){
        if(!StringUtils.isEmpty(from)){
            if(!Language.existsLanguage(from)){
                return false;
            }
        }
        if(!StringUtils.isEmpty(to)){
            if(!Language.existsLanguage(to)){
                return false;
            }
        }
        return true;
    }

    public static TransferLanguage build(String from,String to){
        TransferLanguage transferLanguage = new TransferLanguage();
        if(StringUtils.isEmpty(from)){
            transferLanguage.setFrom(Language.DEFAULT_FROM);
        }else{
            transferLanguage.setFrom(Language.fromVal(from));
        }
        if(StringUtils.isEmpty(to)){

            transferLanguage.setTo(Language.DEFAULT_TO);
        }else{
            transferLanguage.setTo(Language.fromVal(to));
        }
        return transferLanguage;
    }

    public int getFromToVal(){
        return this.from.getCode()*10+this.to.getCode();
    }
}
