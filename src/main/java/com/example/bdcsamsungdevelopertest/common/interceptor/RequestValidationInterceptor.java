package com.example.bdcsamsungdevelopertest.common.interceptor;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.bdcsamsungdevelopertest.common.util.RequestValue.URI_REQUEST_FORMAT;
import static com.example.bdcsamsungdevelopertest.common.util.StringUtilExtension.subStringDomainFromUri;

@Component
public class RequestValidationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Map<String, Map<String, Map<String, Boolean>>>> registeredRequests = URI_REQUEST_FORMAT;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String methodKey = request.getMethod().toUpperCase();
        String domainKey = subStringDomainFromUri(uri);
        Map<String, Map<String,Boolean>> filteredDomain = registeredRequests.get(methodKey).get(domainKey);

        Map<String, Boolean> paramRequirements = extractParamRequirements(filteredDomain, methodKey, uri);
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            StringBuilder payload = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payload.append(line);
                }
            }

            // Parse the payload into JsonNode for validation
            JsonNode jsonNode = objectMapper.readTree(payload.toString());

            // Validate required fields (example for "content" and "videoId")
            if (!jsonNode.hasNonNull("content") || !jsonNode.hasNonNull("videoId")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing required properties: content, videoId\"}");
                return false;
            }
        }
        return true;
    }

    private Map<String, Boolean> extractParamRequirements(
        Map<String, Map<String,Boolean>> filteredDomain,
        String methodKey,
        String uri
    ) {
        if(filteredDomain.isEmpty()) throw new BadRequestException("없는 요청을 보내셨습니다.");

        Map<String, Boolean> extractedDomain = new HashMap<>();
        if(filteredDomain.size() < 2) {
            Map.Entry<String, Map<String, Boolean>> filteredDomainEntry = filteredDomain.entrySet().iterator().next();
            extractedDomain = filteredDomainEntry.getValue();
        } else {
            String uriKey = methodKey + "]" + uri;
            for(String key : filteredDomain.keySet()) {
                String regex = key.replaceAll("\\{[^/]+\\}", "[^/]+");
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uriKey);
                if(matcher.matches()) {
                    extractedDomain = filteredDomain.get(key);
                }
            }
        }

        if(extractedDomain == null) throw new BadRequestException("없는 요청을 보내셨습니다.");
        return extractedDomain;
    }
}
