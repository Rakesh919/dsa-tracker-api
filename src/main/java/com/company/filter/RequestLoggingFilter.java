package com.company.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/favicon.ico", "/robots.txt", "/sitemap.xml", "/health", "/actuator"
    );

    private static final Set<String> SYSTEM_HEADERS = Set.of(
            "host", "connection", "user-agent", "accept", "accept-encoding",
            "accept-language", "cache-control", "upgrade-insecure-requests",
            "sec-fetch-site", "sec-fetch-mode", "sec-fetch-dest", "sec-ch-ua",
            "sec-ch-ua-mobile", "sec-ch-ua-platform", "referer", "dnt",
            "pragma", "if-modified-since", "if-none-match"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        if (shouldExcludePath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // Proceed with the request to allow Spring to consume it first
        filterChain.doFilter(wrappedRequest, response);

        // Now safely read the cached body
        String queryString = wrappedRequest.getQueryString();
        String body = extractBody(wrappedRequest);
        String meaningfulHeaders = extractMeaningfulHeaders(wrappedRequest);
        String ipAddress = getClientIpAddress(wrappedRequest);

        StringBuilder logMessage = new StringBuilder("\nREQUEST LOG:\n");
        logMessage.append("Method: ").append(wrappedRequest.getMethod()).append("\n");
        logMessage.append("Path: ").append(requestPath).append("\n");
        logMessage.append("IP Address: ").append(ipAddress).append("\n");

        if (hasMeaningfulData(queryString, body, meaningfulHeaders)) {
            if (queryString != null && !queryString.trim().isEmpty()) {
                logMessage.append("Query: ").append(queryString).append("\n");
            }
            if (!meaningfulHeaders.trim().isEmpty()) {
                logMessage.append("Headers: ").append(meaningfulHeaders).append("\n");
            }
            if (!body.trim().isEmpty()) {
                logMessage.append("Body: ").append(body).append("\n");
            }
        }

        logger.info(logMessage.toString());
    }

    private boolean shouldExcludePath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith) ||
                path.endsWith(".css") || path.endsWith(".js") ||
                path.endsWith(".png") || path.endsWith(".jpg") ||
                path.endsWith(".jpeg") || path.endsWith(".gif") ||
                path.endsWith(".ico") || path.endsWith(".svg") ||
                path.endsWith(".woff") || path.endsWith(".woff2") ||
                path.endsWith(".ttf");
    }

    private String extractBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }

    private String extractMeaningfulHeaders(HttpServletRequest request) {
        StringBuilder meaningfulHeaders = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerNameLower = headerName.toLowerCase();

            if (!SYSTEM_HEADERS.contains(headerNameLower)
                    && !headerNameLower.startsWith("sec-")
                    && !headerNameLower.startsWith("x-forwarded")
                    && !headerNameLower.startsWith("x-real")) {
                String headerValue = request.getHeader(headerName);
                meaningfulHeaders.append(headerName)
                        .append(": ").append(headerValue).append("; ");
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
