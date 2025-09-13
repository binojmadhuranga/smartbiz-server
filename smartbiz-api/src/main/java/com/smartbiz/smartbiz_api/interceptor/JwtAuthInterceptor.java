package com.smartbiz.smartbiz_api.interceptor;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Allow CORS preflight through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return false;
        }

        // validate token
        if (!jwtUtil.validateJwtToken(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return false;
        }

        // attach claims + userId
        Claims claims = jwtUtil.getClaimsFromToken(authHeader);
        if (claims != null) {
            request.setAttribute("email", claims.getSubject());
            Object role = claims.get("role");
            request.setAttribute("role", role != null ? role.toString() : null);
            Object userId = claims.get("userId");
            if (userId != null) {
                try { request.setAttribute("userId", Long.valueOf(userId.toString())); } catch (NumberFormatException ignored) {}
            }
        }
        return true; // proceed to controller
    }
}
