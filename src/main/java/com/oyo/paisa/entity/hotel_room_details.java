package com.oyo.paisa.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_room_types")
public class hotel_room_details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("hotel_id")
    @Column(name = "hotel_id")
    private Integer hotel_id;

    @JsonProperty("room_category_id")
    @Column(name = "room_category_id")
    private Integer room_category_id;

    @JsonProperty("prices")
    @Column(name = "prices")
    private String prices;

    @JsonProperty("date")
    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @JsonProperty("status")
    @Column(name = "status")
    private int status;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @JsonProperty("reason")
    @Column(name = "reason")
    private String reason;

}
