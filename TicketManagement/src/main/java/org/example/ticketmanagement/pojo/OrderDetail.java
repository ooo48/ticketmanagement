package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private Integer id;
    private Integer userId;
    private Integer showTicketId;
    private Integer quantity;
    private Integer totalPrice;
    private String status;
    private LocalDateTime paymentDate;
    private String callbackData;

    // 演出信息
    private String showName;
    private LocalDate date;
    private LocalTime sessionTime;
    private Integer ticketPrice;
}
