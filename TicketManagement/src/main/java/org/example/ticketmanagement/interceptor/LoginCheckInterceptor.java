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

        // 从token中获取用户角色
        String role = (String) claims.get("position");
        log.info("当前用户角色: {}", role);

        // 将用户ID和角色存入请求属性，供后续控制器使用
        request.setAttribute("userId", claims.get("id"));
        request.setAttribute("userRole", role);

        // 检查权限
        if (!hasPermission(request, role)) {
            log.info("权限不足，拒绝访问");
            Result error = Result.error("NO_PERMISSION");
            String noPermission = JSONObject.toJSONString(error);
            response.getWriter().print(noPermission);
            return false;
        }

        log.info("令牌合法，放行");
        return true;
    }
}
