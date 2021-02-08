package com.oyo.paisa.MesProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessengerProducer {

    private String topic="Messenger";
    @Autowired
    KafkaTemplate<Object, Object> kafkaTemplate;
    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
