package com.example.resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.example.util.TPMUtil;
import com.example.vo.TestDataVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Profile(value = "producer")
@RestController
public class KafkaProducer implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private TPMUtil tpsUtil;

    private ExecutorService workerPool;
    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init(){
        workerPool = Executors.newCachedThreadPool();
    }

    @ResponseBody
	@RequestMapping(value="/kafka", 
                    method = RequestMethod.POST)
    public ResponseEntity<Object> requestEmayLogin(HttpServletRequest request) throws Exception{
        int i=0;
        String key = String.format("%010d", i++);
        TestDataVO testDataVO = new TestDataVO(key, System.currentTimeMillis()/1000, 0);
        try {
            kafkaTemplate.send("TEST", mapper.writeValueAsString(testDataVO));
            LOGGER.info("(enque) {}", mapper.writeValueAsString(testDataVO));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            LOGGER.error("error", e);
        }
       
		return new ResponseEntity<>(HttpStatus.OK);
	}

    @ResponseBody
	@RequestMapping(value="/kafka/bulk", 
                    method = RequestMethod.POST)
    public ResponseEntity<Object> requestBulk(HttpServletRequest request) throws Exception{
        workerPool.execute(this);
		return new ResponseEntity<>(HttpStatus.OK);
	}

    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        LOGGER.info("Producer Started.");
        int i=0;
        long startTime = System.currentTimeMillis()/1000;
        while (true){
            if (System.currentTimeMillis()/1000 - startTime >= 180){
                break;
            }
            
            if (i > Integer.MAX_VALUE){
                i=0;
            }
            String key = String.format("%010d", i++);
            TestDataVO testDataVO = new TestDataVO(key, System.currentTimeMillis()/1000, 0);
            try {
                kafkaTemplate.send("TEST", mapper.writeValueAsString(testDataVO));
                LOGGER.info("(enque) {}", mapper.writeValueAsString(testDataVO));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                LOGGER.error("error", e);
            }
            tpsUtil.add();
        }
    }

}
