package com.oyo.paisa.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyo.paisa.cache.ConsumerCache;
import com.fasterxml.jackson.core.type.TypeReference;
import com.oyo.paisa.entity.hotel_room_details;
import com.oyo.paisa.repo.hotelRequestRepo;
import com.oyo.paisa.request.ConsumerPriceGetAll;
import com.oyo.paisa.request.ConsumerPriceGetRequest;
import com.oyo.paisa.request.ConsumerPricePostRequest;
import com.oyo.paisa.response.ConsumerPriceGetResponse;
import com.oyo.paisa.util.TransformUtil;
import com.oyo.paisa.entity.hotel_room_detailsTmp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ConsumerCacheService {
    @Autowired
    ConsumerCache consumerCache;

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private ObjectMapper objectMapper = new ObjectMapper();
    public void setConsumerDetailsInCache (hotel_room_details details)
    {
        try {
            String key = "Room_DETAILS" + "_" + details.getHotel_id().toString() + details.getRoom_category_id().toString() + details.getDate().toString();
            Long ttl=Long.valueOf(2*24*60*60*1000);
            hotel_room_detailsTmp detailsTmp= new hotel_room_detailsTmp();
            detailsTmp.setHotel_id(details.getHotel_id());
            detailsTmp.setRoom_category_id(details.getRoom_category_id());
            detailsTmp.setPrices(details.getPrices());
            detailsTmp.setReason(details.getReason());
            detailsTmp.setStatus(details.getStatus());
            detailsTmp.setDate(details.getDate().toString());
            detailsTmp.setCreated_at(details.getCreated_at().format(formatter));
            detailsTmp.setUpdated_at(details.getUpdated_at().format(formatter));
            consumerCache.set(key, objectMapper.writeValueAsString(detailsTmp),ttl);
        }catch (Exception ex){
            log.error("exception occured while setting property_details in cache :: {}",details.toString());
        }
    }
    public hotel_room_details getConsumerFromCache (ConsumerPriceGetRequest details)
    {
        try {
            hotel_room_details tmp= new hotel_room_details();
            log.info("fetching topic details from cache :: {}",details);
            String key = "Room_DETAILS" + "_" + details.getHotel_id().toString() + details.getRoom_category_id().toString() + details.getDate().toString();
            String value= consumerCache.get(key);
            hotel_room_detailsTmp response= objectMapper.readValue(value,hotel_room_detailsTmp.class);
            tmp.setPrices(response.getPrices());
            tmp.setReason(response.getReason());
            tmp.setStatus(response.getStatus());
            tmp.setHotel_id(response.getHotel_id());
            tmp.setRoom_category_id(response.getRoom_category_id());
            tmp.setDate(LocalDate.parse(response.getDate()));
            tmp.setUpdated_at(LocalDateTime.parse(response.getUpdated_at(),formatter));
            tmp.setCreated_at(LocalDateTime.parse(response.getCreated_at(),formatter));
            System.out.println(response);
            return tmp;
        }catch (Exception ex){
            log.info("exception occured while fetching property_details in cache :: {}",details);
        }
        return null;
    }
    public void deleteConsumerDetailsInCache (ConsumerPriceGetRequest details)
    {
        try {
            String key = "Room_DETAILS" + "_" + details.getHotel_id() + details.getRoom_category_id() + details.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Long ttl=Long.valueOf(2*24*60*60*1000);
            consumerCache.delete(key);
        }catch (Exception ex){
            log.error("exception occured while setting property_details in cache :: {}",details.toString());
        }
    }
}