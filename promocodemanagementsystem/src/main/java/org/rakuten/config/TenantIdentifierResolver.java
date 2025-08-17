package org.rakuten.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Value("${spring.jpa.properties.hibernate.default_schema:public}")
    private String defaultTenant;

    @Override
    public String resolveCurrentTenantIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // Assuming the claim is named "tenant_id". This should be configured in Keycloak.
            String tenantId = jwt.getClaimAsString("tenant_id");
            if (tenantId != null) {
                return tenantId;
            }
        }

        return defaultTenant; // Default schema for non-tenant specific operations
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}