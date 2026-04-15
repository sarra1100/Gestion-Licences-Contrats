package com.example.projet2024.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        String authorization = request.getHeader("Authorization");
        long startTime = System.currentTimeMillis();

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("🌐 INCOMING REQUEST:");
        System.out.println("   Method: " + method);
        System.out.println("   URI: " + uri + queryString);
        System.out.println("   Authorization: " + (authorization != null ? "Bearer [present]" : "NONE"));
        System.out.println("   Remote Address: " + request.getRemoteAddr());
        System.out.println("╚════════════════════════════════════════════════╝");

        try {
            filterChain.doFilter(request, response);
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("✅ Response Status: " + response.getStatus() + " for " + method + " " + uri + " (took " + duration + "ms)");
        } catch (Exception e) {
            System.out.println("❌ Exception during request: " + e.getMessage());
            System.out.println("❌ Exception Type: " + e.getClass().getName());
            System.out.println("❌ Stack trace:");
            e.printStackTrace();
            throw e;
        }
    }
}
