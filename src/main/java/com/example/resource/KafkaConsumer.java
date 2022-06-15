package com.example.resource;

import javax.annotation.PostConstruct;

import com.example.config.KafkaMessageListener;
import com.example.util.TPMUtil;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Profile(value = "consumer")
@Component
public class KafkaConsumer  implements MessageListener<String, String> {
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private TPMUtil tpsUtil;
    
    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        // TODO Auto-generated method stub
        LOGGER.info("(deque) {}", data.toString());
        tpsUtil.add();
    }
} 