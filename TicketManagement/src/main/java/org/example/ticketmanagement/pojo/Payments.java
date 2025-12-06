package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payments {
    private Integer id;
    private Integer orderId;
    private String status;
    private LocalDateTime paymentDate;
    private String callbackData;
}
