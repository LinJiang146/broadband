package com.wei.broadband.filter;

import com.alibaba.fastjson.JSON;
import com.wei.broadband.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@CrossOrigin
@Order(0)
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String requestURI = request.getRequestURI();

        String urls[] = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**",
          "/json-server/**",
          "/user/login",
          "/user/logout",
          "/common/excel",
          "/common/upload",
          "/common/**",
          "/static/backend/**",
        };

        boolean check = check(urls,requestURI);

        if(true){
            filterChain.doFilter(request,response);
            return;
        }


        if(request.getSession().getAttribute("user")!=null){
            //Long empId = (Long)request.getSession().getAttribute("user");
            //BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }


        response.getWriter().write(JSON.toJSONString(R.success("Not logged in")));
        return;

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match) return true;
        }
        return false;
    }
}
