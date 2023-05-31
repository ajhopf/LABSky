package com.devinhouse.labsky.services;

import com.devinhouse.labsky.exceptions.PassageiroNaoEncontradoException;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageiroService {
    @Autowired
    PassageiroRepository repository;

    public List<Passageiro> getPassageiros() {
        return repository.findAll();
    }

    public Passageiro getPassageiroPeloCpf(String cpf) {
        Passageiro passageiro = repository.findById(cpf)
                .orElseThrow(() -> new PassageiroNaoEncontradoException(cpf));

        return passageiro;
    }
}
