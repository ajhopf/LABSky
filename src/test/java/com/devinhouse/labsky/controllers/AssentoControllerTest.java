package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.services.AssentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AssentoController.class)
class AssentoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AssentoService service;

    @Nested
    @DisplayName("Método: getAssentos")
    class GetAssentos {
        @Test
        @DisplayName("Quando houver assentos cadastrados, deve retornar lista com os assentos")
        void getAssentos() throws Exception {
            List<String> assentos = List.of("1A", "2A");
            Mockito.when(service.getAssentos()).thenReturn(assentos);
            mockMvc.perform(get("/assentos").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
        void getAssentos_vazio() throws Exception {
            mockMvc.perform(get("/assentos").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }

}