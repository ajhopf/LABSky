package com.devinhouse.labsky.services;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.exceptions.PassageiroNaoEncontradoException;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.AssentoRepository;
import com.devinhouse.labsky.repositories.PassageiroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {
    @Mock
    private PassageiroRepository repository;
    @Mock
    private AssentoRepository assentoRepository;
    @InjectMocks
    private PassageiroService service;
    @InjectMocks
    private AssentoService assentoService;

    @Nested
    @DisplayName("Método: getPassageiros")
    class GetPassageiros {
        @Test
        @DisplayName("Quando repositório contém registros, deve retornar lista com os registros")
        void getPassageiros() {
            //given
            List<Passageiro> passageiros = List.of(
                    new Passageiro("000.000.000-00", "André", LocalDate.now(), "OURO", 100, null, null, null, null),
                    new Passageiro("111.111.111-11", "Rachel", LocalDate.now(), "PRATA", 50, null, null, null, null)
            );
            Mockito.when(repository.findAll()).thenReturn(passageiros);
            //when
            List<Passageiro> resultado = service.getPassageiros();
            //then
            assertEquals(passageiros.size(), resultado.size());
            assertEquals(passageiros.get(0), resultado.get(0));
        }

        @Test
        @DisplayName("Quando repositório não contém registro, deve retornar lista vazia")
        void getPassageiros_listaVazia() {
            List<Passageiro> passageiros = service.getPassageiros();
            assertTrue(passageiros.isEmpty());
        }
    }

    @Nested
    @DisplayName("Método: getPassageiroPeloCpf")
    class GetPassageiroPeloCpf {
        @Test
        @DisplayName("Quando encontrar um passageiro pelo cpf, deve retornar este passageiro")
        void getPassageiroPeloCpf() {
            Passageiro passageiro = new Passageiro("000.000.000-00", "André", LocalDate.now(), "OURO", 100, null, null, null, null);
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(passageiro));
            Passageiro resultado = service.getPassageiroPeloCpf(passageiro.getCpf());
            assertEquals(passageiro, resultado);
        }

        @Test
        @DisplayName("Quando não encontrar um passageiro com o cpf informado, deve lançar exceção")
        void getPassageiroPeloCpf_passageiroNaoEncontrado() {
            assertThrows(PassageiroNaoEncontradoException.class, () -> service.getPassageiroPeloCpf("000"));
        }
    }

    @Nested
    @DisplayName("Método: realizarCheckin")
    class RealizarCheckin {
        @Test
        @DisplayName("Quando não encontrar um passageiro com o cpf informado, deve lançar exceção")
        void realizarCheckin_passageiroNaoEncontrado() {
            CheckinRequestDto requestDto = new CheckinRequestDto("000", "1A", Mockito.anyBoolean());

            assertThrows(PassageiroNaoEncontradoException.class, () -> service.realizarCheckin(requestDto));
        }
    }

    @Nested
    @DisplayName("Método: acumularMilhas")
    class AcumularMilhas {
        @Test
        @DisplayName("Quando classificacao for VIP, deve retornar milhas atuais mais 100")
        void acumularMilhas_vip() {
            Integer milhas = 100;

            String classificacao = "VIP";
            Integer resultado = service.acumularMilhas(milhas, classificacao);

            assertEquals(milhas + 100, resultado);
        }

        @Test
        @DisplayName("Quando classificacao for OURO, deve retornar milhas atuais mais 80")
        void acumularMilhas_ouro() {
            Integer milhas = 100;

            String classificacao = "OURO";
            Integer resultado = service.acumularMilhas(milhas, classificacao);

            assertEquals(milhas + 80, resultado);
        }

        @Test
        @DisplayName("Quando classificacao for PRATA, deve retornar milhas atuais mais 50")
        void acumularMilhas_prata() {
            Integer milhas = 100;

            String classificacao = "PRATA";
            Integer resultado = service.acumularMilhas(milhas, classificacao);

            assertEquals(milhas + 50, resultado);
        }

        @Test
        @DisplayName("Quando classificacao for BRONZE, deve retornar milhas atuais mais 30")
        void acumularMilhas_bronze() {
            Integer milhas = 100;

            String classificacao = "BRONZE";
            Integer resultado = service.acumularMilhas(milhas, classificacao);

            assertEquals(milhas + 30, resultado);
        }

        @Test
        @DisplayName("Quando classificacao for ASSOCIADO, deve retornar milhas atuais mais 10")
        void acumularMilhas_associado() {
            Integer milhas = 100;

            String classificacao = "ASSOCIADO";
            Integer resultado = service.acumularMilhas(milhas, classificacao);

            assertEquals(milhas + 10, resultado);
        }
    }

}