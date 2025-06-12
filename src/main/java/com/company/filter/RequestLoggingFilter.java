package com.company.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    // Paths to exclude from logging
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/favicon.ico",
            "/robots.txt",
            "/sitemap.xml",
            "/health",
            "/actuator"
    );

    // Headers to exclude from meaningful data check (browser/system headers)
    private static final Set<String> SYSTEM_HEADERS = Set.of(
            "host", "connection", "user-agent", "accept", "accept-encoding",
            "accept-language", "cache-control", "upgrade-insecure-requests",
            "sec-fetch-site", "sec-fetch-mode", "sec-fetch-dest", "sec-ch-ua",
            "sec-ch-ua-mobile", "sec-ch-ua-platform", "referer", "dnt",
            "pragma", "if-modified-since", "if-none-match"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Skip logging for excluded paths
        String requestPath = request.getRequestURI();
        if (shouldExcludePath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Collect request data
        String queryString = request.getQueryString();
        String body = extractBody(request);
        String meaningfulHeaders = extractMeaningfulHeaders(request);

        // Always log basic info (IP, method, endpoint)
        String ipAddress = getClientIpAddress(request);
        StringBuilder logMessage = new StringBuilder("\nREQUEST LOG:\n");
        logMessage.append("Method: ").append(request.getMethod()).append("\n");
        logMessage.append("Path: ").append(requestPath).append("\n");
        logMessage.append("IP Address: ").append(ipAddress).append("\n");

        // Only add additional data if meaningful
        if (hasMeaningfulData(queryString, body, meaningfulHeaders)) {
            if (queryString != null && !queryString.trim().isEmpty()) {
                logMessage.append("Query: ").append(queryString).append("\n");
            }

            if (meaningfulHeaders != null && !meaningfulHeaders.trim().isEmpty()) {
                logMessage.append("Headers: ").append(meaningfulHeaders).append("\n");
            }

            if (body != null && !body.trim().isEmpty()) {
                logMessage.append("Body: ").append(body).append("\n");
            }
        }

        logger.info(logMessage.toString());

        filterChain.doFilter(request, response);
    }

    private boolean shouldExcludePath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith) ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".jpeg") ||
                path.endsWith(".gif") ||
                path.endsWith(".ico") ||
                path.endsWith(".svg") ||
                path.endsWith(".woff") ||
                path.endsWith(".woff2") ||
                path.endsWith(".ttf");
    }

    private String extractBody(HttpServletRequest request) {
        String body = "";
        if ("POST".equalsIgnoreCase(request.getMethod()) ||
                "PUT".equalsIgnoreCase(request.getMethod()) ||
                "PATCH".equalsIgnoreCase(request.getMethod())) {
            try (BufferedReader reader = request.getReader()) {
                body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                logger.debug("Could not read request body: {}", e.getMessage());
            }
        }
        return body;
    }

    private String extractMeaningfulHeaders(HttpServletRequest request) {
        StringBuilder meaningfulHeaders = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerNameLower = headerName.toLowerCase();

            // Only include headers that are not system/browser headers
            if (!SYSTEM_HEADERS.contains(headerNameLower) &&
                    !headerNameLower.startsWith("sec-") &&
                    !headerNameLower.startsWith("x-forwarded") &&
                    !headerNameLower.startsWith("x-real")) {

                String headerValue = request.getHeader(headerName);
                meaningfulHeaders.append(headerName).append(": ").append(headerValue).append("; ");
            }
        }

        return meaningfulHeaders.toString();
    }

    private boolean hasMeaningfulData(String queryString, String body, String meaningfulHeaders) {
        return (queryString != null && !queryString.trim().isEmpty()) ||
                (body != null && !body.trim().isEmpty()) ||
                (meaningfulHeaders != null && !meaningfulHeaders.trim().isEmpty());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }

        String xrHeader = request.getHeader("X-Real-IP");
        if (xrHeader != null && !xrHeader.isEmpty()) {
            return xrHeader;
        }

        return request.getRemoteAddr();
    }
}