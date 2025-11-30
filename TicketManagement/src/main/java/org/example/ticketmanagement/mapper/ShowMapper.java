package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.ticketmanagement.pojo.ShowDetail;
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
    @Select("select shows.id,shows.city,shows.name,shows.default_area,shows.category," +
            "show_sessions.date,show_sessions.time," +
            "show_tickets.price,show_tickets.stock,show_tickets.is_open,show_tickets.has_stock " +
            "from shows,show_sessions,show_tickets " +
            "where shows.id=#{id} AND shows.id=show_sessions.show_id AND show_sessions.id=show_tickets.session_id")
    List<ShowDetail> getShowDetails(int id);
}
