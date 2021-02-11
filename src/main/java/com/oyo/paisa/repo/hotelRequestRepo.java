package com.oyo.paisa.repo;

import com.oyo.paisa.entity.hotel_room_details;
import com.oyo.paisa.response.ConsumerPriceGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface hotelRequestRepo extends JpaRepository<hotel_room_details,Integer> {

    @Query(value ="SELECT * FROM hotel_room_types where hotel_id = :hotel_id",nativeQuery = true)
    List<hotel_room_details> findAllByHotelIds(Integer hotel_id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE hotel_room_types SET status = :status WHERE id = :reqId" , nativeQuery = true)
    void updateConsumerRequestStatus(@Param("status") Integer status, @Param("reqId") Integer reqId);

    //hotel_room_details findByReqId(Integer reqId);
    @Query(value = "SELECT * from hotel_room_types h where h.hotel_id= :hotel_id and h.room_category_id=:room_category_id and h.date = :date",nativeQuery = true)
    List<hotel_room_details> findAllByHotel(@Param("hotel_id") Integer status, @Param("room_category_id") Integer room_category_id , @Param("date") LocalDate date);

    @Query(value="SELECT * from hotel_room_types h where h.hotel_id= :hotel_id and h.room_category_id=:room_category_id and h.date=:date and h.status=:status",nativeQuery = true)
    hotel_room_details findhotel(@Param("hotel_id") Integer hotel_id, @Param("room_category_id") Integer room_category_id, @Param("date") LocalDate date,@Param("status") Integer status);
}
