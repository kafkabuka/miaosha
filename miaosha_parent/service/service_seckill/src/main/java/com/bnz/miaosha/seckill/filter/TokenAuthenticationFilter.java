package com.bnz.miaosha.seckill.filter;

import com.bnz.miaosha.entity.User;
import com.bnz.miaosha.seckill.config.TokenDecode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenDecode tokenDecode;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("SecKill TokenAuthenticationFilter");
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String element = headerNames.nextElement();
            System.out.println("Header::"+element);
        }

        String bearerToken = httpServletRequest.getHeader("Authorization");
        log.info("Bearer Token:{}", bearerToken);
        if (bearerToken != null && bearerToken.equals("")) {
            String token = bearerToken.substring(7);
            Map<String, String> claims = tokenDecode.dcodeToken(token);
            String userJson = claims.get("username");
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);
            log.info("user:{}", user);
            if (user != null) {
                String authorities = claims.get("authorities");
                List<GrantedAuthority> authorities1 = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                //2.新建并填充authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, tokenDecode.getPubKey(),
                                AuthorityUtils.createAuthorityList(authorities));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                //3.将authentication保存进安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
