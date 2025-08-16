package org.rakuten.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null && !tenantId.isEmpty()) {
            request.setAttribute("TENANT_ID", tenantId);
        } else {
            // Alternatively, extract tenant from JWT if preferred
            // String authHeader = request.getHeader("Authorization");
            // Parse JWT and get tenant claim
        }
        
        filterChain.doFilter(request, response);
    }
}