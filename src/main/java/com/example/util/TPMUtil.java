package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TPMUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(TPMUtil.class);
    private LinkedHashMap<String, Integer> tpsMap;
    private int size = 2;

    @PostConstruct
    public void init(){
        tpsMap = new LinkedHashMap<String, Integer>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                LOGGER.info("tpm:{}", eldest.getValue());
                return size() > size;
            }
        };
        initialize(System.currentTimeMillis());
    }
    public synchronized void initialize (long nowMilliSecond) {
       
        for (int i=0; i<size; i++){
            // long timestamp = nowMilliSecond - ((60*1000)*(size-i-1));
            String date = getNowyyyyMMddHHmm(nowMilliSecond);
            if(!tpsMap.containsKey(date)){
                tpsMap.put(date, 0);
            }else{

            }
        }
    }
    public synchronized void add(){
        long nowMilliSecond = System.currentTimeMillis();

        initialize(nowMilliSecond);

        int trafficCount = tpsMap.get(getNowyyyyMMddHHmm(nowMilliSecond));
        tpsMap.put(getNowyyyyMMddHHmm(nowMilliSecond), ++trafficCount);        
    }

    private static String getNowyyyyMMddHHmm(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(time));
    }
}
