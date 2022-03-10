package com.spotmoney.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * fills map at application startup
 */
@Component
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private ConcurrentHashMap<String, String> countryMap;

    @PostConstruct
    private void init(){
        logger.info("fill country map at application startup");
        countryMap = new ConcurrentHashMap<>();
        countryMap.put("076","BR");
        countryMap.put("188","CR");
        countryMap.put("818","EG");
        countryMap.put("288","GH");
        countryMap.put("288","GH");
        countryMap.put("356","IN");
    }

    public String getCountry(String countryCode){
        return countryMap.get(countryCode);
    }
}
