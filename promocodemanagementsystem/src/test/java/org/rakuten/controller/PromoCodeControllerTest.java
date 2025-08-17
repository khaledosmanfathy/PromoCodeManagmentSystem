package org.rakuten.controller;

import org.junit.jupiter.api.Test;
import org.rakuten.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromoCodeController.class)
public class PromoCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromoCodeService promoCodeService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenGetAllPromoCodes_thenReturnOk() throws Exception {
        mockMvc.perform(get("/api/promo-codes"))
                .andExpect(status().isOk());
    }
}
