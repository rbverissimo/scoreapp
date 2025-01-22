package br.com.serasa.scoreapp.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MaskUtils {

    public String removerMascara(String s){
        if(StringUtils.isBlank(s)) return s;
        return s.replaceAll("[^\\d]", "");
    }
}
