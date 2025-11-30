package org.example.ticketmanagement.interceptor;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override//访问目标资源前运行，返回true，放行，否则不放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url=request.getRequestURL().toString();
        log.info("请求的url:{}",url);

        if(url.contains("login")){
            log.info("登录操作，放行");
            return true;
        }

        String jwt=request.getHeader("token");

        if(StringUtils.isEmpty(jwt)){
            log.info("请求头token为空，返回未登录");
            Result error= Result.error("NOT_LOGIN");
            String notLogin= JSONObject.toJSONString(error);
            response.getWriter().print(notLogin);
            return false;
        }

        Claims claims=null;
        try{
            claims= JwtUtils.parseJwt(jwt);
        }catch (Exception e){
            e.printStackTrace();
            log.info("解析失败，返回未登录");
            Result error= Result.error("NOT_LOGIN");
            String notLogin=JSONObject.toJSONString(error);
            response.getWriter().print(notLogin);
            return false;
        }

        // 将用户ID存入request
        Integer userId = (Integer) claims.get("userId");
        request.setAttribute("userId", userId);
        // 将用户IPD存入request
        String IP = (String) claims.get("IP");
        request.setAttribute("IP", IP);

        log.info("令牌合法，放行");
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
