package com.devinhouse.labsky.services;

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
}
