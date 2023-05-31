package com.devinhouse.labsky.services;

import com.devinhouse.labsky.models.Assento;
import com.devinhouse.labsky.repositories.AssentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssentoServiceTest {
    @Mock
    private AssentoRepository repository;
    @InjectMocks
    private AssentoService service;

    @Nested
    @DisplayName("Método: getAssentos")
    class GetAssentos {
        @Test
        @DisplayName("Quando houver assentos cadastrados, deve retornar lista com os assentos")
        void getAssentos() {
            List<Assento> assentos = List.of(
                    new Assento(1L,"A1", '0'),
                    new Assento(2L, "A2", '0')
            );
            Mockito.when(repository.findAll()).thenReturn(assentos);

            List<String> resultado = service.getAssentos();

            assertEquals(assentos.size(), resultado.size());
            assertEquals(assentos.get(0).getAssento(), resultado.get(0));
        }

        @Test
        @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
        void getAssentos_vazio() {
            List<String> resultado = service.getAssentos();

            assertTrue(resultado.isEmpty());
        }
    }

}