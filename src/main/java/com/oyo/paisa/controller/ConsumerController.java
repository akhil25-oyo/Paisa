package com.oyo.paisa.controller;
import com.oyo.paisa.cache.ConsumerCache;
import com.oyo.paisa.entity.hotel_room_details;
import com.oyo.paisa.request.ConsumerPriceGetAll;
import com.oyo.paisa.request.ConsumerPriceGetRequest;
import com.oyo.paisa.request.ConsumerPricePostRequest;
import com.oyo.paisa.request.ConsumerPricePostRequestTmp;
import com.oyo.paisa.response.ConsumerPriceGetResponse;
import com.oyo.paisa.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
public class ConsumerController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerCache consumerCache;
    @PostMapping(value = "/post")
    public hotel_room_details postData(@RequestBody ConsumerPricePostRequest consumerRequest) {
       return  consumerService.postconsumerRequest(consumerRequest);
    }
    @GetMapping(value = "/getdata")
    public ConsumerPriceGetResponse getData(@RequestBody ConsumerPriceGetRequest consumerRequest) {
        return  consumerService.getconsumerRequest(consumerRequest);
    }
    @GetMapping(value = "/getAll")
    public List<hotel_room_details> getDataAll(@RequestBody ConsumerPriceGetAll consumerRequest) {
        return  consumerService.getconsumerRequestByHotelId(consumerRequest);
    }
    @GetMapping(value="redis")
    public Object getAuth(){
        Long time=10000L;
        consumerCache.set("TEST_KEY","TEST_VALUE",time);
        return consumerCache.get("TEST_KEY");
    }
    @PostMapping(value="/post/upload")
    public List<ConsumerPricePostRequestTmp> postCsvData(@RequestParam("file") MultipartFile file) throws IOException {
        return consumerService.postconsumerRequestbyCsv(file);
    }
}
