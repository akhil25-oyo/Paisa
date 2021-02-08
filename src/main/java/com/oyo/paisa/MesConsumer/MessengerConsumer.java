package com.oyo.paisa.MesConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyo.paisa.entity.hotel_room_details;
import com.oyo.paisa.request.ConsumerPricePostRequest;
import com.oyo.paisa.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import  com.oyo.paisa.util.TransformUtil;
@Service
public class MessengerConsumer {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ConsumerService consumerService;
    @KafkaListener(topics = "Messenger")
    public void receiveMessage(String message) throws JsonProcessingException {
        System.out.println("Message Recieved in Consumer  :"+message);
        String kafkamsg=TransformUtil.getParsedResponse(message);
        ConsumerPricePostRequest consumerPricePostRequest= TransformUtil.fromJson(kafkamsg,ConsumerPricePostRequest.class);
        System.out.println(consumerPricePostRequest);
        consumerService.postconsumerRequest(consumerPricePostRequest);
    }
}