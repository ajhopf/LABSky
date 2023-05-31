package com.devinhouse.labsky.services;

import com.devinhouse.labsky.repositories.AssentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
