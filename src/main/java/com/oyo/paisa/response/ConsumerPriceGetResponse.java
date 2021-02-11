package com.oyo.paisa.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerPriceGetResponse {
    @JsonProperty("hotel_id")
    private Integer hotel_id;

    @JsonProperty("room_category_id")
    private Integer room_category_id;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("status")
    private int status;

    @JsonProperty("created_at")
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    private LocalDateTime updated_at;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("prices")
    private Map<String,Double> prices;
}
