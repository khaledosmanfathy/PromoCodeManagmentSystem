package org.rakuten.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TenantIdentifierResolverTest {

    @InjectMocks
    private TenantIdentifierResolver tenantIdentifierResolver;

    @BeforeEach
    void setUp() {
        TenantContext.clear();
        ReflectionTestUtils.setField(tenantIdentifierResolver, "defaultTenant", "public");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testResolveCurrentTenantIdentifier_withTenantInContext() {
        // Given
        TenantContext.setCurrentTenant("tenant1");

        // When
        String tenantId = tenantIdentifierResolver.resolveCurrentTenantIdentifier();

        // Then
        assertEquals("tenant1", tenantId);
    }

    @Test
    void testResolveCurrentTenantIdentifier_withoutTenantInContext() {
        // When
        String tenantId = tenantIdentifierResolver.resolveCurrentTenantIdentifier();

        // Then
        assertEquals("public", tenantId);
    }
}
