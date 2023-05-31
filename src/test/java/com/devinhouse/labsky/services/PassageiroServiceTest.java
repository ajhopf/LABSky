package com.devinhouse.labsky.services;

import com.devinhouse.labsky.models.Passageiro;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {
    @Mock
    private PassageiroRepository repository;
    @InjectMocks
    private PassageiroService service;

    @Nested
    @DisplayName("Método: getPassageiros")
    class getPassageiros {
        @Test
        @DisplayName("Quando repositório contém registros, deve retornar lista com os registros")
        void getPassageiros() {
            //given
            List<Passageiro> passageiros = List.of(
                    new Passageiro("000.000.000-00", "André", LocalDate.now(), "OURO", 100),
                    new Passageiro("111.111.111-11", "Rachel", LocalDate.now(), "PRATA", 50)
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

}