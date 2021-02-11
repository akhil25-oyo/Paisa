package com.oyo.paisa.MesConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyo.paisa.entity.hotel_room_detailsTmp;
import com.oyo.paisa.request.ConsumerPricePostRequest;
import com.oyo.paisa.request.ConsumerPricePostRequestTmp;
import com.oyo.paisa.service.ConsumerService;
import com.oyo.paisa.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class MessengerConsumer {
    private ObjectMapper objectMapper = new TransformUtil().objectMapper();
    @Autowired
    ConsumerService consumerService;
    @KafkaListener(topics = "Messenger")
    public void receiveMessage(String message) throws JsonProcessingException {
        System.out.println("Message Recieved in Consumer  :"+message);
        ConsumerPricePostRequestTmp consumerPricePostRequestTmp= objectMapper.readValue(message, ConsumerPricePostRequestTmp.class);
        ConsumerPricePostRequest consumerPricePostRequest = new ConsumerPricePostRequest();
        consumerPricePostRequest.setHotel_id(Integer.valueOf(consumerPricePostRequestTmp.getHotel_id()));
       // Map<String, Double> map = objectMapper.readValue(consumerPricePostRequestTmp.getPrices(), Map.class);
        consumerPricePostRequest.setPrices(TransformUtil.fromJson(consumerPricePostRequestTmp.getPrices(),Map.class));
        consumerPricePostRequest.setRoom_category_id(Integer.valueOf(consumerPricePostRequestTmp.getRoom_category_id()));
        consumerPricePostRequest.setReason(consumerPricePostRequestTmp.getReason());
        consumerPricePostRequest.setDate(consumerPricePostRequestTmp.getDate());
        consumerService.postconsumerRequest(consumerPricePostRequest);
        }
    }
