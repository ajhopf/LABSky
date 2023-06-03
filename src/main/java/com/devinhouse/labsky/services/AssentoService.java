package com.devinhouse.labsky.services;

import com.devinhouse.labsky.exceptions.AssentoNaoEncontradoException;
import com.devinhouse.labsky.exceptions.AssentoReservadoException;
import com.devinhouse.labsky.exceptions.BagagensNaoDespachadasEmFileiraDeEmergenciaException;
import com.devinhouse.labsky.exceptions.MenorEmFileiraDeEmergenciaException;
import com.devinhouse.labsky.models.Assento;
import com.devinhouse.labsky.repositories.AssentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AssentoService {
    @Autowired
    private AssentoRepository repository;

    public List<String> getAssentos() {
        return repository
                .findAll()
                .stream()
                .map(assento -> assento.getAssento())
                .toList();
    }

    public Assento reservarAssento(String assentoEscolhido, LocalDate dobPassageiro, Boolean malasDespachadas) {
        Assento assento = repository.findByAssento(assentoEscolhido)
                .orElseThrow(() -> new AssentoNaoEncontradoException(assentoEscolhido));

        if (assento.getReservado() == '1') {
            throw new AssentoReservadoException(assentoEscolhido);
        }

        Integer idadePassageiro = Period.between(dobPassageiro, LocalDate.now()).getYears();

        if (idadePassageiro < 18 && assento.getFileiraDeEmergencia() == '1') {
            throw new MenorEmFileiraDeEmergenciaException();
        }

        if (!malasDespachadas && assento.getFileiraDeEmergencia() == '1') {
            throw new BagagensNaoDespachadasEmFileiraDeEmergenciaException(assentoEscolhido);
        }

        assento.setReservado('1');
        assento = repository.save(assento);

        return assento;
    }
}
