package com.dani.couponsystemv2.filter;

import com.dani.couponsystemv2.Services.AdminDetailsService;
import com.dani.couponsystemv2.Services.CompanyDetailsService;
import com.dani.couponsystemv2.Services.CustomerDetailsService;
import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.model.Scope;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import static com.dani.couponsystemv2.model.Scope.*;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomerDetailsService customerDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final AdminDetailsService adminDetailsService;
    private final JwtService jwtService;

    private UserDetails mapToUserDetails(Claims claims) {
        Scope scope = jwtService.extractScope.apply(claims);
        String username = jwtService.extractName.apply(claims);

        return scope.equals(ADMIN) ?
                adminDetailsService.loadUserByUsername(username) :
                scope.equals(COMPANY) ?
                        companyDetailsService.loadUserByUsername(username) :
                        customerDetailsService.loadUserByUsername(username);
    }

    private boolean validateToken(Claims claims) {
        return jwtService.validateToken(claims, mapToUserDetails(claims));
    }

    private UsernamePasswordAuthenticationToken mapToToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private Consumer<UsernamePasswordAuthenticationToken> setContextAuth(HttpServletRequest request) {
        return authToken -> {
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        };
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader("Authorization"))
                .map(header -> header.substring(7))
                .map(jwtService::decodeJwt)
                .filter(this::validateToken)
                .map(this::mapToUserDetails)
                .map(this::mapToToken)
                .ifPresent(setContextAuth(request));

        chain.doFilter(request, response);
    }
}
