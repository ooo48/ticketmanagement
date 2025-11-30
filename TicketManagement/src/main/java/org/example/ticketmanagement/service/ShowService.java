package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.ShowDetail;
import org.example.ticketmanagement.pojo.Shows;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ShowService {
    List<Shows> getShowsForHomepage(String city);
    List<Shows> searchShows(String keyword);
    List<Shows> getShows(String city, String category,int page,int size);
    List<ShowDetail> getShowDetails(int id);
}
