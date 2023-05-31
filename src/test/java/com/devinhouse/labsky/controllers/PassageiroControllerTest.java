package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.exceptions.PassageiroNaoEncontradoException;
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
import static org.hamcrest.Matchers.containsStringIgnoringCase;
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

    @Nested
    @DisplayName("Método: getPassageiro")
    class GetPassageiro {
        @Test
        @DisplayName("Quando passageiro com cpf informado estiver cadastrado, deve retornar o passageiro")
        void getPassageiro() throws Exception {
            Passageiro passageiro = new Passageiro("000.000.000-00", "André", LocalDate.now(), "OURO", 100);
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString())).thenReturn(passageiro);
            mockMvc.perform(get("/passageiros/{cpf}", passageiro.getCpf()).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cpf", is(passageiro.getCpf())));
        }

        @Test
        @DisplayName("Quando passageiro com cpf informado não estiver cadastrado, deve retornar erro com status 404")
        void getPassageiro_naoEncontrado() throws Exception {
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString())).thenThrow(PassageiroNaoEncontradoException.class);
            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando passageiro com cpf informado não estiver cadastrado, deve retornar mensagem de erro no json body")
        void getPassageiro_naoEncontradoMensagemDeErro() throws Exception {
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString())).thenThrow(PassageiroNaoEncontradoException.class);
            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Passageiro com cpf informado não encontrado(a)")));
        }
    }
}