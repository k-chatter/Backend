package com.sum.chatter.configuration.filter;

import com.sum.chatter.service.auth.JwtBuilder;
import com.sum.chatter.service.auth.JwtInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtBuilder jwtBuilder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        JwtInfo jwtInfo = jwtBuilder.decryptJwt(jwtToken);

        if (jwtInfo.id() != null) {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    jwtToken,
                    jwtInfo.id(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}
