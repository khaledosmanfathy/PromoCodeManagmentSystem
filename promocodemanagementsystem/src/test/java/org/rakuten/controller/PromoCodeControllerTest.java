package org.rakuten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.rakuten.model.PromoCode;
import org.rakuten.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromoCodeController.class)
public class PromoCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromoCodeService promoCodeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenGetAllPromoCodes_thenReturnOk() throws Exception {
        mockMvc.perform(get("/api/promo-codes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPromoCode_withAdminRole_shouldSucceed() throws Exception {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("TEST");

        when(promoCodeService.createPromoCode(any(PromoCode.class))).thenReturn(promoCode);

        mockMvc.perform(post("/api/promo-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCode)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "BUSINESS")
    void createPromoCode_withBusinessRole_shouldBeForbidden() throws Exception {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("TEST");

        mockMvc.perform(post("/api/promo-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCode)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPromoCode_withoutUser_shouldBeUnauthorized() throws Exception {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("TEST");

        mockMvc.perform(post("/api/promo-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCode)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePromoCode_withAdminRole_shouldSucceed() throws Exception {
        long promoCodeId = 1L;
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("UPDATED");

        given(promoCodeService.updatePromoCode(any(Long.class), any(PromoCode.class))).willReturn(promoCode);

        mockMvc.perform(put("/api/promo-codes/{id}", promoCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCode)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "BUSINESS")
    void updatePromoCode_withBusinessRole_shouldBeForbidden() throws Exception {
        long promoCodeId = 1L;
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("UPDATED");

        mockMvc.perform(put("/api/promo-codes/{id}", promoCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCode)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePromoCode_withAdminRole_shouldSucceed() throws Exception {
        long promoCodeId = 1L;
        mockMvc.perform(delete("/api/promo-codes/{id}", promoCodeId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "BUSINESS")
    void deletePromoCode_withBusinessRole_shouldBeForbidden() throws Exception {
        long promoCodeId = 1L;
        mockMvc.perform(delete("/api/promo-codes/{id}", promoCodeId))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "BUSINESS")
    void getPromoCodeById_withBusinessRole_shouldSucceed() throws Exception {
        long promoCodeId = 1L;
        PromoCode promoCode = new PromoCode();
        promoCode.setId(promoCodeId);
        promoCode.setCode("TEST");

        given(promoCodeService.getPromoCodeById(promoCodeId)).willReturn(promoCode);

        mockMvc.perform(get("/api/promo-codes/{id}", promoCodeId))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
