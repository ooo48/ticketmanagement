package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.*;
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

    //用户端获取演出详情
    @Select("select shows.id,shows.city,shows.name,shows.default_area,shows.category," +
            "show_sessions.date,show_sessions.time," +
            "show_tickets.price,show_tickets.is_open,show_tickets.has_stock " +
            "from shows,show_sessions,show_tickets " +
            "where shows.id=#{id} AND shows.id=show_sessions.show_id AND show_sessions.id=show_tickets.session_id")
    List<ShowDetail> getShowDetails(int id);

    //管理端获取演出详情
    @Select("select shows.id,shows.city,shows.name,shows.default_area,shows.category," +
            "show_sessions.date,show_sessions.time," +
            "show_tickets.price,show_tickets.is_open,show_tickets.stock " +
            "from shows,show_sessions,show_tickets " +
            "where shows.id=#{id} AND shows.id=show_sessions.show_id AND show_sessions.id=show_tickets.session_id")
    List<ShowDetail> getAdminShowDetails(int id);

    //添加演出
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into shows(name,city,default_area,category) values(#{name},#{city},#{defaultArea},#{category})")
    void addShow(Shows shows);

    //修改演出信息
    void updateShow(Shows shows);

    //删除演出信息
    @Delete("delete from shows where id=#{id} ")
    void deleteShow(int id);
}
