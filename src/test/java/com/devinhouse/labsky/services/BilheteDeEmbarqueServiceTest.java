package com.devinhouse.labsky.services;

import com.devinhouse.labsky.models.BilheteDeEmbarque;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.BilheteDeEmbarqueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BilheteDeEmbarqueServiceTest {
    @Mock
    private BilheteDeEmbarqueRepository repository;
    @InjectMocks
    private BilheteDeEmbarqueService service;

    @Nested
    @DisplayName("Método: getBilheteDeEmbarque")
    class GetBilheteDeEmbarque {
        @Test
        @DisplayName("Quando existe BilheteDeEmbarque com o cpf informado, deve retornar Optional com o bilhete de embarque")
        void getBilheteDeEmbarque() {
            Passageiro passageiro = new Passageiro();
            BilheteDeEmbarque bilheteDeEmbarque = new BilheteDeEmbarque(1L, "12345", "1A",true, LocalDateTime.now(), passageiro);
            String cpf = "123";
            Mockito.when(repository.findByPassageiroCpf(cpf)).thenReturn(Optional.of(bilheteDeEmbarque));

            Optional<BilheteDeEmbarque> resultado = service.getBilheteDeEmbarque(cpf);

            assertTrue(resultado.isPresent());
            assertEquals(bilheteDeEmbarque, resultado.get());
        }

        @Test
        @DisplayName("Quando não existe BilheteDeEmbarque com o cpf informado, deve retornar Optional vazio")
        void getBilheteDeEmbarque_vazio() {
            String cpf = "123";
            Mockito.when(repository.findByPassageiroCpf(cpf)).thenReturn(Optional.empty());

            Optional<BilheteDeEmbarque> resultado = service.getBilheteDeEmbarque(cpf);

            assertFalse(resultado.isPresent());
        }
    }

    @Nested
    @DisplayName("Método: cpfJaTemBilhete")
    class CpfJaTemBilhete {
        @Test
        @DisplayName("Quando existe BilheteDeEmbarque com o cpf informado, deve retornar true")
        void cpfJaTemBilhete() {
            Passageiro passageiro = new Passageiro();
            BilheteDeEmbarque bilheteDeEmbarque = new BilheteDeEmbarque(1L, "12345", "1A",true, LocalDateTime.now(), passageiro);
            Mockito.when(repository.findByPassageiroCpf(Mockito.anyString())).thenReturn(Optional.of(bilheteDeEmbarque));

            assertTrue(service.cpfJaTemBilhete("000"));
        }

        @Test
        @DisplayName("Quando não existe BilheteDeEmbarque com o cpf informado, deve retornar false")
        void cpfJaTemBilhete_naoTemBilhete() {
                assertFalse(service.cpfJaTemBilhete("000"));
        }

    }
}