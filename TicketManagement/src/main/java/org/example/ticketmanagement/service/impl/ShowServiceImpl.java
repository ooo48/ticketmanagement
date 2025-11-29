package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.ShowMapper;
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
        // 默认城市逻辑：若 city 为空则使用默认城市 Beijing
        if (city.isEmpty()) {
            city ="北京";
        }
        // Mapper 中的 SQL 按 city 查询并将默认推荐演出排在前面
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
    public Shows getShowDetails(int id){
        return showMapper.getShowDetails(id);
    }

}
