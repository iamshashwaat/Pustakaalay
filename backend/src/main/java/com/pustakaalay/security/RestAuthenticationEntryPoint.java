package com.pustakaalay.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json =
                "{"
                + "\"status\":401,"
                + "\"error\":\"Unauthorized\","
                + "\"message\":\"Authentication is required\","
                + "\"path\":\"" + escape(request.getRequestURI()) + "\""
                + "}";

        response.getWriter().write(json);
    }

    private String escape(String value) {
        return value == null
                ? ""
                : value.replace("\\", "\\\\")
                       .replace("\"", "\\\"");
    }
}
