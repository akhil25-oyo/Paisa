package com.oyo.paisa.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.oyo.paisa.MesProducer.MessengerProducer;
import com.oyo.paisa.entity.hotel_room_details;
import com.oyo.paisa.repo.hotelRequestRepo;
import com.oyo.paisa.request.ConsumerPriceGetAll;
import com.oyo.paisa.request.ConsumerPriceGetRequest;
import com.oyo.paisa.request.ConsumerPricePostRequest;
import com.oyo.paisa.request.ConsumerPricePostRequestTmp;
import com.oyo.paisa.response.ConsumerPriceGetResponse;
import com.oyo.paisa.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ConsumerService {
    @Autowired
    hotelRequestRepo consumerRequestRepo;
    @Autowired
    ConsumerCacheService consumerCacheService;
    @Autowired
    MessengerProducer messengerProducer;
    private ObjectMapper objectMapper = new TransformUtil().objectMapper();
    public hotel_room_details postconsumerRequest(ConsumerPricePostRequest consumerPricePostRequest)
    {
        hotel_room_details consumerRequest=new hotel_room_details();
        consumerRequest.setRoom_category_id(consumerPricePostRequest.getRoom_category_id());
        consumerRequest.setHotel_id(consumerPricePostRequest.getHotel_id());
        consumerRequest.setPrices(TransformUtil.toJson(consumerPricePostRequest.getPrices()));
        consumerRequest.setDate(consumerPricePostRequest.getDate());
        consumerRequest.setReason(consumerPricePostRequest.getReason());
        consumerRequest.setStatus(0);
        hotel_room_details temp=consumerRequestRepo.findhotel(consumerPricePostRequest.getHotel_id(), consumerPricePostRequest.getRoom_category_id(), consumerPricePostRequest.getDate(),0);
        if(temp==null)
        {
            consumerRequest.setCreated_at(LocalDateTime.now());
            consumerRequest.setUpdated_at(LocalDateTime.now());
        }
        else
        {
            consumerRequestRepo.updateConsumerRequestStatus(1,temp.getId());
            consumerRequest.setCreated_at(temp.getCreated_at());
            consumerRequest.setUpdated_at(LocalDateTime.now());
            ConsumerPriceGetRequest temp2= new ConsumerPriceGetRequest();
            temp2.setHotel_id(consumerPricePostRequest.getHotel_id());
            temp2.setDate(consumerPricePostRequest.getDate());
            temp2.setRoom_category_id(consumerPricePostRequest.getRoom_category_id());
            consumerCacheService.deleteConsumerDetailsInCache(temp2);
        }
        return consumerRequestRepo.save(consumerRequest);
    }
    public List<hotel_room_details> getconsumerRequestByHotelId(ConsumerPriceGetAll consumerRequest)
    {
        return consumerRequestRepo.findAllByHotel(consumerRequest.getHotel_id(),consumerRequest.getRoom_category_id(),consumerRequest.getDate());
    }
    public ConsumerPriceGetResponse getconsumerRequest(ConsumerPriceGetRequest consumerPriceGetRequest)
    {
        hotel_room_details temp= consumerCacheService.getConsumerFromCache(consumerPriceGetRequest);
        hotel_room_details consumerRequest = null;
        if(temp==null)
        {
            consumerRequest= consumerRequestRepo.findhotel(consumerPriceGetRequest.getHotel_id(), consumerPriceGetRequest.getRoom_category_id(), consumerPriceGetRequest.getDate(),0);
            if(consumerRequest==null)
                return null;
            consumerCacheService.setConsumerDetailsInCache(consumerRequest);
        }
        else
        {
            consumerRequest=temp;
        }
        ConsumerPriceGetResponse consumerPriceGetResponse=new ConsumerPriceGetResponse();
        consumerPriceGetResponse.setHotel_id(consumerRequest.getHotel_id());
        consumerPriceGetResponse.setRoom_category_id(consumerRequest.getRoom_category_id());
        Map<String,Double>priceMap=(Map<String, Double>)TransformUtil.fromJson(consumerRequest.getPrices(), new TypeReference<Map<String, Double>>() {});
        consumerPriceGetResponse.setPrices(priceMap);
        consumerPriceGetResponse.setStatus(consumerRequest.getStatus());
        consumerPriceGetResponse.setDate(consumerRequest.getDate());
        consumerPriceGetResponse.setReason(consumerRequest.getReason());
        consumerPriceGetResponse.setCreated_at(consumerRequest.getCreated_at());
        consumerPriceGetResponse.setUpdated_at(consumerRequest.getUpdated_at());
        return consumerPriceGetResponse;
    }
    public List<ConsumerPricePostRequestTmp> postconsumerRequestbyCsv(MultipartFile file) throws IOException {
        List<ConsumerPricePostRequestTmp> lst= new ArrayList<>();
        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        String[] headers;
        while((headers = csvReader.readNext())!=null)
        {
            Map<String,Double> mp = new HashMap<>();
            ConsumerPricePostRequestTmp ht = new ConsumerPricePostRequestTmp();
            ht.setHotel_id(headers[0]);
            ht.setRoom_category_id(headers[1]);
            mp.put("1",Double.parseDouble(headers[2]));
            mp.put("2",Double.parseDouble(headers[3]));
            mp.put("3",Double.parseDouble(headers[4]));
            //String price= new ObjectMapper().writeValueAsString(mp);
            ht.setPrices(TransformUtil.toJson(mp));
            ht.setDate(LocalDate.parse(headers[5]));
            ht.setReason(headers[6]);
            String str=objectMapper.writeValueAsString(ht);
            System.out.println(str);
            messengerProducer.sendMessage(str);
            lst.add(ht);
        }
        return lst;
    }
}
