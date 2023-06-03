package com.devinhouse.labsky.services;

import com.devinhouse.labsky.exceptions.AssentoNaoEncontradoException;
import com.devinhouse.labsky.exceptions.AssentoReservadoException;
import com.devinhouse.labsky.exceptions.BagagensNaoDespachadasEmFileiraDeEmergenciaException;
import com.devinhouse.labsky.exceptions.MenorEmFileiraDeEmergenciaException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
                    new Assento(1L,"A1", '0', '0'),
                    new Assento(2L, "A2", '0', '0')
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

    @Nested
    @DisplayName("Método: reservarAssento")
    class ReservarAssento {
        @Test
        @DisplayName("Quando assento informado não for válido, deve lançar exceção")
        void reservarAssento_assentoInvalido() {
            Mockito.when(repository.findByAssento(Mockito.anyString())).thenThrow(AssentoNaoEncontradoException.class);

            assertThrows(AssentoNaoEncontradoException.class,
                    () -> service.reservarAssento("1A", LocalDate.now(), Mockito.anyBoolean()));
        }

        @Test
        @DisplayName("Quando assento estiver reservado, deve lançar exceção")
        void reservarAssento_assentoReservado() {
            Assento assento = new Assento(1L, "1A", '1', '1');

            Mockito.when(repository.findByAssento(Mockito.anyString())).thenReturn(Optional.of(assento));

            assertThrows(AssentoReservadoException.class,
                    () -> service.reservarAssento(assento.getAssento(), LocalDate.now(), Mockito.anyBoolean()));
        }

        @Test
        @DisplayName("Quando passageiro menor de idade reservar assento de emergencia, deve lançar exceção")
        void reservarAssento_menorDeIdadeEmAssentoDeEmergencia() {
            Assento assento = new Assento(1L, "1A", '1', '0');

            Mockito.when(repository.findByAssento(Mockito.anyString())).thenReturn(Optional.of(assento));

            assertThrows(MenorEmFileiraDeEmergenciaException.class,
                    () -> service.reservarAssento(
                            assento.getAssento(),
                            LocalDate.of(2010, 11, 1),
                            Mockito.anyBoolean()));
        }

        @Test
        @DisplayName("Quando passageiro reservar assento de emergencia e não despachar a mala, deve lançar exceção")
        void reservarAssento_malaNaoDespachadaEmAssentoDeEmergencia() {
            Assento assento = new Assento(1L, "1A", '1', '0');

            Mockito.when(repository.findByAssento(Mockito.anyString())).thenReturn(Optional.of(assento));

            assertThrows(BagagensNaoDespachadasEmFileiraDeEmergenciaException.class,
                    () -> service.reservarAssento(
                            assento.getAssento(),
                            LocalDate.of(2000, 1, 1),
                            false));
        }

        @Test
        @DisplayName("Quando reserva de assento for bem sucedida, deve alterar propriedade 'reservado' para '1' e retornar o assento")
        void reservarAssento_alteraReservado() {
            Assento assento = new Assento(1L, "1A", '1', '0');

            Mockito.when(repository.findByAssento(Mockito.anyString())).thenReturn(Optional.of(assento));
            Mockito.when(repository.save(Mockito.any(Assento.class))).thenReturn(assento);

            Assento resultado = service.reservarAssento(
                    assento.getAssento(),
                    LocalDate.of(1990,1,1),
                    true);

            assertEquals('1', resultado.getReservado());
            assertEquals(assento.getAssento(), resultado.getAssento());
        }
    }

}