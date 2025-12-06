package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.ShowMapper;
import org.example.ticketmanagement.pojo.ShowDetail;
import org.example.ticketmanagement.pojo.Shows;
import org.example.ticketmanagement.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowMapper showMapper;

    @Override
    public List<Shows> getShowsForHomepage(String city){
        if (city==null||city.isEmpty()) {
            city ="北京";
        }
        return showMapper.getShowsForHomepage(city);
    }

    @Override
    public List<Shows> searchShows(String keyword){
        return showMapper.searchShows(keyword);
    }

    @Override
    public List<Shows> getShows(String city, String category,int page,int size){
        int index=(page-1)*size;
        return showMapper.getShows(city, category, index, size);
    }

    @Override
    public List<ShowDetail> getShowDetails(int id){
        return showMapper.getShowDetails(id);
    }

    @Override
    public List<ShowDetail> getAdminShowDetails(int id){
        return showMapper.getAdminShowDetails(id);
    }

    @Override
    public void addShow(Shows shows){
        showMapper.addShow(shows);
    }

    @Override
    public void updateShow(Shows shows){
        showMapper.updateShow(shows);
    }

    @Override
    public void deleteShow(int id){
        showMapper.deleteShow(id);
    }
}
