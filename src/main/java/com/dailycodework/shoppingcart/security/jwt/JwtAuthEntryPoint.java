package com.dailycodework.shoppingcart.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//when a request is issued this is the class that going to receive that request first
//this class is going to pick the request and check if that request has been authenticated or not
//Whenever an unauthenticated user tries to access a secured endpoint (e.g., a restricted API route),
// this class intercepts the request and sends an HTTP 401 Unauthorized response instead of redirecting to a login page
// (which is the default behavior in Spring Security).

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        final Map<String, Object> body = new HashMap<>();
        //body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "You may login and try again!");
        //body.put("path", request.getServletPath());


        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
