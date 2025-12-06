package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminShowDetail {
    private Integer id;
    private String name;
    private String city;
    private Integer defaultArea;
    private String category;
    private LocalDate date;
    private LocalTime time;
    private Integer price;
    private Integer stock;
    private Integer isOpen;
}
