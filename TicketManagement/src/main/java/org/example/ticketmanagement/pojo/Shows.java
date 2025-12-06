package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shows {
    private Integer id;
    private String name;
    private String city;
    private Integer defaultArea;
    private String category;

}
