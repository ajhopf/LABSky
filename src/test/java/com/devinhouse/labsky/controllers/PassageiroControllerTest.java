package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.dtos.checkin.CheckinResponseDto;
import com.devinhouse.labsky.exceptions.*;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.services.BilheteDeEmbarqueService;
import com.devinhouse.labsky.services.PassageiroService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PassageiroController.class)
class PassageiroControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PassageiroService service;
    @MockBean
    private BilheteDeEmbarqueService bilheteDeEmbarqueService;

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
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString()))
                    .thenReturn(passageiro);
            mockMvc.perform(get("/passageiros/{cpf}", passageiro.getCpf()).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cpf", is(passageiro.getCpf())));
        }

        @Test
        @DisplayName("Quando passageiro com cpf informado não estiver cadastrado, deve retornar erro com status 404")
        void getPassageiro_naoEncontrado() throws Exception {
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString()))
                    .thenThrow(PassageiroNaoEncontradoException.class);
            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando passageiro com cpf informado não estiver cadastrado, deve retornar mensagem de erro no json body")
        void getPassageiro_naoEncontradoMensagemDeErro() throws Exception {
            Mockito.when(service.getPassageiroPeloCpf(Mockito.anyString()))
                    .thenThrow(PassageiroNaoEncontradoException.class);
            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Passageiro com cpf informado não encontrado(a)")));
        }
    }

    @Nested
    @DisplayName("Método: realizarCheckin")
    class RealizarCheckin {
        @Test
        @DisplayName("Quando não houver passageiro registrado com o cpf informado, deve retornar mensagem de erro e status 404")
        void realizarCheckin_cpfNaoRegistrado() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", true);
            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenThrow(PassageiroNaoEncontradoException.class);
            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Passageiro com cpf informado não encontrado(a)")))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando assento informado na requisição não existir, deve retornar mensagem de erro e status 404")
        void realizarCheckin_assentoNaoExiste() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", true);
            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenThrow(AssentoNaoEncontradoException.class);
            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Assento não encontrado")))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando assento informado na requisição já estiver reservado, deve retornar mensagem de erro e status 409")
        void realizarCheckin_assentoJaReservado() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", true);
            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenThrow(AssentoReservadoException.class);
            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Assento já está reservado")))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Quando passageiro for menor de idade e escolher assento de fileira de emergência, deve retornar mensagem de erro e status 400")
        void realizarCheckin_menorEmFileiraDeEmergencia() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", true);
            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenThrow(MenorEmFileiraDeEmergenciaException.class);
            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Não é possível escolher um assento de emergência para um passageiro menor de idade.")))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando passageiro escolher fileira de emergência e não despachar as malas, deve retornar mensagem de erro e status 400")
        void realizarCheckin_naoDespachouAsMalas() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", false);
            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenThrow(BagagensNaoDespachadasEmFileiraDeEmergenciaException.class);
            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Assento escolhido está em uma fileira de emergência. Bagagens devem ser despachadas.")))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando checkin for realizado com sucesso, deve retornar informações de checkin e status 200")
        void realizarCheckin() throws Exception {
            CheckinRequestDto requestDto = new CheckinRequestDto("111", "1A", false);

            String eticket = UUID.randomUUID().toString();
            CheckinResponseDto responseDto = new CheckinResponseDto(eticket, LocalDateTime.now());

            Mockito.when(service.realizarCheckin(Mockito.any(CheckinRequestDto.class)))
                    .thenReturn(responseDto);

            String requestJson = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.eticket", is(eticket)))
                    .andExpect(status().isOk());
        }
    }
}