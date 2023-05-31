package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.services.PassageiroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class PassageiroControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ModelMapper modelMapper = new ModelMapper();
    @MockBean
    private PassageiroService service;

    @Nested
    @DisplayName("Método: getPassageiros")
    class GetPassageiros {
        @Test
        @DisplayName("Quando tem passageiros cadastrados, deve retornar lista com estes passageiros")
        void getPassageiros() throws Exception {
            List<Passageiro> passageiros = List.of(
                    new Passageiro("000.000.000-00", "André", LocalDate.now(), "OURO", 100),
                    new Passageiro("111.111.111-11", "Rachel", LocalDate.now(), "PRATA", 50)
            );
            Mockito.when(service.getPassageiros()).thenReturn(passageiros);
            mockMvc.perform(get("/passageiros").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()", is(passageiros.size())));
        }

        @Test
        @DisplayName("Quando não existem passageiros cadastrados, deve retornar lista vazia")
        void getPassageiros_listaVazia() throws Exception {
            mockMvc.perform(get("/passageiros").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }
}