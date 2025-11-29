package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTickets {
    private Integer id;
    private Integer sessionId;
    private Integer price;
    private Integer stock;
    private Integer isOpen;
    private Integer hasStock;
}
