package com.oyo.paisa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class hotel_room_detailsTmp {
    private Integer hotel_id;


    private Integer room_category_id;


    private String date;


    private Integer status;


    private String created_at;


    private String updated_at;


    private String reason;


    private String prices;
}
