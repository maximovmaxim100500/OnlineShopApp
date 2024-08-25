package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Добавляет заголовок, позволяющий отправлять учетные данные (cookies) через CORS
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        // Продолжает выполнение следующего фильтра в цепочке
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
