package org.rakuten.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TenantFilterTest {

    @InjectMocks
    private TenantFilter tenantFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        TenantContext.clear();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_withTenantId() throws ServletException, IOException {
        // Given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("tenant_id", "tenant1")
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                assertEquals("tenant1", TenantContext.getCurrentTenant());
                return null;
            }
        }).when(filterChain).doFilter(request, response);

        // When
        tenantFilter.doFilterInternal(request, response, filterChain);
    }

    @Test
    void testDoFilterInternal_withoutTenantId() throws ServletException, IOException {
        // Given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("dummy", "value")
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                assertNull(TenantContext.getCurrentTenant());
                return null;
            }
        }).when(filterChain).doFilter(request, response);

        // When
        tenantFilter.doFilterInternal(request, response, filterChain);
    }
}
