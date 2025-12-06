package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Integer id;
    private Integer userId;
    private Integer showTicketId;
    private Integer quantity;
    private Integer totalPrice;
    private String status;
}
