package com.pustakaalay.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json =
                "{"
                + "\"status\":403,"
                + "\"error\":\"Forbidden\","
                + "\"message\":\"You do not have permission to access this resource\","
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
