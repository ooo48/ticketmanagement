package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.ticketmanagement.pojo.Shows;

import java.util.List;

@Mapper
public interface ShowMapper {
    // 首页获取地区与分类演出列表
    @Select("select * from shows where city=#{city}")
    List<Shows> getShowsForHomepage(String city);

    // 搜索演出
    @Select("select * from shows where name=#{keyword}")
    List<Shows> searchShows(String keyword);

    // 条件查询演出
    List<Shows> getShows(String city, String category,int index,int size);

    //获取演出详情
    @Select("select * from shows,show_sessions,show_tickets where shows.id=#{id}")
    Shows getShowDetails(int id);
}
